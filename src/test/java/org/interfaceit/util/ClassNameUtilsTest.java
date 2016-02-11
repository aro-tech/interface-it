/**
 * 
 */
package org.interfaceit.util;

import org.interfaceit.util.mixin.AssertJ;
import org.junit.Test;

/**
 * Focused tests for class name manipulation
 * 
 * @author aro_tech
 *
 */
public class ClassNameUtilsTest implements AssertJ {

	/**
	 * Test method for
	 * {@link org.interfaceit.util.ClassNameUtils#extractSimpleName(java.lang.String)}
	 * .
	 */
	@Test
	public void can_extract_generic_type_simple_name() {
		assertThat(ClassNameUtils.extractSimpleName("org.hamcrest.Matcher<java.lang.Double>"))
				.isEqualTo("Matcher<Double>");
	}

	@Test
	public void can_extract_complex_generic_type_simple_name() {
		assertThat(ClassNameUtils.extractSimpleName(
				"org.assertj.core.api.AbstractCharSequenceAssert<?, ? extends java.lang.CharSequence>"))
						.isEqualTo("AbstractCharSequenceAssert<?, ? extends CharSequence>");
	}

	@Test
	public void can_extract_nested_type_simple_name() {
		assertThat(ClassNameUtils.extractSimpleName("org.assertj.core.api.ThrowableAssert$ThrowingCallable"))
				.isEqualTo("ThrowableAssert.ThrowingCallable");
	}

	@Test
	public void can_extract_imports() {
		assertThat(ClassNameUtils.makeImports("org.foo.Bar<com.whatsit.FoobleyWoo$Thingy, org.yippi.Skippi[]>"))
				.contains("org.foo.Bar").contains("com.whatsit.FoobleyWoo").contains("org.yippi.Skippi");
		assertThat(ClassNameUtils.makeImports("java.util.List<MyFoobleyWoo>")).contains("java.util.List").hasSize(1);
		assertThat(ClassNameUtils.makeImports("java.util.List<? extends com.whatsit.FoobleyWoo>"))
				.contains("java.util.List").contains("com.whatsit.FoobleyWoo").hasSize(2);

	}

	@Test
	public void can_convert_to_varargs_or_not() {
		assertThat(ClassNameUtils.convertToVarArgs("String[]")).isEqualTo("String...");
		assertThat(ClassNameUtils.convertToVarArgs("String")).isEqualTo("String");

	}

}
