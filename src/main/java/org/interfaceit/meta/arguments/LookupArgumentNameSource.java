/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.interfaceit.util.ClassNameUtils;

/**
 * ArgumentNameSource which uses a dictionary lookup
 * @author aro_tech
 *
 */
public class LookupArgumentNameSource implements ArgumentNameSource {
	private Map<String, String> lookup = new HashMap<>();

	/**
	 * Constructor
	 */
	public LookupArgumentNameSource() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.interfaceit.meta.arguments.ArgumentNameSource#getArgumentNameFor(java
	 * .lang.reflect.Method, int)
	 */
	@Override
	public String getArgumentNameFor(Method method, int zeroBasedArgumentIndex) {
		String name = lookup.get(makeKey(method.getName(), zeroBasedArgumentIndex,
				ClassNameUtils.getNormalizedArgTypeForLookupKey(method, zeroBasedArgumentIndex), method.getParameterCount()));
		if (null == name) {
			name = ArgumentNameSource.super.getArgumentNameFor(method, zeroBasedArgumentIndex);
		}
		return name;
	}


	/**
	 * Add an argument name to be looked up
	 * 
	 * @param methodName
	 * @param zeroBasedArgumentIndex
	 * @param simpleTypeName
	 * @param argumentName
	 * @param methodArgumentCount 
	 */
	public void add(String methodName, int zeroBasedArgumentIndex, String simpleTypeName, String argumentName, int methodArgumentCount) {
		lookup.put(makeKey(methodName, zeroBasedArgumentIndex, simpleTypeName, methodArgumentCount), argumentName);
	}

	private String makeKey(String methodName, int zeroBasedArgumentIndex, String simpleTypeName, int methodArgumentCount) {
		return String.join("_", methodName, "_", "" + methodArgumentCount, "_",  "" + zeroBasedArgumentIndex, simpleTypeName);
	}

}
