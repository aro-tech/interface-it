/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.lang.reflect.Method;

import org.assertj.core.api.Assertions;
import org.interfaceit.util.mixin.AssertJ;
import org.junit.Before;
import org.junit.Test;

/**
 * @author aro_tech
 *
 */
public class LookupArgumentNameSourceTest implements AssertJ {
	LookupArgumentNameSource underTest = new LookupArgumentNameSource();
	Method method1 = null;
	Method method2 = null;

	@Before
	public void setUp() throws Exception {
		method1 = Assertions.class.getDeclaredMethod("assertThat", String.class);
		underTest.add("assertThat", 0, "String", "actual");
		method2 = Assertions.class.getDeclaredMethod("assertThat", Long.class);
	}

	@Test
	public void can_look_up_names() {
		assertThat(underTest.getArgumentNameFor(method1, 0)).isEqualTo("actual");
	}

	@Test
	public void can_handle_unknown_method() {
		assertThat(underTest.getArgumentNameFor(method2, 0)).isEqualTo("arg0");
	}

	@Test
	public void can_handle_parameter_count_mismatch() {
		assertThat(underTest.getArgumentNameFor(method1, 1)).isEqualTo("arg1");
	}
}
