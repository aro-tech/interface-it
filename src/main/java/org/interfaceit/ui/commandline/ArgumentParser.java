/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;

/**
 * Class for parsing command-line arguments for interface-it
 * @author aro_tech
 *
 */
public class ArgumentParser {
	private final String[] args;

	/**
	 * 
	 * Constructor
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
		return findValueAfterFlag("-p", "");
	}

	/**
	 * @return The name of the target interface
	 */
	public String getTargetInterfaceName() {
		return findValueAfterFlag("-n", CommandLineMain.DEFAULT_MIXIN_NAME);
	}
	
	private String findValueAfterFlag(String flag, String defaultValue) {
		for(int i=0; i < args.length; i++) {
			if(args[i].equals(flag) && args.length > i+1) {
				return args[i+1];
			}
		}
		return defaultValue;
	}

	/**
	 * @return The delegate class to wrap
	 * @throws ClassNotFoundException 
	 */
	public Class<?> getDelegateClass() throws ClassNotFoundException {
		return Class.forName(findValueAfterFlag("-c", "class.not.specified.UnknownClass"));
	}

	/**
	 * @return The directory in which to write the file
	 */
	public File getWriteDirectoryPath() {
		return new File(findValueAfterFlag("-d", "."));
	}

}
