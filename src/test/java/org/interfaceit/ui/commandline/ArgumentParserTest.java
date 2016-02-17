/**
 * 
 */
package org.interfaceit.ui.commandline;

import java.io.File;
import java.io.IOException;

import org.interfaceit.util.mixin.AssertJ;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for ArgumentParser
 * 
 * @author aro_tech
 *
 */
public class ArgumentParserTest implements AssertJ {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	// String[] args = { "-d", examplesDir.getAbsolutePath(), "-n", "Math",
	// "-c", "java.lang.Math", "-p",
	// packageName };

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getPackageName()}.
	 */
	@Test
	public void can_find_package_name_after_flag() {
		ArgumentParser underTest = makeArgumentParser("whatever", "-p", "my.package", "thingy");
		assertThat(underTest.getPackageName()).isEqualTo("my.package");
		underTest = makeArgumentParser("", "whatever", "-p", "my.package.too", "thingy", "");
		assertThat(underTest.getPackageName()).isEqualTo("my.package.too");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getPackageName()}.
	 */
	@Test
	public void returns_empty_package_name_by_default() {
		ArgumentParser underTest = makeArgumentParser();
		assertThat(underTest.getPackageName()).isEqualTo("");
		underTest = makeArgumentParser("-p");
		assertThat(underTest.getPackageName()).isEqualTo("");
	}

	private ArgumentParser makeArgumentParser(String... args) {
		return new ArgumentParser(args);
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getTargetInterfaceName()}
	 * .
	 */
	@Test
	public void can_get_target_interface_name() {
		ArgumentParser underTest = makeArgumentParser("-n", "MyMixin", "thingy");
		assertThat(underTest.getTargetInterfaceName()).isEqualTo("MyMixin");
		underTest = makeArgumentParser("", "", "", "-n", "MyMixin2", "");
		assertThat(underTest.getTargetInterfaceName()).isEqualTo("MyMixin2");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getTargetInterfaceName()}
	 * .
	 */
	@Test
	public void returns_default_interface_name_if_unspecified() {
		ArgumentParser underTest = makeArgumentParser("thingy");
		assertThat(underTest.getTargetInterfaceName()).isEqualTo(CommandLineMain.DEFAULT_MIXIN_NAME);
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getDelegateClass()}.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test
	public void can_get_delegate_class() throws ClassNotFoundException {

		ArgumentParser underTest = makeArgumentParser("-c", "java.util.Arrays", "thingy");
		assertThat(underTest.getDelegateClass()).isEqualTo(java.util.Arrays.class);
		underTest = makeArgumentParser("", "", "", "-c", "java.util.Collections");
		assertThat(underTest.getDelegateClass()).isEqualTo(java.util.Collections.class);
	}

	@Test
	public void can_generate_help_message() {
		assertThat(ArgumentParser.getHelpText()).contains("-p ").contains("-c ").contains("-n ").contains("-d ");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getDelegateClass()}.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test(expected = java.lang.ClassNotFoundException.class)
	public void throws_error_if_class_not_found() throws ClassNotFoundException {
		assertThat(makeArgumentParser().getDelegateClass()).isEqualTo(java.util.Arrays.class);
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void returns_default_directory_path() throws IOException {
		assertThat(makeArgumentParser().getWriteDirectoryPath().getAbsolutePath())
				.isEqualTo(new File(".").getAbsolutePath());
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_get_directory_path() throws IOException {
		assertThat(makeArgumentParser("", "-d", "relative/path", "").getWriteDirectoryPath().getAbsolutePath())
				.isEqualTo(new File("relative/path").getAbsolutePath());
	}
}
