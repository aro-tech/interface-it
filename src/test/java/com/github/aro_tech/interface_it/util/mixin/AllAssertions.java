/**
 * 
 */
package com.github.aro_tech.interface_it.util.mixin;

/**
 * @author aro_tech
 *
 */
public interface AllAssertions extends AssertJ, JUnitAssert {

	/**
	 * This override is necessary because of a name collision between JUnit and
	 * AssertJ. By default, JUnit's version of fail() is called 
	 */
	@Override
	default void fail(String message) {
		JUnitAssert.super.fail(message);
	}
}
