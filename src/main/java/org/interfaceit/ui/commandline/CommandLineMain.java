/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.interfaceit.ClassCodeGenerator;
import org.interfaceit.DelegateMethodGenerator;
import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.meta.arguments.LookupArgumentNameSource;
import org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader;
import org.interfaceit.util.ZipFileUtils;

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
	 * 
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
	static void execute(String[] args, PrintStream out, ClassCodeGenerator generator, ArgumentParser argParser) {
		if (argParser.isVersionRequest()) {
			printVersion(out);
		} else if (argParser.isHelpRequest() || argParser.hasInsufficientArguments()) {
			printArgFeedbackAndHelp(args, out);
		} else {
			try {
				tryFileGeneration(out, generator, argParser);
			} catch (ClassNotFoundException cnfe) {
				out.println("Incorrect or unspecified class name in arguments: " + args);
			}
		}
	}

	private static void printVersion(PrintStream out) {
		out.println("InterfaceIt Version " + ClassCodeGenerator.PRODUCT_VERSION);
	}

	private static void printArgFeedbackAndHelp(String[] args, PrintStream out) {
		if (args.length < 1) {
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
		Optional<File> sourceArchive = argParser.getSourceZipOrJarFileObjectOption();
		ArgumentNameSource argSource = new ArgumentNameSource() {
		};
		if (sourceArchive.isPresent()) {
			argSource = new LookupArgumentNameSource();
			List<String> sourceLines;
			try {
				sourceLines = ZipFileUtils.readFilesInZipArchive(sourceArchive.get(), classToPaths(delegateClass));
				new SourceLineReadingArgumentNameLoader().parseAndLoad(sourceLines,
						(LookupArgumentNameSource) argSource);
			} catch (IOException e) {
				e.printStackTrace(); // TODO: improve error handling
			}
		}

		try {
			File result = generator.generateClassToFile(directoryPath, targetInterfaceName, delegateClass,
					targetPackageName, argSource);
			giveSuccessFeedback(result, out);
		} catch (IOException e) {
			e.printStackTrace(out); // TODO: improve error handling
		}
	}

	private static String[] classToPaths(Class<?> target) {
		List<String> paths = new ArrayList<>();
		Class<?> current = target;
		while(null != current && !"java.lang.Object".equals(current.getCanonicalName())) {
			paths.add(current.getCanonicalName().replace('.', '/') + ".java");
			current = current.getSuperclass();
		}
		return paths.toArray(new String[0]);
	}

	/**
	 * @param result
	 */
	private static void giveSuccessFeedback(File result, PrintStream out) {
		out.println("Wrote file: " + result.getAbsolutePath());
	}

}
