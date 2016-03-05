/**
 * 
 */
package org.interfaceit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.util.ClassNameUtils;

/**
 * Generates an interface with default methods that delegate to static methods
 * 
 * @author aro_tech
 *
 */
public class DelegateMethodGenerator implements ClassCodeGenerator {

	private static final int DEFAULT_INDENTATION_SPACES = 4;
	private static final String NEWLINE = System.lineSeparator();

	/**
	 * Get all static methods for a class
	 * 
	 * @param clazz
	 * @return
	 */
	protected List<Method> listStaticMethodsForClass(Class<?> clazz) {
		return Arrays.stream(clazz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers())).sorted((m1, m2) -> {
			int val = m1.getName().compareTo(m2.getName());
			int parameterCountM1 = m1.getParameterCount();
			if (val == 0) {
				val = Integer.compare(parameterCountM1, m2.getParameterCount());
			}
			if (val == 0) {
				for (int i = 0; val == 0 && i < parameterCountM1; i++) {
					val = ClassNameUtils.extractSimpleName(m1.getParameters()[i].getParameterizedType().getTypeName())
							.toLowerCase()
							.compareTo(ClassNameUtils
									.extractSimpleName(m2.getParameters()[i].getParameterizedType().getTypeName())
									.toLowerCase());
				}
			}
			if (val == 0) {
				val = m1.toGenericString().compareTo(m2.toGenericString());
			}
			return val;
		}).collect(Collectors.toList());
	}

	/**
	 * Generate code for 1 static method delegation
	 * 
	 * @param method
	 * @param targetInterfaceName
	 *            The name of the wrapper interface, to avoid class name
	 *            conflicts
	 * @param importsOut
	 *            The method potentially adds values to this set if imports are
	 *            required
	 * @param argumentNameSource,
	 * 
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(Method method, String targetInterfaceName, Set<String> importsOut,
			ArgumentNameSource argumentNameSource) {
		return this.makeDelegateMethod(targetInterfaceName, method, importsOut, argumentNameSource,
				this.makeIndentationUnit(DEFAULT_INDENTATION_SPACES));
	}

	/**
	 * Generate code for 1 static method delegation
	 * 
	 * @param targetInterfaceName
	 *            The name of the wrapper interface, to avoid class name
	 *            conflicts
	 * @param method
	 * @param importsOut
	 *            The method potentially adds values to this set if imports are
	 *            required
	 * @param argumentNameSource
	 * @param indentationUnit
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(String targetInterfaceName, Method method, Set<String> importsOut,
			ArgumentNameSource argumentNameSource, String indentationUnit) {
		StringBuilder buf = new StringBuilder();
		StringBuilder paramsForJavadocLink = new StringBuilder();
		for (Type cur : method.getParameterTypes()) {
			if (paramsForJavadocLink.length() > 0) {
				paramsForJavadocLink.append(',');
			}
			paramsForJavadocLink.append(cur.getTypeName());
		}
		buf.append(indentationUnit).append("/**").append(NEWLINE).append(indentationUnit).append(" * Delegate call to ")
				.append(method.toGenericString()).append(NEWLINE).append(indentationUnit).append(" * ")
				.append("{@link ").append(method.getDeclaringClass().getTypeName()).append('#').append(method.getName())
				.append("(").append(paramsForJavadocLink.toString()).append(")}").append(NEWLINE)
				.append(indentationUnit).append(" */").append(NEWLINE)
				.append(this.makeMethodSignature(method, importsOut, argumentNameSource, indentationUnit)).append(" {")
				.append(NEWLINE).append(indentationUnit).append(indentationUnit)
				.append(this.makeDelegateCall(method, targetInterfaceName, importsOut, argumentNameSource))
				.append(NEWLINE).append(indentationUnit).append("}").append(NEWLINE);

		return buf.toString();
	}

	/**
	 * Generate the non-static signature of the method
	 * 
	 * @param method
	 * @param importNamesOut
	 *            collects any java imports required for the arguments and/or
	 *            return type
	 * @param argumentNameSource
	 *            For naming arguments
	 * @param indentationSpaces
	 * @return The signature
	 */
	protected String makeMethodSignature(Method method, Set<String> importNamesOut,
			ArgumentNameSource argumentNameSource, String indentationUnit) {
		StringBuilder buf = new StringBuilder();
		if (isDeprecated(method)) {
			buf.append(indentationUnit).append("@Deprecated").append(NEWLINE);
		}
		buf.append(indentationUnit).append("default ")
				.append(makeGenericMarkerAndUpdateImports(method, importNamesOut));
		buf.append(extractShortNameAndUpdateImports(importNamesOut, method.getGenericReturnType().getTypeName()));
		buf.append(' ').append(method.getName()).append('(');
		appendMethodArgumentsInSignature(method, importNamesOut, buf, argumentNameSource);
		buf.append(')');
		addThrowsClauseToSignatureUpdatingImports(method, importNamesOut, buf);
		return buf.toString();
	}

	private boolean isDeprecated(Method method) {
		return method.getDeclaredAnnotationsByType(Deprecated.class).length > 0;
	}

	private void addThrowsClauseToSignatureUpdatingImports(Method method, Set<String> importNamesOut,
			StringBuilder buf) {
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		if (exceptionTypes.length > 0) {
			buf.append(" throws ")
					.append(makeCommaDelimitedExceptionTypesListUpdatingImports(importNamesOut, exceptionTypes));

		}
	}

	private String makeCommaDelimitedExceptionTypesListUpdatingImports(Set<String> importNamesOut,
			Class<?>[] exceptionTypes) {
		return String.join(", ",
				Arrays.stream(exceptionTypes)
						.map(type -> extractShortNameAndUpdateImports(importNamesOut, type.getTypeName()))
						.collect(Collectors.toList()));
	}

	private String makeGenericMarkerAndUpdateImports(Method method, Set<String> importNamesOut) {
		StringBuilder buf = new StringBuilder();
		TypeVariable<Method>[] typeParameters = method.getTypeParameters();
		for (TypeVariable<Method> cur : typeParameters) {
			if (buf.length() > 0) {
				buf.append(",");
			} else {
				buf.append('<');
			}
			buf.append(cur.getName());
			Type[] bounds = cur.getBounds();
			if (bounds.length > 0
					&& Arrays.stream(bounds).noneMatch(t -> t.getTypeName().endsWith("java.lang.Object"))) {
				buf.append(" extends ");
				for (int i = 0; i < bounds.length; i++) {
					if (i > 0) {
						buf.append(",");
					}
					String typeName = bounds[i].getTypeName();
					buf.append(ClassNameUtils.extractSimpleName(typeName));
					importNamesOut.addAll(ClassNameUtils.makeImports(typeName));
				}
			}

		}
		if (buf.length() > 0) {
			buf.append('>').append(' ');
		}

		return buf.toString();
	}

	private void appendMethodArgumentsInSignature(Method method, Set<String> importNamesOut, StringBuilder buf,
			ArgumentNameSource argumentNameSource) {
		Type[] types = method.getGenericParameterTypes();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			String fullTypeName = types[i].getTypeName();
			String shortTypeName = extractShortNameAndUpdateImports(importNamesOut, fullTypeName);
			if (isVarargsParameter(method, types, i)) {
				shortTypeName = ClassNameUtils.convertToVarArgs(shortTypeName);
			}
			buf.append(shortTypeName).append(' ').append(argumentNameSource.getArgumentNameFor(method, i));
		}
	}

	private boolean isVarargsParameter(Method method, Type[] types, int i) {
		return i == types.length - 1 && method.isVarArgs();
	}

	private static String extractShortNameAndUpdateImports(Set<String> importNamesOut, String fullTypeName) {
		String shortTypeName = ClassNameUtils.extractSimpleName(fullTypeName);
		if (shortTypeName.length() < fullTypeName.length()) {
			importNamesOut.addAll(ClassNameUtils.makeImports(fullTypeName));
		}
		return shortTypeName;
	}

	/**
	 * Generate the line of code calling the static method
	 * 
	 * @param method
	 * @param targetInterfaceName
	 *            The name of the wrapper interface, to avoid class name
	 *            conflicts
	 * @param importsOut
	 *            For receiving imports needed
	 * @param argumentNameSource
	 * @return
	 */
	public String makeDelegateCall(Method method, String targetInterfaceName, Set<String> importsOut,
			ArgumentNameSource argumentNameSource) {
		StringBuilder buf = new StringBuilder();
		if (!"void".equals(method.getReturnType().getTypeName())) {
			buf.append("return ");
		}
		Class<?> declaringClass = method.getDeclaringClass();
		String delegateClassName = ClassNameUtils.getDelegateClassNameWithoutPackageIfNoConflict(declaringClass,
				targetInterfaceName);
		if (needToImport(delegateClassName)) {
			importsOut.add(declaringClass.getCanonicalName());
		}
		buf.append(delegateClassName).append(".").append(method.getName()).append("(");
		int parameterCount = method.getParameterCount();
		for (int i = 0; i < parameterCount; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			buf.append(argumentNameSource.getArgumentNameFor(method, i));
			// buf.append("arg").append(i);
		}
		buf.append(");");
		return buf.toString();
	}

	/**
	 * @param delegateClassName
	 * @return
	 */
	private boolean needToImport(String delegateClassName) {
		return delegateClassName.indexOf('.') < 0;
	}

	/**
	 * Retrieve the constants for a class
	 * 
	 * @param clazz
	 * @return List of all constants declared in clazz
	 */
	protected List<Field> listConstantsForClass(Class<?> clazz) {
		return Arrays.stream(clazz.getFields()).filter(f -> {
			int modifiers = f.getModifiers();
			return Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
		}).sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).collect(Collectors.toList());
	}

	/**
	 * Generate code for one constant
	 * 
	 * @param field
	 * @param fieldClass
	 * @param imports
	 * @param buf
	 * @param targetInterfaceName
	 * @param indentationUnit
	 */
	protected void generateConstant(Field field, Class<?> fieldClass, Set<String> imports, StringBuilder buf,
			String targetInterfaceName, String indentationUnit) {
		String type = extractShortNameAndUpdateImports(imports, field.getType().getTypeName());
		buf.append(NEWLINE).append(indentationUnit).append("/** ").append("{@link ").append(fieldClass.getTypeName())
				.append('#').append(field.getName()).append("} */").append(NEWLINE);
		buf.append(indentationUnit).append("public static final ").append(type).append(' ').append(field.getName())
				.append(" = ")
				.append(ClassNameUtils.getDelegateClassNameWithoutPackageIfNoConflict(fieldClass, targetInterfaceName))
				.append('.').append(field.getName()).append(";").append(NEWLINE);
	}

	/**
	 * Generate Java code declaring and assigning constants which refer to each
	 * constant in the delegate class
	 * 
	 * @param clazz
	 * @param importsUpdated
	 *            As a side effect, the imports needed for these constants are
	 *            added to the set
	 * @param targetInterfaceName
	 *            TODO
	 * @return
	 */
	protected String generateConstantsForClassUpdatingImports(Class<?> clazz, Set<String> importsUpdated,
			String targetInterfaceName) {
		return this.generateConstantsForClassUpdatingImports(clazz, importsUpdated, DEFAULT_INDENTATION_SPACES,
				targetInterfaceName);
	}

	/**
	 * Generate Java code declaring and assigning constants which refer to each
	 * constant in the delegate class
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 *            As a side effect, the imports needed for these constants are
	 *            added to the set
	 * @param indentationSpaces
	 * @param targetInterfaceName
	 *            TODO
	 * @return
	 */
	protected String generateConstantsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces, String targetInterfaceName) {
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		StringBuilder buf = new StringBuilder();
		this.listConstantsForClass(delegateClass).stream().forEach(field -> {
			generateConstant(field, delegateClass, importsUpdated, buf, targetInterfaceName, indentationUnit);
		});

		importsUpdated.addAll(ClassNameUtils.makeImports(delegateClass.getTypeName()));
		return buf.toString();
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @param argumentNameSource
	 * @return Generated code
	 */
	public String generateDelegateClassCode(String targetPackageName, String targetInterfaceName,
			Class<?> delegateClass, ArgumentNameSource argumentNameSource) {
		return this.generateDelegateClassCode(targetPackageName, targetInterfaceName, delegateClass, argumentNameSource,
				DEFAULT_INDENTATION_SPACES);
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @param argumentNameSource
	 * @param indentationSpaces
	 * @return Generated code
	 */
	public String generateDelegateClassCode(String targetPackageName, String targetInterfaceName,
			Class<?> delegateClass, ArgumentNameSource argumentNameSource, int indentationSpaces) {
		Set<String> imports = new HashSet<String>();
		StringBuilder result = new StringBuilder();
		String constants = this.generateConstantsForClassUpdatingImports(delegateClass, imports, indentationSpaces,
				targetInterfaceName);
		String methods = this.generateMethodsForClassUpdatingImports(delegateClass, imports, indentationSpaces,
				targetInterfaceName, argumentNameSource);
		appendPackage(targetPackageName, result);
		appendSortedImports(result, imports, targetInterfaceName);
		appendInterfaceBody(targetInterfaceName, delegateClass, indentationSpaces, result, constants, methods);
		return result.toString();
	}

	private void appendInterfaceBody(String targetInterfaceName, Class<?> delegateClass, int indentationSpaces,
			StringBuilder buf, String constants, String methods) {
		String indentationUnit = makeIndentationUnit(indentationSpaces);
		buf.append(NEWLINE).append("/** ").append(NEWLINE).append(" * Wrapper of static elements in ")
				.append(delegateClass.getTypeName()).append(NEWLINE)
				.append(" * Generated by Interface-It: https://github.com/aro-tech/interface-it").append(NEWLINE)
				.append(" * {@link ").append(delegateClass.getTypeName()).append("}").append(NEWLINE).append(" */")
				.append(NEWLINE).append("public interface ").append(targetInterfaceName).append(" {").append(NEWLINE)
				.append(NEWLINE).append(NEWLINE).append(indentationUnit).append("// CONSTANTS: ").append(NEWLINE)
				.append(constants).append(NEWLINE).append(NEWLINE).append(indentationUnit)
				.append("// DELEGATE METHODS: ").append(NEWLINE).append(methods).append(NEWLINE).append("}");
	}

	private String makeIndentationUnit(int indentationSpaces) {
		StringBuilder indentation = new StringBuilder();
		for (int i = 0; i < indentationSpaces; i++) {
			indentation.append(' ');
		}
		String indentationUnit = indentation.toString();
		return indentationUnit;
	}

	private void appendPackage(String targetPackageName, StringBuilder buf) {
		if (null != targetPackageName && !targetPackageName.isEmpty()) {
			buf.append("package ").append(targetPackageName).append(";").append(NEWLINE).append(NEWLINE);
		}
	}

	private void appendSortedImports(StringBuilder buf, Set<String> importsUpdated, String targetInterfaceName) {
		String[] sortedImports = importsUpdated.toArray(new String[importsUpdated.size()]);
		Arrays.sort(sortedImports);
		for (String cur : sortedImports) {
			if (hasNoConflict(targetInterfaceName, cur)) {
				buf.append("import ").append(cur).append("; ").append(NEWLINE);
			}
		}
	}

	private boolean hasNoConflict(String targetInterfaceName, String cur) {
		return !cur.endsWith("." + targetInterfaceName);
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @param targetInterfaceName
	 * @param argumentNameSource
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			String targetInterfaceName, ArgumentNameSource argumentNameSource) {
		return this.generateMethodsForClassUpdatingImports(delegateClass, importsUpdated, DEFAULT_INDENTATION_SPACES,
				targetInterfaceName, argumentNameSource);
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @param indentationSpaces
	 * @param targetInterfaceName
	 * @param argumentNameSource
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces, String targetInterfaceName, ArgumentNameSource argumentNameSource) {
		StringBuilder buf = new StringBuilder();
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		for (Method cur : this.listStaticMethodsForClass(delegateClass)) {
			buf.append(NEWLINE).append(this.makeDelegateMethod(targetInterfaceName, cur, importsUpdated,
					argumentNameSource, indentationUnit)).append(NEWLINE).append(NEWLINE);
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.interfaceit.ClassCodeGenerator#generateClassToFile(java.io.File,
	 * java.lang.String, java.lang.Class, java.lang.String, int)
	 */
	@Override
	public File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, ArgumentNameSource argumentNameSource, int indentationSpaces) throws IOException {
		return writeFile(dir, this.generateDelegateClassCode(targetPackageName, targetInterfaceName, delegateClass,
				argumentNameSource, indentationSpaces), interfaceNameToFileName(targetInterfaceName));
	}

	private String interfaceNameToFileName(String targetInterfaceName) {
		return targetInterfaceName + ".java";
	}

	private File writeFile(File dir, String content, String fileName) throws IOException {
		File fileToWrite = new File(dir, fileName);
		try (BufferedWriter w = new BufferedWriter(new FileWriter(fileToWrite))) {
			w.write(content);
		}
		return fileToWrite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.interfaceit.ClassCodeGenerator#generateClassToFile(java.io.File,
	 * java.lang.String, java.lang.Class, java.lang.String)
	 */
	@Override
	public File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, ArgumentNameSource argumentNameSource) throws IOException {
		return writeFile(dir, this.generateDelegateClassCode(targetPackageName, targetInterfaceName, delegateClass,
				argumentNameSource), interfaceNameToFileName(targetInterfaceName));
	}

}
