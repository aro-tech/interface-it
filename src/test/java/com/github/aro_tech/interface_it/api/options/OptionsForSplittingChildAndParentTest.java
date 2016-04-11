/**
 * 
 */
package com.github.aro_tech.interface_it.api.options;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.github.aro_tech.interface_it.api.CoreMixinGenerator;
import com.github.aro_tech.interface_it.util.mixin.AllAssertions;

/**
 * Options to use for wrapping parent and child classes so that the child
 * wrapper ignores the inherited methods from the parent and instead inherits
 * them from the parent's wrapper
 * 
 * @author aro_tech
 *
 */
public class OptionsForSplittingChildAndParentTest implements AllAssertions {
	private static final String PARENT_MIXIN_NAME = "MyMatchersMixin";
	private static final String CHILD_MIXIN_NAME = "MyMockitoMixin";
	private static final File SAVE_DIRECTORY = new File(".");
	private static final String EXPECTED_PACKAGE = "org.example";
	OptionsForSplittingChildAndParent underTest = new OptionsForSplittingChildAndParent(EXPECTED_PACKAGE,
			SAVE_DIRECTORY, CHILD_MIXIN_NAME, PARENT_MIXIN_NAME, org.mockito.Mockito.class);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void can_get_package() {
		assertThat(underTest.getTargetPackageNameForDelegate(org.mockito.Mockito.class)).isEqualTo(EXPECTED_PACKAGE);
		assertThat(underTest.getTargetPackageNameForDelegate(org.mockito.Matchers.class)).isEqualTo(EXPECTED_PACKAGE);
	}

	@Test
	public void can_name_child_mixin() {
		assertThat(underTest.getTargetInterfaceNameForDelegate(org.mockito.Mockito.class)).isEqualTo(CHILD_MIXIN_NAME);
	}

	@Test
	public void can_name_parent_mixin() {
		assertThat(underTest.getTargetInterfaceNameForDelegate(org.mockito.Matchers.class))
				.isEqualTo(PARENT_MIXIN_NAME);
	}

	@Test
	public void can_get_save_directory() {
		assertThat(underTest.getMixinSaveDirectoryForDelegate(org.mockito.Matchers.class)).isEqualTo(SAVE_DIRECTORY);
	}

	@Test
	public void child_has_parent_as_supertype() {
		assertThat(underTest.getSuperTypes(org.mockito.Mockito.class)).isNotNull().isNotEmpty()
				.contains(PARENT_MIXIN_NAME);
	}

	@Test
	public void parent_has_no_supertypes() {
		assertThat(underTest.getSuperTypes(org.mockito.Matchers.class)).isNotNull().isEmpty();
	}

	@Test
	public void can_filter_parent_methods() throws NoSuchMethodException, SecurityException {
		Class<?> delegate = org.mockito.Mockito.class;
		assertThat(underTest.getMethodFilterForDelegate(delegate).test(org.mockito.Mockito.class.getDeclaredMethod("never"))).isTrue();
		assertThat(underTest.getMethodFilterForDelegate(delegate).test(org.mockito.Mockito.class.getMethod("anyChar"))).isFalse();
	}
}
