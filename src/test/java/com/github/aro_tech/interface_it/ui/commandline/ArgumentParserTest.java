/**
 * 
 */
package com.github.aro_tech.interface_it.ui.commandline;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.aro_tech.interface_it.policy.DeprecationPolicy;
import com.github.aro_tech.interface_it.util.mixin.AllAssertions;
import com.github.aro_tech.interface_it.util.mixin.AssertJ;

/**
 * Unit tests for ArgumentParser
 * 
 * @author aro_tech
 *
 */
public class ArgumentParserTest implements AllAssertions {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getPackageName()}.
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
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getPackageName()}.
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
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getTargetInterfaceName()}
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
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getTargetInterfaceName()}
	 * .
	 */
	@Test
	public void returns_default_interface_name_if_unspecified() {
		ArgumentParser underTest = makeArgumentParser("thingy");
		assertThat(underTest.getTargetInterfaceName()).isEqualTo(CommandLineMain.DEFAULT_MIXIN_NAME);
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getDelegateClass()}.
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
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getDelegateClass()}.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test(expected = java.lang.ClassNotFoundException.class)
	public void throws_error_if_class_not_found() throws ClassNotFoundException {
		assertThat(makeArgumentParser().getDelegateClass()).isEqualTo(java.util.Arrays.class);
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
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
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_get_directory_path() throws IOException {
		assertThat(makeArgumentParser("", "-d", "relative/path", "").getWriteDirectoryPath().getAbsolutePath())
				.isEqualTo(new File("relative/path").getAbsolutePath());
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void returns_empty_optional_source_zip_by_default() throws IOException {
		assertThat(makeArgumentParser().getSourceZipOrJarFileObjectOption().isPresent()).isFalse();
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_get_source_jar_path() throws IOException {
		String pathname = "relative/path/Fake.jar";
		ArgumentParser argParser = makeArgumentParser("", "-s", pathname, "");
		assertThat(argParser.getSourceZipOrJarFileObjectOption().get().getAbsolutePath())
				.isEqualTo(new File(pathname).getAbsolutePath());
		assertThat(argParser.getSourceFileObjectOption().isPresent()).isFalse();
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.ui.commandline.ArgumentParser#getWriteDirectoryPath()}
	 * .
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_get_source_file_path() throws IOException {
		String pathname = "relative/path/Fake.java";
		ArgumentParser argParser = makeArgumentParser("", "-s", pathname, "");
		assertThat(argParser.getSourceZipOrJarFileObjectOption().isPresent()).isFalse();
		assertThat(argParser.getSourceFileObjectOption().get().getAbsolutePath())
				.isEqualTo(new File(pathname).getAbsolutePath());
	}

	@Test
	public void can_get_map_of_flags() {
		ArgumentParser argParser = makeArgumentParser("-n", "MyTestMockito", "-c", "org.mockito.Mockito", "-p",
				"org.test.package", "-d", "tmp", "-s",
				"<YOUR HOME DIRECTORY>\\.m2\\repository\\org\\mockito\\mockito-core\\2.0.43-beta\\mockito-core-2.0.43-beta-sources.jar");

		Map<String, String> map = argParser.getFlagMap();

		assertThat(map).isNotEmpty().containsEntry("-c", "org.mockito.Mockito").containsEntry("-n", "MyTestMockito");
	}

	@Test
	public void can_get_default_deprecation_policy() {
		ArgumentParser argParser = makeArgumentParser();
		assertThat(argParser.getDeprecationPolicy()).isEqualTo(DeprecationPolicy.PROPAGATE_DEPRECATION);
	}
	
	@Test
	public void can_override_default_deprecation_policy() {
		ArgumentParser argParser = makeArgumentParser("-i");
		assertThat(argParser.getDeprecationPolicy()).isEqualTo(DeprecationPolicy.IGNORE_DEPRECATED_METHODS);
	}

	@Test
	public void can_get_parent_mixin_name() {
		final String parentMixinName = "MatchersMixin";
		ArgumentParser argParser = makeArgumentParser("", "-P", parentMixinName);
		assertThat(argParser.hasParentMixinName()).isTrue();
		assertThat(argParser.getParentMixinName("")).isEqualTo(parentMixinName);
	}
	
	@Test
	public void can_detect_absence_of_parent_mixin_name() {
		ArgumentParser argParser = makeArgumentParser("");
		assertThat(argParser.hasParentMixinName()).isFalse();
		assertThat(argParser.getParentMixinName(null)).isNull();
	}

}
