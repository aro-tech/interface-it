/**
 * 
 */
package org.interfaceit.util;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Utility methods to manipulate class name
 * 
 * @author aro_tech
 *
 */
public class ClassNameUtils {
	/**
	 * 
	 * @param fullName
	 * @return set of classes to import (ex: java.util.Properties)
	 */
	public static Set<String> makeImports(String fullName) {
		Set<String> result = new TreeSet<String>();
		String[] splitForParameterizedType = fullName.split("<");
		if (splitForParameterizedType[0].lastIndexOf('.') >= 0) {
			String extracted = splitForParameterizedType[0].trim();
			cleanUpAndAddImport(result, extracted);
		}

		for (int i = 1; i < splitForParameterizedType.length; i++) {
			int lastDotIx = splitForParameterizedType[i].lastIndexOf('.');
			if (lastDotIx >= 0) {
				String[] commaSeparated = splitForParameterizedType[i].split(",");
				for (String cs : commaSeparated) {
					String[] spaceSplit = cs.split(" ");
					addOneImportIfPossible(result, spaceSplit);
				}
			}
		}

		return result;
	}

	/**
	 * @param result
	 * @param extracted
	 */
	private static void cleanUpAndAddImport(Set<String> result, String extracted) {
		StringBuilder buf = new StringBuilder();
		char[] chars = extracted.toCharArray();
		for (char c : chars) {
			if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '.') {
				buf.append(c);
			} else if (c == '$') {
				buf.append('.');
			}
		}
		if (buf.length() > 0) {
			result.add(buf.toString());
		}
	}

	private static void addOneImportIfPossible(Set<String> result, String[] spaceSplit) {
		for (String cur : spaceSplit) {
			if (containsPackageInfo(cur)) {
				cleanUpAndAddImport(result, cur);
			}
		}
	}

	private static boolean containsPackageInfo(String cur) {
		return cur.lastIndexOf('.') >= 0;
	}

	/**
	 * utility call to extract the simple class name
	 * 
	 * @param fullName
	 * @return Name without package
	 */
	public static String extractSimpleName(String fullName) {
		return doNameExtraction(fullName).replace('$', '.');
	}

	private static String doNameExtraction(String fullName) {
		int ltIndex = fullName.indexOf('<');
		if (ltIndex < 0) {
			int lastDotIx = fullName.lastIndexOf('.');
			if (lastDotIx >= 0) {
				return fullName.substring(lastDotIx + 1);
			}
			return fullName;
		}
		return Arrays.stream(fullName.split("<")).map(s -> extractSimpleNamesInGenerics(s))
				.collect(Collectors.joining("<"));
	}

	private static String extractSimpleNamesInGenerics(String genericsContentWithClosingCaret) {
		return Arrays.stream(genericsContentWithClosingCaret.split(" ")).map(s -> extractSimpleName(s))
				.collect(Collectors.joining(" "));
	}

	/**
	 * Convert array type name to varargs
	 * 
	 * @param shortTypeName
	 * @return converted type
	 */
	public static String convertToVarArgs(String shortTypeName) {
		int braceIndex = shortTypeName.indexOf('[');
		if (braceIndex > 0) {
			return shortTypeName.substring(0, braceIndex) + "...";
		}
		return shortTypeName;
	}

	/**
	 * Get the method's class name, shortened if there's no conflict with the target interface name
	 * @param declaringClass
	 * @param targetInterfaceName
	 * @return the name
	 */
	public static String getDelegateClassNameWithoutPackageIfNoConflict(Class<?> declaringClass, String targetInterfaceName) {
		String delegateClassName = declaringClass.getSimpleName();
		if (delegateClassName.equals(targetInterfaceName)) {
			delegateClassName = declaringClass.getCanonicalName();
		}
		return delegateClassName;
	}

}
