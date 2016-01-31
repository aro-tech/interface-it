/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.interfaceit.util.ClassNameUtils;

/**
 * Generates an interface with default methods that delegate to static methods
 * 
 * @author aro_tech
 *
 */
public class DelegateMethodGenerator {

	private static final int DEFAULT_INDENTATION_SPACES = 4;

	/**
	 * Get all static methods for a class
	 * 
	 * @param clazz
	 * @return
	 */
	protected List<Method> listStaticMethodsForClass(Class<?> clazz) {
		return Arrays.stream(clazz.getMethods())
				.filter(m -> Modifier.isStatic(m.getModifiers()))
				.collect(Collectors.toList());
	}

	/**
	 * Generate code for 1 static method delegation
	 * 
	 * @param method
	 * @param importsOut
	 *            The method potentially adds values to this set if imports are
	 *            required
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(Method method, Set<String> importsOut) {
		return this.makeDelegateMethod(method, importsOut,
				this.makeIndentationUnit(DEFAULT_INDENTATION_SPACES));
	}

	/**
	 * Generate code for 1 static method delegation
	 * 
	 * @param method
	 * @param importsOut
	 *            The method potentially adds values to this set if imports are
	 *            required
	 * @param indentationUnit
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(Method method, Set<String> importsOut,
			String indentationUnit) {
		StringBuilder buf = new StringBuilder();
		StringBuilder paramsForJavadocLink = new StringBuilder();
		for (Type cur : method.getParameterTypes()) {
			if (paramsForJavadocLink.length() > 0) {
				paramsForJavadocLink.append(',');
			}
			paramsForJavadocLink.append(cur.getTypeName());
		}
		buf.append(indentationUnit).append("/**\n").append(indentationUnit)
				.append(" * Delegate call to ")
				.append(method.toGenericString()).append("\n")
				.append(indentationUnit).append(" * ").append("{@link ")
				.append(method.getDeclaringClass().getTypeName()).append('#')
				.append(method.getName()).append("(")
				.append(paramsForJavadocLink.toString()).append(")}")
				.append("\n").append(indentationUnit).append(" */\n")
				.append(indentationUnit)
				.append(this.makeMethodSignature(method, importsOut))
				.append(" {\n").append(indentationUnit).append(indentationUnit)
				.append(this.makeDelegateCall(method))
				.append("\n").append(indentationUnit).append("}\n");

		return buf.toString();
	}

	/**
	 * Generate the non-static signature of the method
	 * 
	 * @param method
	 * @param importNamesOut
	 *            collects any java imports required for the arguments and/or
	 *            return type
	 * @return The signature
	 */
	protected String makeMethodSignature(Method method,
			Set<String> importNamesOut) {
		StringBuilder buf = new StringBuilder();
		buf.append("default ").append(makeGenericMarker(method));
		buf.append(extractShortNameAndUpdateImports(importNamesOut, method
				.getGenericReturnType().getTypeName()));
		buf.append(' ').append(method.getName()).append('(');
		appendMethodArgumentsInSignature(method, importNamesOut, buf);
		buf.append(')');
		addThrowsClauseToSignatureUpdatingImports(method, importNamesOut, buf);
		return buf.toString();
	}

	private void addThrowsClauseToSignatureUpdatingImports(Method method,
			Set<String> importNamesOut, StringBuilder buf) {
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		if (exceptionTypes.length > 0) {
			buf.append(" throws ").append(
					makeCommaDelimitedExceptionTypesListUpdatingImports(
							importNamesOut, exceptionTypes));

		}
	}

	private String makeCommaDelimitedExceptionTypesListUpdatingImports(
			Set<String> importNamesOut, Class<?>[] exceptionTypes) {
		return String.join(
				", ",
				Arrays.stream(exceptionTypes)
						.map(type -> extractShortNameAndUpdateImports(
								importNamesOut, type.getTypeName()))
						.collect(Collectors.toList()));
	}

	private String makeGenericMarker(Method method) {
		// Set<String> generics = new HashSet<String>();
		StringBuilder buf = new StringBuilder();
		String generic = method.toGenericString();
		String[] split = generic.split(" ");
		Optional<String> genericPrefix = Arrays.stream(split)
				.filter(s -> s.startsWith("<") && s.endsWith(">")).findFirst();
		if (genericPrefix.isPresent()) {
			buf.append(genericPrefix.get()).append(' ');
		}
		return buf.toString();
	}

	private void appendMethodArgumentsInSignature(Method method,
			Set<String> importNamesOut, StringBuilder buf) {
		Type[] types = method.getGenericParameterTypes();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			String fullTypeName = types[i].getTypeName();
			String shortTypeName = extractShortNameAndUpdateImports(
					importNamesOut, fullTypeName);
			if (isVarargsParameter(method, types, i)) {
				shortTypeName = ClassNameUtils.convertToVarArgs(shortTypeName);
			}
			buf.append(shortTypeName).append(' ')
					.append(method.getParameters()[i].getName());
		}
	}

	private boolean isVarargsParameter(Method method, Type[] types, int i) {
		return i == types.length - 1 && method.isVarArgs();
	}

	private static String extractShortNameAndUpdateImports(
			Set<String> importNamesOut, String fullTypeName) {
		String shortTypeName = ClassNameUtils.extractSimpleName(fullTypeName);
		if (shortTypeName.length() < fullTypeName.length()) {
			importNamesOut.add(ClassNameUtils.makeImport(fullTypeName));
		}
		return shortTypeName;
	}

	/**
	 * Generate the line of code calling the static method
	 * 
	 * @param method
	 * @return
	 */
	public String makeDelegateCall(Method method) {
		StringBuilder buf = new StringBuilder();
		if (!"void".equals(method.getReturnType().getTypeName())) {
			buf.append("return ");
		}
		buf.append(method.getDeclaringClass().getSimpleName()).append(".")
				.append(method.getName()).append("(");
		int parameterCount = method.getParameterCount();
		for (int i = 0; i < parameterCount; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			buf.append("arg").append(i);
		}
		buf.append(");");
		return buf.toString();
	}

	/**
	 * Retrieve the constants for a class
	 * 
	 * @param clazz
	 * @return List of all constants declared in clazz
	 */
	protected List<Field> listConstantsForClass(Class<?> clazz) {
		return Arrays
				.stream(clazz.getFields())
				.filter(f -> {
					int modifiers = f.getModifiers();
					return Modifier.isStatic(modifiers)
							&& Modifier.isPublic(modifiers);
				}).collect(Collectors.toList());
	}

	private void generateConstant(Field field, Class<?> fieldClass,
			Set<String> imports, StringBuilder buf, String indentationUnit) {
		String type = extractShortNameAndUpdateImports(imports, field.getType()
				.getTypeName());
		buf.append("\n").append(indentationUnit).append("/** ")
				.append("{@link ").append(fieldClass.getTypeName()).append('#')
				.append(field.getName()).append("} */\n");
		buf.append(indentationUnit).append("public static final ").append(type)
				.append(' ').append(field.getName()).append(" = ")
				.append(fieldClass.getSimpleName()).append('.')
				.append(field.getName()).append(";\n");
	}

	/**
	 * Generate Java code declaring and assigning constants which refer to each
	 * constant in the delegate class
	 * 
	 * @param clazz
	 * @param importsUpdated
	 *            As a side effect, the imports needed for these constants are
	 *            added to the set
	 * @return
	 */
	protected String generateConstantsForClassUpdatingImports(Class<?> clazz,
			Set<String> importsUpdated) {
		return this.generateConstantsForClassUpdatingImports(clazz,
				importsUpdated, DEFAULT_INDENTATION_SPACES);
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
	 * @return
	 */
	protected String generateConstantsForClassUpdatingImports(
			Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces) {
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		StringBuilder buf = new StringBuilder();
		this.listConstantsForClass(delegateClass)
				.stream()
				.forEach(
						field -> {
							generateConstant(field, delegateClass,
									importsUpdated, buf, indentationUnit);
						});

		importsUpdated.add(ClassNameUtils.makeImport(delegateClass
				.getTypeName()));
		return buf.toString();
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @return Generated code
	 */
	public String generateDelegateClassCode(String targetPackageName,
			String targetInterfaceName, Class<?> delegateClass) {
		return this.generateDelegateClassCode(targetPackageName,
				targetInterfaceName, delegateClass, DEFAULT_INDENTATION_SPACES);
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @return Generated code
	 */
	public String generateDelegateClassCode(String targetPackageName,
			String targetInterfaceName, Class<?> delegateClass,
			int indentationSpaces) {
		StringBuilder buf = new StringBuilder();
		appendPackage(targetPackageName, buf);

		Set<String> importsUpdated = new HashSet<String>();
		String constants = this.generateConstantsForClassUpdatingImports(
				delegateClass, importsUpdated, indentationSpaces);
		String methods = this.generateMethodsForClassUpdatingImports(
				delegateClass, importsUpdated, indentationSpaces);

		appendSortedImports(buf, importsUpdated);

		String indentationUnit = makeIndentationUnit(indentationSpaces);

		buf.append("\n/** \n * Generated wrapper of static elements in ")
				.append(delegateClass.getTypeName()).append("\n * {@link ")
				.append(delegateClass.getTypeName()).append("}")
				.append("\n */\npublic static interface ")
				.append(targetInterfaceName).append(" {\n").append("\n\n")
				.append(indentationUnit).append("// CONSTANTS: \n")
				.append(constants).append("\n\n").append(indentationUnit)
				.append("// DELEGATE METHODS: \n").append(methods)
				.append("\n}");
		return buf.toString();
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
			buf.append("package ").append(targetPackageName).append(";\n\n");
		}
	}

	private void appendSortedImports(StringBuilder buf,
			Set<String> importsUpdated) {
		String[] sortedImports = importsUpdated
				.toArray(new String[importsUpdated.size()]);
		Arrays.sort(sortedImports);
		for (String cur : sortedImports) {
			buf.append("import ").append(cur).append("; \n");
		}
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(
			Class<?> delegateClass, Set<String> importsUpdated) {
		return this.generateMethodsForClassUpdatingImports(delegateClass,
				importsUpdated, DEFAULT_INDENTATION_SPACES);
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @param indentationSpaces
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(
			Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces) {
		StringBuilder buf = new StringBuilder();
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		for (Method cur : this.listStaticMethodsForClass(delegateClass)) {
			buf.append("\n")
					.append(this.makeDelegateMethod(cur, importsUpdated,
							indentationUnit)).append("\n\n");
		}
		return buf.toString();
	}

}
