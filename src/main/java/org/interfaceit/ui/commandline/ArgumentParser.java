/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.interfaceit.DeprecationPolicy;

/**
 * Class for parsing command-line arguments for interface-it
 * 
 * @author aro_tech
 *
 */
public class ArgumentParser {
	private final String[] args;
	private Map<String, String> flagMap = new HashMap<>();
	private Set<Flag> allFlags = EnumSet.noneOf(Flag.class);

	static enum Flag {
		VERSION("v", "Write version number.", false),
		TARGET_INTERFACE_NAME("n", "Name of the target interface (ex: \"MyMixin\")", true),
		WRITE_DIRECTORY("d", "Directory which will contain the generated file (default value is \".\")", true),
		DELEGATE_CLASS("c", "Fully qualified delegate class name (ex: \"java.lang.Math\")", true),
		TARGET_PACKAGE("p", "The package name for the target interface (ex: \"org.example\")", true),
		SOURCE_PATH("s",
				"File path of either a .jar or .zip file or a single source file ending in .java or .txt containing source code to be used to recover argument names lost during compilation",
				true),
		IGNORE_DEPRECATED_METHODS("i",
				"Ignore all deprecated methods. Default behavior is to wrap each deprecated method and deprecate the wrapping method.",
				false),
		MISPLACED_CLASSPATH_FLAG_SHORT("cp", null, true),
		MISPLACED_CLASSPATH_FLAG_LONG("classpath", null, true);

		private final String flag;
		private final String helpMessage;
		private boolean expectsValue;

		Flag(String letter, String helpText, boolean expectsVal) {
			this.flag = "-" + letter;
			this.helpMessage = helpText;
			this.expectsValue = expectsVal;
		}

		/**
		 * @return the flag
		 */
		public String getFlagText() {
			return flag;
		}

		/**
		 * @return the helpMessage
		 */
		public String getHelpMessage() {
			return helpMessage;
		}

		/**
		 * @return true if a value is to follow the flag, false otherwise
		 */
		public boolean expectsValue() {
			return expectsValue;
		}

		/**
		 * @param expectsValue
		 *            true if a value is to follow the flag, false otherwise
		 */
		public void setExpectsValue(boolean expectsValue) {
			this.expectsValue = expectsValue;
		}

		/**
		 * 
		 * @return true if this flag should be mentioned in help messages, false
		 *         otherwise
		 */
		public boolean includeInHelpMessage() {
			return this.helpMessage != null;
		}

		static Optional<Flag> fromString(String flagText) {
			if (flagText.length() > 1 && flagText.startsWith("-")) {
				for (Flag cur : Flag.values()) {
					if (cur.getFlagText().equals(flagText)) {
						return Optional.ofNullable(cur);
					}
				}
			}
			return Optional.empty();
		}
	}

	/**
	 * 
	 * Constructor
	 * 
	 * @param args
	 */
	public ArgumentParser(String[] args) {
		super();
		this.args = args;
		Flag currentFlag = null;
		for (String currentWord : args) {
			Optional<Flag> current = Flag.fromString(currentWord);
			if (current.isPresent()) {
				currentFlag = current.get();
				if (!currentFlag.expectsValue()) {
					flagMap.put(currentFlag.getFlagText(), currentWord);
					allFlags.add(currentFlag);
					currentFlag = null;
				}
			} else if (null != currentFlag) {
				flagMap.put(currentFlag.getFlagText(), currentWord);
				allFlags.add(currentFlag);
				currentFlag = null;
			}
		}
	}

	/**
	 * @return Package name for the target interface
	 */
	public String getPackageName() {
		return findValueAfterFlag(Flag.TARGET_PACKAGE, "");
	}

	/**
	 * @return The name of the target interface
	 */
	public String getTargetInterfaceName() {
		return findValueAfterFlag(Flag.TARGET_INTERFACE_NAME, CommandLineMain.DEFAULT_MIXIN_NAME);
	}

	private String findValueAfterFlag(Flag flag, String defaultValue) {
		String value = flagMap.get(flag.getFlagText());
		if (null == value) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * @return The delegate class to wrap
	 * @throws ClassNotFoundException
	 */
	public Class<?> getDelegateClass() throws ClassNotFoundException {
		return Class.forName(findValueAfterFlag(Flag.DELEGATE_CLASS, "class.not.specified.UnknownClass"));
	}

	/**
	 * @return The directory in which to write the file
	 */
	public File getWriteDirectoryPath() {
		return new File(findValueAfterFlag(Flag.WRITE_DIRECTORY, "."));
	}

	/**
	 * @return true if the arguments indicate that this is a simple version
	 *         request
	 */
	public boolean isVersionRequest() {
		return allFlags.size() == 1 && allFlags.contains(Flag.VERSION);
	}

	/**
	 * @return Help text with args
	 */
	public static String getHelpText() {
		StringBuilder buf = new StringBuilder();
		buf.append("Possible argument flags:").append(System.lineSeparator());
		for (Flag f : Flag.values()) {
			if (f.includeInHelpMessage()) {
				buf.append(f.getFlagText()).append(" > ").append(f.getHelpMessage()).append(System.lineSeparator());
			}
		}
		return buf.toString();
	}

	/**
	 * 
	 * @return true if the arguments indicate that this request requires help
	 *         output
	 */
	public boolean isHelpRequest() {
		return args.length == 0 || args[0].toLowerCase().contains("help");
	}

	/**
	 * 
	 * @return true if there are not enough known arguments to execute
	 */
	public boolean hasInsufficientArguments() {
		return !isHelpRequest() && !isVersionRequest() && null == this.findValueAfterFlag(Flag.DELEGATE_CLASS, null);
	}

	/**
	 * @return Optional object containing possible source code archive file
	 *         reference
	 */
	public Optional<File> getSourceZipOrJarFileObjectOption() {
		return Optional.ofNullable(getSourceBundleFileFromArgs(".zip", ".jar"));
	}

	private File getSourceBundleFileFromArgs(String... expectedExtensions) {
		String path = findValueAfterFlag(Flag.SOURCE_PATH, null);
		File file = null;
		if (null != path && (expectedExtensions.length < 1 || endsWithOneOf(path, expectedExtensions))) {
			file = new File(path);
		}
		return file;
	}

	private boolean endsWithOneOf(String path, String[] expectedExtensionsLowerCase) {
		String pathLowerCase = path.toLowerCase();
		for (String cur : expectedExtensionsLowerCase) {
			if (pathLowerCase.endsWith(cur)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return optional of file object, present if the -s flag specifies a .java
	 *         or .txt file
	 */
	public Optional<File> getSourceFileObjectOption() {
		return Optional.ofNullable(getSourceBundleFileFromArgs(".java", ".txt"));
	}

	/**
	 * @return The text for the -s flag
	 */
	public String getSourceFlagText() {
		return this.findValueAfterFlag(Flag.SOURCE_PATH, "");
	}

	/**
	 * @return map of arguments where an entry contains flag and value (ex: "-c"
	 *         -> "org.mockito.Mockito")
	 */
	public Map<String, String> getFlagMap() {
		return flagMap;
	}

	/**
	 * 
	 * @return The deprecation policy specified
	 */
	public DeprecationPolicy getDeprecationPolicy() {
		if (allFlags.contains(Flag.IGNORE_DEPRECATED_METHODS)) {
			return DeprecationPolicy.IGNORE_DEPRECATED_METHODS;
		}
		return DeprecationPolicy.PROPAGATE_DEPRECATION;
	}

	/**
	 * 
	 * @return true if the arguments contain -cp or -classpath, indicating that
	 *         Java ignored the flag
	 */
	public boolean hasMisplacedClassPathFlag() {
		return allFlags.contains(Flag.MISPLACED_CLASSPATH_FLAG_SHORT)
				|| allFlags.contains(Flag.MISPLACED_CLASSPATH_FLAG_LONG);
	}

}
