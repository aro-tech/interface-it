/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.interfaceit.ClassCodeGenerator;
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
		ArgumentParser argParser = new ArgumentParser(args);
		execute(args, System.out, new DelegateMethodGenerator(), argParser);
	}

	/**
	 * @param args
	 * @param out
	 * @param generator
	 * @param argParser
	 */
	static void execute(String[] args, PrintStream out, ClassCodeGenerator generator,
			ArgumentParser argParser) {
		if(argParser.isVersionRequest()) {
			printVersion(out);
		} else if(argParser.isHelpRequest() || argParser.hasInsufficientArguments()) {
			printArgFeedbackAndHelp(args, out);
		} else {
			try {	
				tryFileGeneration(out, generator, argParser);
			}catch(ClassNotFoundException cnfe) {
				out.println("Incorrect or unspecified class name in arguments: " + args);
			}			
		}
	}

	private static void printVersion(PrintStream out) {
		out.println("InterfaceIt Version " + ClassCodeGenerator.PRODUCT_VERSION);
	}

	private static void printArgFeedbackAndHelp(String[] args, PrintStream out) {
		if(args.length < 1) {
			out.println("No arguments provided.");				
		}
		out.println(ArgumentParser.getHelpText());
	}

	private static void tryFileGeneration(PrintStream out, ClassCodeGenerator generator, ArgumentParser argParser)
			throws ClassNotFoundException {
		File directoryPath = argParser.getWriteDirectoryPath();
		Class<?> delegateClass = argParser.getDelegateClass();
		String targetInterfaceName = argParser.getTargetInterfaceName();
		String targetPackageName = argParser.getPackageName();

		try {
			File result = generator.generateClassToFile(directoryPath, targetInterfaceName, delegateClass,
					targetPackageName);
			giveSuccessFeedback(result, out);
		} catch (IOException e) {
			e.printStackTrace(out); // TODO: improve error handling
		}
	}

	/**
	 * @param result
	 */
	private static void giveSuccessFeedback(File result, PrintStream out) {
		out.println("Wrote file: " + result.getAbsolutePath());
	}

}
