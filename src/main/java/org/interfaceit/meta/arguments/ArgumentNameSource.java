/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Represents the source of argument names used By default, reflection-found
 * names arg0, arg1 etc. are used ArgumentNameSource allows restoration of more
 * meaningful names which were not stored by the compiler
 * 
 * @author aro_tech
 *
 */
public interface ArgumentNameSource {

	/**
	 * Get the name of the given methods nth argument
	 * 
	 * @param method
	 * @param zeroBasedArgumentIndex
	 * @return The argument name (arg0, arg1, etc. if no meta info is available)
	 */
	default String getArgumentNameFor(Method method, int zeroBasedArgumentIndex) {
		Parameter[] parameters = method.getParameters();
		if(zeroBasedArgumentIndex >= parameters.length) {
			return "arg" + zeroBasedArgumentIndex;
		}
		return parameters[zeroBasedArgumentIndex].getName();
	}

}
