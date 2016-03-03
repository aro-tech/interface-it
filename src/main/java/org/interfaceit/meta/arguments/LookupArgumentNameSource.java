/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.interfaceit.util.ClassNameUtils;

/**
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
				ClassNameUtils.getNormalizedArgTypeForLookupKey(method, zeroBasedArgumentIndex)));
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
	 */
	public void add(String methodName, int zeroBasedArgumentIndex, String simpleTypeName, String argumentName) {
		lookup.put(makeKey(methodName, zeroBasedArgumentIndex, simpleTypeName), argumentName);
	}

	private String makeKey(String methodName, int zeroBasedArgumentIndex, String simpleTypeName) {
		return String.join("_", methodName, "" + zeroBasedArgumentIndex, simpleTypeName);
	}

}
