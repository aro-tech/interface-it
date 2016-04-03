/**
 * 
 */
package com.github.aro_tech.interface_it.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.aro_tech.interface_it.api.MixinCodeGenerator;
import com.github.aro_tech.interface_it.api.StatisticProvidingMixinGenerator;
import com.github.aro_tech.interface_it.api.StatisticsProvider;
import com.github.aro_tech.interface_it.format.CodeFormatter;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.meta.arguments.LookupArgumentNameSource;
import com.github.aro_tech.interface_it.meta.arguments.SourceLineReadingArgumentNameLoader;
import com.github.aro_tech.interface_it.statistics.GenerationStatistics;
import com.github.aro_tech.interface_it.ui.meta.error.EmptySource;
import com.github.aro_tech.interface_it.ui.meta.error.UnableToCreateOutputDirectory;
import com.github.aro_tech.interface_it.ui.meta.error.UnableToReadSource;
import com.github.aro_tech.interface_it.util.FileSystem;
import com.github.aro_tech.interface_it.util.FileUtils;
import com.github.aro_tech.interface_it.util.SourceFileReader;

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
		StatisticProvidingMixinGenerator methodGenerator = buildGenerator(argParser,
				FileUtils.getDefaultFileSystem());
		execute(args, System.out, methodGenerator, argParser, FileUtils.getDefaultSourceFileReader(), methodGenerator);
	}

	/**
	 * Create the code generator to use
	 * 
	 * @param argParser
	 * @param fileSystem
	 * @return The generator created
	 */
	static StatisticProvidingMixinGenerator buildGenerator(ArgumentParser argParser, FileSystem fileSystem) {
		return new StatisticProvidingMixinGenerator(fileSystem, argParser.getDeprecationPolicy(),
				CodeFormatter.getDefault());
	}

	/**
	 * @param args
	 * @param out
	 * @param generator
	 * @param argParser
	 * @param sourceReader
	 * @param statsProvider
	 */
	static void execute(String[] args, PrintStream out, MixinCodeGenerator generator, ArgumentParser argParser,
			SourceFileReader sourceReader, StatisticsProvider statsProvider) {
		if (argParser.isVersionRequest()) {
			printVersion(out);
		} else if (argParser.isHelpRequest() || argParser.hasInsufficientArguments()) {
			printArgFeedbackAndHelp(args, out);
		} else {
			executeMixinGeneration(args, out, generator, argParser, sourceReader, statsProvider);
		}
	}

	private static void executeMixinGeneration(String[] args, PrintStream out, MixinCodeGenerator generator,
			ArgumentParser argParser, SourceFileReader sourceReader, StatisticsProvider statsProvider) {
		try {
			generateClassFileAndPrintFeedback(out, generator, argParser, sourceReader, statsProvider,
					argParser.getDelegateClass());
		} catch (ClassNotFoundException cnfe) {
			String argsStr = String.join("\n>", Arrays.asList(args));
			out.println("Incorrect or unspecified class name in arguments: \n>" + argsStr);
			out.println("Class not found: " + cnfe.getMessage());
			warnIfUsingJarAndClasspathFlags(out, argParser);
		}
	}

	private static void warnIfUsingJarAndClasspathFlags(PrintStream out, ArgumentParser argParser) {
		if (argParser.hasMisplacedClassPathFlag()) {
			Map<String, String> flagMap = argParser.getFlagMap();
			String cp = getClasspathFromArgs(flagMap);
			if (null != cp) {
				writeWarningAboutJarAndClasspathFlags(out, flagMap, cp);
			}
		}
	}

	private static void writeWarningAboutJarAndClasspathFlags(PrintStream out, Map<String, String> flagMap, String cp) {
		out.println(
				"IMPORTANT: If you run this application using the \"-jar\" flag, the classpath in the commandline is ignored by Java.  "
						+ "\nTry adding this jar to the classpath, eliminate the -jar flag, and add the main class: "
						+ "java -cp <the path of this jar>;" + cp + " com.github.aro_tech.interface_it.ui.commandline.CommandLineMain "
						+ reconstructCommandlineFlagsWithoutClasspath(flagMap));
	}

	private static String reconstructCommandlineFlagsWithoutClasspath(Map<String, String> flagMap) {
		String flags = flagMap.entrySet().stream()
				.filter(e -> !"-cp".equals(e.getKey()) && !"-classpath".equals(e.getKey()))
				.map(e -> String.join(" ", e.getKey(), e.getValue())).collect(Collectors.joining(" "));
		return flags;
	}

	private static String getClasspathFromArgs(Map<String, String> flagMap) {
		String cp = flagMap.get("-cp");
		if (null == cp) {
			cp = flagMap.get("-classpath");
		}
		return cp;
	}

	private static void printVersion(PrintStream out) {
		out.println("InterfaceIt Version " + MixinCodeGenerator.PRODUCT_VERSION);
	}

	private static void printArgFeedbackAndHelp(String[] args, PrintStream out) {
		if (args.length < 1) {
			out.println("No arguments provided.");
		}
		out.println(ArgumentParser.getHelpText());
	}

	private static void generateClassFileAndPrintFeedback(PrintStream out, MixinCodeGenerator generator,
			ArgumentParser argParser, SourceFileReader sourceReader, StatisticsProvider statsProvider,
			Class<?> delegateClass) {
		try {
			File result = generator.generateMixinJavaFile(argParser.getWriteDirectoryPath(),
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
		return makeArgumentNameSourceBasedOnSourceLines(readSourceLines(argParser, delegateClass, sourceReader, out));
	}

	private static ArgumentNameSource makeArgumentNameSourceBasedOnSourceLines(List<String> sourceLines) {
		ArgumentNameSource argSource = new ArgumentNameSource() {
		};
		if (null != sourceLines) {
			argSource = new LookupArgumentNameSource();
			new SourceLineReadingArgumentNameLoader().parseAndLoad(sourceLines, (LookupArgumentNameSource) argSource);
		}
		return argSource;
	}

	private static List<String> readSourceLines(ArgumentParser argParser, Class<?> delegateClass,
			SourceFileReader sourceReader, PrintStream out) throws UnableToReadSource, EmptySource {
		List<String> sourceLines = tryReadingSourceFile(argParser, delegateClass, sourceReader);
		if (sourceLines.isEmpty() && !argParser.getSourceFlagText().isEmpty()) {
			throwEmptySource(argParser, out);
		}
		return sourceLines;
	}

	private static List<String> tryReadingSourceFile(ArgumentParser argParser, Class<?> delegateClass,
			SourceFileReader sourceReader) throws UnableToReadSource {
		List<String> sourceLines;
		try {
			sourceLines = getSourceCodeLines(delegateClass, argParser, sourceReader);
		} catch (IOException e) {
			throw new UnableToReadSource(e);
		}
		return sourceLines;
	}

	private static void throwEmptySource(ArgumentParser argParser, PrintStream out) throws EmptySource {
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
		out.println(stats.summarizeStatistics());
	}

}
