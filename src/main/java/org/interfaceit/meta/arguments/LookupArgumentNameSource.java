/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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

	/* (non-Javadoc)
	 * @see org.interfaceit.meta.arguments.ArgumentNameSource#getArgumentNameFor(java.lang.reflect.Method, int)
	 */
	@Override
	public String getArgumentNameFor(Method method, int zeroBasedArgumentIndex) {
		String name = lookup.get(makeKey(method.getName(), zeroBasedArgumentIndex, getSimpleTypeFromMethod(method, zeroBasedArgumentIndex)));
		if(null == name) {
			name = ArgumentNameSource.super.getArgumentNameFor(method, zeroBasedArgumentIndex);
		}
		return name;
	}

	private String getSimpleTypeFromMethod(Method method, int zeroBasedArgumentIndex) {
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if(genericParameterTypes.length <= zeroBasedArgumentIndex) {
			return null;
		}
		String typeName = genericParameterTypes[zeroBasedArgumentIndex].getTypeName(); 
		int lastDotIx = typeName.lastIndexOf('.');
		if(lastDotIx >= 0) {
			typeName = typeName.substring(lastDotIx+ 1);
		}
		
		return typeName;
	}

	/**
	 * Add an argument name to be looked up
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
