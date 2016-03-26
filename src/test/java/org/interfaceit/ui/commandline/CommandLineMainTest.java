/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.interfaceit.ClassCodeGenerator;
import org.interfaceit.statistics.GenerationStatistics;
import org.interfaceit.statistics.StatisticsProvider;
import org.interfaceit.util.SourceFileReader;
import org.interfaceit.util.mixin.AssertJ;
import org.interfaceit.util.mixin.Mockito;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for CommandLineMain
 * 
 * @author aro_tech
 *
 */
public class CommandLineMainTest implements AssertJ, Mockito {
	private PrintStream out;
	private ClassCodeGenerator generator;
	private SourceFileReader reader;
	private StatisticsProvider statsProvider;

	@Before
	public void setUp() throws Exception {
		out = mock(PrintStream.class);
		generator = mock(ClassCodeGenerator.class);
		reader = mock(SourceFileReader.class);
		statsProvider = mock(StatisticsProvider.class);
	}

	@Test
	public void execute_prints_result_file_path_and_stats() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File(".");
		when(generator.generateClassToFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])), eq(args[7]),
				any())).thenReturn(result);
		when(statsProvider.getStatistics()).thenReturn(setUpStatsWith2MethodsAnd1Constant());
		
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, statsProvider);
		
		verify(out).println(contains(result.getAbsolutePath()));
		verify(out).println(contains("2 methods"));
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
		when(generator.generateClassToFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])), eq(args[7]),
				any())).thenReturn(result);
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Warning: No source code was found"));
		verify(out).println(contains(result.getAbsolutePath()));
	}

	@Test
	public void execute_prints_version_if_flag() {
		String[] args = { "-v" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains(ClassCodeGenerator.PRODUCT_VERSION));
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
		when(generator.generateClassToFile(eq(new File(args[1])), eq(args[3]), eq(Class.forName(args[5])), eq(args[7]),
				any())).thenThrow(new IOException());
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args), reader, null);
		verify(out).println(contains("Error writing output"));
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
				+ " java -cp <the path of this jar>;whatever org.interfaceit.ui.commandline.CommandLineMain -n Thingy -p com.example -c org.whoosit.Whazzit -s bogus.jar -d ."));
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
				+ " java -cp <the path of this jar>;whatever org.interfaceit.ui.commandline.CommandLineMain -n Thingy -p com.example -c org.whoosit.Whazzit -s bogus.jar -d ."));
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
}
