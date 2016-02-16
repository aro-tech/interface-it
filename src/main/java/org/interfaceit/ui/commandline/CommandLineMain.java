/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;

import org.interfaceit.DelegateMethodGenerator;

/**
 * Main class for command line interface
 * 
 * @author aro_tech
 *
 */
public class CommandLineMain {

	/**
	 * Default output class name in case it's not specified in the args
	 */
	public static final String DEFAULT_MIXIN_NAME = "GeneratedMixIn";

	/**
	 * Generate code for a mix-in based on the command line arguments
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DelegateMethodGenerator generator = new DelegateMethodGenerator();
			ArgumentParser argParser = new ArgumentParser(args);
			File directoryPath = argParser.getWriteDirectoryPath();
			Class<?> delegateClass = argParser.getDelegateClass();
			String targetInterfaceName = argParser.getTargetInterfaceName();
			String targetPackageName = argParser.getPackageName();

			try {
				File result = generator.generateClassToFile(directoryPath, targetInterfaceName, delegateClass,
						targetPackageName);
				giveSuccessFeedback(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}catch(ClassNotFoundException cnfe) {
			System.out.println("Incorrect or unspecified class name in arguments: " + args);
		}

	}

	/**
	 * @param result
	 */
	private static void giveSuccessFeedback(File result) {
		System.out.println("Wrote file: " + result.getAbsolutePath());
	}

}
