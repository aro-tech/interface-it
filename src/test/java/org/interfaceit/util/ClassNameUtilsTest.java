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

}
