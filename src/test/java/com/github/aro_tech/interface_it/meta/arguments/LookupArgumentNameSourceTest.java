/**
 * 
 */
package com.github.aro_tech.interface_it.meta.arguments;

import java.lang.reflect.Method;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.aro_tech.tdd_mixins.AssertJ;

/**
 * @author aro_tech
 *
 */
public class LookupArgumentNameSourceTest implements AssertJ {
	LookupArgumentNameSource underTest = new LookupArgumentNameSource();
	Method method1 = null;
	Method method2 = null;
	Method shortMethod = null;
	Method longMethod = null;

	@Before
	public void setUp() throws Exception {
		method1 = Assertions.class.getDeclaredMethod("assertThat", String.class);
		underTest.add("assertThat", 0, "String", "actual", 1);
		method2 = Assertions.class.getDeclaredMethod("assertThat", Long.class);
		
		shortMethod = Assert.class.getDeclaredMethod("assertArrayEquals", new boolean[0].getClass(), new boolean[0].getClass());
		longMethod = Assert.class.getDeclaredMethod("assertArrayEquals", String.class, new boolean[0].getClass(), new boolean[0].getClass());
		underTest.add("assertArrayEquals", 1, "boolean[]", "expecteds", 3);
		underTest.add("assertArrayEquals", 1, "boolean[]", "actuals", 2);

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
	
	@Test
	public void can_handle_method_with_signature_of_another_plus_one_param() {
		assertThat(underTest.getArgumentNameFor(shortMethod, 1)).isEqualTo("actuals");
		assertThat(underTest.getArgumentNameFor(longMethod, 1)).isEqualTo("expecteds");

	}

}
