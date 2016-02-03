/**
 * 
 */
package org.interfaceit.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author arothkopf
 *
 */
public class ClassNameUtilsTest {

	/**
	 * Test method for
	 * {@link org.interfaceit.util.ClassNameUtils#extractSimpleName(java.lang.String)}
	 * .
	 */
	@Test
	public void can_extract_generic_type_simple_name() {
		Assertions.assertThat(ClassNameUtils.extractSimpleName("org.hamcrest.Matcher<java.lang.Double>"))
				.isEqualTo("Matcher<Double>");
	}
	
	//org.assertj.core.api.AbstractCharSequenceAssert<?, ? extends java.lang.CharSequence>
	@Test
	public void can_extract_complex_generic_type_simple_name() {
		Assertions.assertThat(ClassNameUtils.extractSimpleName("org.assertj.core.api.AbstractCharSequenceAssert<?, ? extends java.lang.CharSequence>"))
				.isEqualTo("AbstractCharSequenceAssert<?, ? extends CharSequence>");
	}
	
	// org.assertj.core.api.ThrowableAssert$ThrowingCallable
	@Test
	public void can_extract_nested_type_simple_name() {
		Assertions.assertThat(ClassNameUtils.extractSimpleName("org.assertj.core.api.ThrowableAssert$ThrowingCallable"))
				.isEqualTo("ThrowableAssert.ThrowingCallable");
	}
}
