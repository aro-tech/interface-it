/**
 * 
 */
package com.github.aro_tech.interface_it.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import com.github.aro_tech.extended_mockito.ExtendedMockito;
import com.github.aro_tech.interface_it.api.MixinCodeGenerator;
import com.github.aro_tech.interface_it.api.MultiFileOutputOptions;
import com.github.aro_tech.interface_it.api.StatisticProvidingMixinGenerator;
import com.github.aro_tech.interface_it.api.StatisticsProvider;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.statistics.GenerationStatistics;
import com.github.aro_tech.interface_it.ui.meta.error.UnableToCreateOutputDirectory;
import com.github.aro_tech.interface_it.util.SourceFileReader;
import com.github.aro_tech.interface_it.util.mixin.AssertJ;

/**
 * Unit tests for CommandLineMain
 * 
 * @author aro_tech
 *
 */
public class CommandLineMainTest implements AssertJ, ExtendedMockito {
	private PrintStream out;
	private MixinCodeGenerator generator;
	private SourceFileReader reader;
	private StatisticsProvider statsProvider;

	@Before
	public void setUp() throws Exception {
		out = mock(PrintStream.class);
		generator = mock(MixinCodeGenerator.class);
		reader = mock(SourceFileReader.class);
		statsProvider = mock(StatisticsProvider.class);
	}

	@Test
	public void execute_prints_result_file_path_and_stats() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File("./Math.java");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor(anyString())).thenReturn(setUpStatsWith2MethodsAnd1Constant());

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("Generated 1 constant and 2 methods"));
		verifyNoMoreInteractions(out);
	}

	@Test
	public void execute_can_generate_child_and_parent() throws IOException {
		String[] args = givenSetupForParentChildGeneration();
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);
		verify(generator).generateMixinJavaFiles(
				toStringContainsAllOf("OptionsForSplittingChildAndParent [" + "targetPackage=com.example, "
						+ "saveDirectory=., childMixinName=MockitoMixin, "
						+ "parentMixinName=MatchersMixin, childClass=class org.mockito.Mockito, "
						+ "getMethodFilter()=com.github.aro_tech.interface_it.api.options.OptionsForSplittingChildAndParent$$Lambda"),
				any(), eq(org.mockito.Mockito.class), eq(org.mockito.Matchers.class));
		verify(out, VerificationModeFactory.times(2)).println(startsWith("Wrote file: "));
		verify(out).println(containsAllOf("2 constants", "1 method"));
		verify(out).println(containsAllOf("1 constant", "7 methods"));
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private String[] givenSetupForParentChildGeneration() throws IOException {
		final String childMixin = "MockitoMixin";
		final String parentMixin = "MatchersMixin";
		String[] args = { "-d", ".", "-n", childMixin, "-c", "org.mockito.Mockito", "-p", "com.example", "-P",
				parentMixin };
		List<File> result = new ArrayList<File>() {
			{
				add(new File("./" + childMixin + ".java"));
				add(new File("./" + parentMixin + ".java"));
			}
		};
		when(generator.generateMixinJavaFiles(objectMatches(opts -> true), any(), eq(org.mockito.Mockito.class),
				eq(org.mockito.Matchers.class))).thenReturn(result);
		when(statsProvider.getStatisticsFor(childMixin + ".java")).thenReturn(setUpStatsWith1MethodAnd2Constants());
		when(statsProvider.getStatisticsFor(parentMixin + ".java"))
				.thenReturn(setUpStatsWith7MethodsAnd1ConstantAndDeprecated(2));
		return args;
	}

	@Test
	public void execute_prints_result_file_path_and_stats_handling_plurals_and_singular()
			throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File("./Math.java");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor("Math.java")).thenReturn(setUpStatsWith1MethodAnd2Constants());

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("Generated 2 constants and 1 method"));
		verifyNoMoreInteractions(out);
	}

	private GenerationStatistics setUpStatsWith1MethodAnd2Constants() {
		GenerationStatistics stats = new GenerationStatistics();
		stats.incrementConstantCount();
		stats.incrementConstantCount();
		stats.incrementMethodCount();
		return stats;
	}

	@Test
	public void execute_prints_result_file_path_and_skipped_count_1() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File("./MyFile.txt");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor("MyFile.txt")).thenReturn(setUpStatsWith22MethodsAnd1ConstantAndSkipped(1));

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("22 methods"));
		verify(out).println(contains("Skipped 1 static method because of deprecation policy"));
	}

	@Test
	public void execute_prints_result_file_path_and_skipped_count_3() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File(".");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor(anyString())).thenReturn(setUpStatsWith22MethodsAnd1ConstantAndSkipped(3));

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("22 methods"));
		verify(out).println(contains("Skipped 3 static methods because of deprecation policy"));
	}

	@Test
	public void execute_prints_result_file_path_and_deprecated_count_1() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File(".");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor(anyString())).thenReturn(setUpStatsWith7MethodsAnd1ConstantAndDeprecated(1));

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("7 methods"));
		verify(out).println(contains("1 generated method is deprecated"));
	}

	@Test
	public void execute_prints_result_file_path_and_deprecated_count_5() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File("./Math.java");
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		when(statsProvider.getStatisticsFor(eq(result.getName()))).thenReturn(setUpStatsWith7MethodsAnd1ConstantAndDeprecated(5));

		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);

		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("7 methods"));
		verify(out).println(contains("5 generated methods are deprecated"));
	}

	private GenerationStatistics setUpStatsWith7MethodsAnd1ConstantAndDeprecated(int deprecated) {
		final GenerationStatistics stats = new GenerationStatistics();
		stats.incrementConstantCount();
		for (int i = 0; i < 7; i++) {
			stats.incrementMethodCount();
		}
		for (int i = 0; i < deprecated; i++) {
			stats.incrementDeprecationCount();
		}
		return stats;
	}

	private GenerationStatistics setUpStatsWith22MethodsAnd1ConstantAndSkipped(int skippedCount) {
		final GenerationStatistics stats = new GenerationStatistics();
		stats.incrementConstantCount();
		for (int i = 0; i < 22; i++) {
			stats.incrementMethodCount();
		}
		for (int i = 0; i < skippedCount; i++) {
			stats.incrementSkippedCount();
		}
		return stats;
	}

	private GenerationStatistics setUpStatsWith2MethodsAnd1Constant() {
		final GenerationStatistics stats = new GenerationStatistics();
		stats.incrementConstantCount();
		stats.incrementMethodCount();
		stats.incrementMethodCount();
		return stats;
	}

	@Test
	public void execute_prints_warning_when_source_is_empty() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example", "-s", "emptyFile.txt" };
		File result = new File(".");
		when(reader.readFilesInZipArchive(any(), any())).thenReturn(new ArrayList<String>());
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenReturn(result);
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Warning: No source code was found"));
		verify(out).println(contains(result.getAbsolutePath()));
	}

	@Test
	public void execute_prints_version_if_flag() {
		String[] args = { "-v" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains(MixinCodeGenerator.PRODUCT_VERSION));
		verifyNoMoreInteractions(out);
		verifyZeroInteractions(generator);
	}

	@Test
	public void empty_args_execute_prints_help_text() {
		String[] args = {};
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}

	@Test
	public void no_args_execute_prints_help_text() {
		String[] args = {};
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}

	@Test
	public void only_unknown_args_execute_prints_help_text() {
		String[] args = { "foo", "bar" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}

	@Test
	public void no_args_execute_prints_no_args_text() {
		String[] args = {};
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("No arguments"));
		verifyZeroInteractions(generator);
	}

	@Test
	public void help_arg_execute_prints_help_text() {
		String[] args = { "help" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}

	@Test
	public void execute_prints_error_message_on_generation_error() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenThrow(new IOException());
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Error writing output"));
	}

	@Test
	public void execute_prints_error_message_on_failure_to_create_output_directory()
			throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenThrow(new UnableToCreateOutputDirectory(new File(".")));
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Error creating output directory"));
	}

	@Test
	public void execute_can_handle_error_with_null_output_directory() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		when(generator.generateMixinJavaFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])),
				eq(args[7]), any())).thenThrow(new UnableToCreateOutputDirectory(null));
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("<NULL>"));
	}

	@Test
	public void execute_prints_error_message_source_jar_read_error() throws ClassNotFoundException, IOException {
		String sourceFile = "bogus.jar";
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example", "-s", sourceFile };
		doThrow(new IOException()).when(reader).readFilesInZipArchive(any(), any());
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Error reading specified source file: " + sourceFile));
	}

	@Test
	public void execute_prints_error_message_notifying_about_jar_flag() throws ClassNotFoundException, IOException {
		String sourceFile = "bogus.jar";
		String[] args = { "-cp", "whatever", "-d", ".", "-n", "Thingy", "-c", "org.whoosit.Whazzit", "-p",
				"com.example", "-s", sourceFile };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Incorrect or unspecified"));
		verify(out).println(contains("Class not found: org.whoosit.Whazzit"));
		verify(out).println(contains("IMPORTANT: If you run this application using the "
				+ "\"-jar\" flag, the classpath in the commandline is ignored by Java.  "
				+ "\nTry adding this jar to the classpath, eliminate the -jar flag, and add the main class:"
				+ " java -cp <the path of this jar>;whatever com.github.aro_tech.interface_it.ui.commandline.CommandLineMain -n Thingy -p com.example -c org.whoosit.Whazzit -s bogus.jar -d ."));
	}

	@Test
	public void execute_prints_error_message_notifying_about_jar_flag_recognizing_classpath_flag()
			throws ClassNotFoundException, IOException {
		String sourceFile = "bogus.jar";
		String[] args = { "-classpath", "whatever", "-d", ".", "-n", "Thingy", "-c", "org.whoosit.Whazzit", "-p",
				"com.example", "-s", sourceFile };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Incorrect or unspecified"));
		verify(out).println(contains("Class not found: org.whoosit.Whazzit"));
		verify(out).println(contains("IMPORTANT: If you run this application using the "
				+ "\"-jar\" flag, the classpath in the commandline is ignored by Java.  "
				+ "\nTry adding this jar to the classpath, eliminate the -jar flag, and add the main class:"
				+ " java -cp <the path of this jar>;whatever com.github.aro_tech.interface_it.ui.commandline.CommandLineMain -n Thingy -p com.example -c org.whoosit.Whazzit -s bogus.jar -d ."));
	}

	@Test
	public void execute_prints_error_message_without_notifying_about_jar_flag_when_cp_flag_absent()
			throws ClassNotFoundException, IOException {
		String sourceFile = "bogus.jar";
		String[] args = { "-d", ".", "-n", "Thingy", "-c", "org.whoosit.Whazzit", "-p", "com.example", "-s",
				sourceFile };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Incorrect or unspecified"));
		verify(out).println(contains("Class not found: org.whoosit.Whazzit"));
		verifyNoMoreInteractions(out);
	}

	@Test
	public void can_build_generator_which_ignores_deprecated() {
		String[] args = { "-d", ".", "-n", "Enc", "-c", "java.net.URLEncoder", "-p", "org.example", "-i" };
		String wrapperCode = buildCodeGeneratorAndGenerateCodeForClassWithDeprecatedMethod(args);
		assertThat(wrapperCode).doesNotContain("default String encode(String arg0)")
				.doesNotContain("return URLEncoder.encode(arg0);").contains("return URLEncoder.encode(arg0, arg1);");
	}

	String buildCodeGeneratorAndGenerateCodeForClassWithDeprecatedMethod(String[] args) {
		StatisticProvidingMixinGenerator built = CommandLineMain.buildGenerator(new ArgumentParser(args), null);
		assertThat(built).isNotNull();
		String wrapperCode = built.generateDelegateClassCode("org.example", "Enc", java.net.URLEncoder.class,
				new ArgumentNameSource() {
				});
		return wrapperCode;
	}

	@Test
	public void can_build_generator_which_propagates_deprecated() {
		String[] args = { "-d", ".", "-n", "Enc", "-c", "java.net.URLEncoder", "-p", "org.example" };
		String wrapperCode = buildCodeGeneratorAndGenerateCodeForClassWithDeprecatedMethod(args);
		assertThat(wrapperCode).contains("default String encode(String arg0)")
				.contains("return URLEncoder.encode(arg0);").contains("return URLEncoder.encode(arg0, arg1);");
	}
}
