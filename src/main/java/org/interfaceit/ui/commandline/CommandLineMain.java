/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.interfaceit.ClassCodeGenerator;
import org.interfaceit.StatisticProvidingClassCodeGenerator;
import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.meta.arguments.LookupArgumentNameSource;
import org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader;
import org.interfaceit.statistics.GenerationStatistics;
import org.interfaceit.statistics.StatisticsProvider;
import org.interfaceit.ui.meta.error.EmptySource;
import org.interfaceit.ui.meta.error.UnableToCreateOutputDirectory;
import org.interfaceit.ui.meta.error.UnableToReadSource;
import org.interfaceit.util.FileUtils;
import org.interfaceit.util.SourceFileReader;

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
		StatisticProvidingClassCodeGenerator methodGenerator = new StatisticProvidingClassCodeGenerator();
		execute(args, System.out, methodGenerator, argParser, new FileUtils(), methodGenerator);
	}

	/**
	 * @param args
	 * @param out
	 * @param generator
	 * @param argParser
	 * @param sourceReader
	 * @param statsProvider
	 */
	static void execute(String[] args, PrintStream out, ClassCodeGenerator generator, ArgumentParser argParser,
			SourceFileReader sourceReader, StatisticsProvider statsProvider) {
		if (argParser.isVersionRequest()) {
			printVersion(out);
		} else if (argParser.isHelpRequest() || argParser.hasInsufficientArguments()) {
			printArgFeedbackAndHelp(args, out);
		} else {
			try {
				generateClassFileAndPrintFeedback(out, generator, argParser, sourceReader, statsProvider);
			} catch (ClassNotFoundException cnfe) {
				String argsStr = String.join("\n>", Arrays.asList(args));
				out.println("Incorrect or unspecified class name in arguments: \n>" + argsStr);
				out.println("Class not found: " + cnfe.getMessage());
				warnIfUsingJarAndClasspathFlags(out, argParser);
			}
		}
	}

	private static void warnIfUsingJarAndClasspathFlags(PrintStream out, ArgumentParser argParser) {
		Map<String, String> flagMap = argParser.getFlagMap();
		String cp = getClasspathFromArgs(flagMap);
		if (null != cp) {
			String flags = flagMap.entrySet().stream()
					.filter(e -> !"-cp".equals(e.getKey()) && !"-classpath".equals(e.getKey()))
					.map(e -> String.join(" ", e.getKey(), e.getValue())).collect(Collectors.joining(" "));
			out.println(
					"IMPORTANT: If you run this application using the \"-jar\" flag, the classpath in the commandline is ignored by Java.  "
							+ "\nTry adding this jar to the classpath, eliminate the -jar flag, and add the main class: "
							+ "java -cp <the path of this jar>;" + cp
							+ " org.interfaceit.ui.commandline.CommandLineMain " + flags.toString());
		}
	}

	private static String getClasspathFromArgs(Map<String, String> flagMap) {
		String cp = flagMap.get("-cp");
		if (null == cp) {
			cp = flagMap.get("-classpath");
		}
		return cp;
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

	private static void generateClassFileAndPrintFeedback(PrintStream out, ClassCodeGenerator generator,
			ArgumentParser argParser, SourceFileReader sourceReader, StatisticsProvider statsProvider)
					throws ClassNotFoundException {
		Class<?> delegateClass = argParser.getDelegateClass();

		try {
			File result = generator.generateClassToFile(argParser.getWriteDirectoryPath(),
					argParser.getTargetInterfaceName(), delegateClass, argParser.getPackageName(),
					makeArgumentSource(argParser, sourceReader, delegateClass, out));
			printSuccessFeedback(result, out, statsProvider);
		} catch (UnableToReadSource err) {
			printErrorFeedback(out, err.getCause(),
					"Error reading specified source file: " + argParser.getSourceFlagText());
		} catch (UnableToCreateOutputDirectory err) {
			printErrorFeedback(out, err, "Error creating output directory: " + err.getTargetDirectoryPath());
		} catch (Exception e) {
			printErrorFeedback(out, e, "Error writing output.");
		}
	}

	private static ArgumentNameSource makeArgumentSource(ArgumentParser argParser, SourceFileReader sourceReader,
			Class<?> delegateClass, PrintStream out) throws UnableToReadSource {
		ArgumentNameSource argSource = new ArgumentNameSource() {
		};
		try {
			argSource = makeArgumentNameSource(argParser, delegateClass, sourceReader, out);
		} catch (EmptySource es) {
			out.println("Warning: No source code was found: " + es.getMessage());
		}
		return argSource;
	}

	private static void printErrorFeedback(PrintStream out, Throwable e, String message) {
		out.println(message);
		e.printStackTrace(out);
	}

	private static ArgumentNameSource makeArgumentNameSource(ArgumentParser argParser, Class<?> delegateClass,
			SourceFileReader sourceReader, PrintStream out) throws UnableToReadSource, EmptySource {
		List<String> sourceLines;
		try {
			sourceLines = getSourceCodeLines(delegateClass, argParser, sourceReader);
		} catch (IOException e) {
			throw new UnableToReadSource(e);
		}
		if (sourceLines.isEmpty() && !argParser.getSourceFlagText().isEmpty()) {
			String msg = "file " + argParser.getSourceFlagText();
			if (argParser.getSourceZipOrJarFileObjectOption().isPresent()) {
				try {
					msg += " with paths " + classToPaths(argParser.getDelegateClass());
				} catch (ClassNotFoundException e) {
					e.printStackTrace(out);
				}
			}
			throw new EmptySource(msg);
		}
		ArgumentNameSource argSource = new ArgumentNameSource() {
		};
		if (null != sourceLines) {
			argSource = new LookupArgumentNameSource();
			new SourceLineReadingArgumentNameLoader().parseAndLoad(sourceLines, (LookupArgumentNameSource) argSource);
		}
		return argSource;
	}

	private static List<String> getSourceCodeLines(Class<?> delegateClass, ArgumentParser argParser,
			SourceFileReader sourceReader) throws IOException {
		List<String> sourceLines = new ArrayList<>();
		Optional<File> sourceArchive = argParser.getSourceZipOrJarFileObjectOption();
		Optional<File> sourceFile = argParser.getSourceFileObjectOption();

		if (sourceArchive.isPresent()) {
			sourceLines = sourceReader.readFilesInZipArchive(sourceArchive.get(), classToPaths(delegateClass));
		} else if (sourceFile.isPresent()) {
			sourceLines = sourceReader.readTrimmedLinesFromFilePath(sourceFile.get().toPath());
		}
		return sourceLines;
	}

	private static String[] classToPaths(Class<?> target) {
		List<String> paths = new ArrayList<>();
		Class<?> current = target;
		while (null != current && !"java.lang.Object".equals(current.getCanonicalName())) {
			paths.add(current.getCanonicalName().replace('.', '/') + ".java");
			current = current.getSuperclass();
		}
		return paths.toArray(new String[0]);
	}

	private static void printSuccessFeedback(File result, PrintStream out, StatisticsProvider statsProvider) {
		out.println("Wrote file: " + result.getAbsolutePath());
		if (null != statsProvider) {
			summarizeStatistics(out, statsProvider.getStatistics());
		}
	}

	private static void summarizeStatistics(PrintStream out, GenerationStatistics stats) {
		out.println("Generated " + stats.getConstantCount() + " constant" + makePluralText(stats.getConstantCount()) + " and "
				+ stats.getMethodCount() + " method" + makePluralText(stats.getMethodCount()) + ".");
		summarizeDeprecationPolicyResults(out, stats);
	}

	private static void summarizeDeprecationPolicyResults(PrintStream out, GenerationStatistics stats) {
		if (stats.getDeprecationCount() > 0) {
			if (stats.getDeprecationCount() > 1) {
				out.println(stats.getDeprecationCount() + " generated methods are deprecated.");
			} else {
				out.println("1 generated method is deprecated.");
			}
		} else if (stats.getSkippedCount() == 1) {
			out.println("Skipped 1 static method because of deprecation policy.");
		} else if (stats.getSkippedCount() > 1) {
			out.println("Skipped " + stats.getSkippedCount() + " static methods because of deprecation policy.");
		}
	}

	private static String makePluralText(int count) {
		if (count == 1) {
			return "";
		}
		return "s";
	}

}
