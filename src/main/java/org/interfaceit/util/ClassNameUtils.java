/**
 * 
 */
package org.interfaceit.util;

import java.util.Arrays;
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
	 * @return package import (ex: java.lang.*)
	 */
	public static String makeImport(String fullName) {
		String[] split = fullName.split("<");
		int lastDotIx = split[0].lastIndexOf('.');
		if (lastDotIx >= 0) {
			StringBuilder buf = new StringBuilder();
			buf.append(fullName.substring(0, lastDotIx + 1)).append('*');
			return buf.toString();
		}
		return fullName;
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

}
