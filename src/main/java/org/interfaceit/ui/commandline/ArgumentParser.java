/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.math.BigDecimal;

/**
 * Class for parsing command-line arguments for interface-it
 * 
 * @author aro_tech
 *
 */
public class ArgumentParser {
	private final String[] args;

	// String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p",
	// "com.example" };
	static enum Flag {
		VERSION("v", "Write version number."), TARGET_INTERFACE_NAME("n",
				"Name of the target interface (ex: \"MyMixin\")"), WRITE_DIRECTORY("d",
						"Directory which will contain the generated file (default value is \".\")"), DELEGATE_CLASS("c",
								"Fully qualified delegate class name (ex: \"java.lang.Math\")"), TARGET_PACKAGE("p",
										"The package name for the target interface (ex: \"org.example\")");
		private final String flag;
		private final String helpMessage;

		Flag(String letter, String helpText) {
			this.flag = "-" + letter;
			this.helpMessage = helpText;
		}

		/**
		 * @return the flag
		 */
		public String getFlag() {
			return flag;
		}

		/**
		 * @return the helpMessage
		 */
		public String getHelpMessage() {
			return helpMessage;
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
	}

	/**
	 * @param args
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
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(flag.getFlag()) && args.length > i + 1) {
				return args[i + 1];
			}
		}
		return defaultValue;
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
	 * 
	 * @return true if the arguments indicate that this is a simple version
	 *         request
	 */
	public boolean isVersionRequest() {
		return args.length == 1 && args[0].equals(Flag.VERSION.getFlag());
	}

	/**
	 * 
	 * @return Help text with args
	 */
	public static String getHelpText() {
		StringBuilder buf = new StringBuilder();
		buf.append("Possible argument flags:").append(System.lineSeparator());
		for (Flag f : Flag.values()) {
			buf.append(f.getFlag()).append(" > ").append(f.getHelpMessage()).append(System.lineSeparator());
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

}