/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.interfaceit.ClassCodeGenerator;
import org.interfaceit.util.mixin.AssertJ;
import org.interfaceit.util.mixin.Mockito;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.zohhak.api.runners.ZohhakRunner;

/**
 * Unit tests for CommandLineMain
 * 
 * @author aro_tech
 *
 */
public class CommandLineMainTest implements AssertJ, Mockito {
	private PrintStream out;
	private ClassCodeGenerator generator;

	@Before
	public void setUp() throws Exception {
		out = mock(PrintStream.class);
		generator = mock(ClassCodeGenerator.class);
	}

	@Test
	public void execute_prints_result_file_path() throws ClassNotFoundException, IOException {
		String[] args = { "-d", ".", "-n", "Math", "-c", "java.lang.Math", "-p", "com.example" };
		File result = new File(".");
		when(generator.generateClassToFile(new File(args[1]), args[3], Class.forName(args[5]), args[7]))
				.thenReturn(result);
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(contains(result.getAbsolutePath()));
	}

	@Test
	public void execute_prints_version_if_flag() {
		String[] args = { "-v"};
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(contains(ClassCodeGenerator.PRODUCT_VERSION));
		verifyNoMoreInteractions(out);
		verifyZeroInteractions(generator);
	}
	
	@Test
	public void empty_args_execute_prints_help_text() {
		String[] args = { };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}
	
	@Test
	public void no_args_execute_prints_help_text() {
		String[] args = { };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}

	@Test
	public void only_unknown_args_execute_prints_help_text() {
		String[] args = { "foo", "bar" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}
	
	@Test
	public void no_args_execute_prints_no_args_text() {
		String[] args = { };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(contains("No arguments"));
		verifyZeroInteractions(generator);
	}
	
	@Test
	public void help_arg_execute_prints_help_text() {
		String[] args = { "help" };
		CommandLineMain.execute(args, out, generator, new ArgumentParser(args));
		verify(out).println(ArgumentParser.getHelpText());
		verifyZeroInteractions(generator);
	}
}
