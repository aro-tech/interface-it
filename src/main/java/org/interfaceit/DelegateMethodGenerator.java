/**
 * 
 */
package org.interfaceit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.ui.meta.error.UnableToCreateOutputDirectory;
import org.interfaceit.util.ClassNameUtils;
import org.interfaceit.util.FileSystem;
import org.interfaceit.util.FileUtils;

/**
 * Generates an interface with default methods that delegate to static methods
 * 
 * @author aro_tech
 *
 */
public class DelegateMethodGenerator implements ClassCodeGenerator {

	private static final String NEWLINE = System.lineSeparator();

	private final FileSystem fileSystem;
	private final DeprecationPolicy deprecationPolicy;

	/**
	 * Constructor using default FileSystem
	 */
	public DelegateMethodGenerator() {
		super();
		this.fileSystem = new FileUtils();
		this.deprecationPolicy = DeprecationPolicy.PROPAGATE_DEPRECATION;
	}

	/**
	 * Constructor
	 * 
	 * @param fileSystem
	 * @param deprecationPolicy
	 */
	public DelegateMethodGenerator(FileSystem fileSystem, DeprecationPolicy deprecationPolicy) {
		super();
		this.fileSystem = fileSystem;
		this.deprecationPolicy = deprecationPolicy;
	}

	/**
	 * Get all static methods for a class
	 * 
	 * @param clazz
	 * @return List of all static methods
	 */
	protected List<Method> listStaticMethodsForClass(Class<?> clazz) {
		return Arrays.stream(clazz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers()))
				.sorted(makeMethodSorter()).collect(Collectors.toList());
	}

	private Comparator<? super Method> makeMethodSorter() {
		return (m1, m2) -> {
			int val = m1.getName().compareTo(m2.getName());
			int parameterCountM1 = m1.getParameterCount();
			if (val == 0) {
				val = Integer.compare(parameterCountM1, m2.getParameterCount());
			}
			if (val == 0) {
				val = compareSimpleTypeNamesOfParameters(m1, m2, val, parameterCountM1);
			}
			if (val == 0) {
				val = m1.toGenericString().compareTo(m2.toGenericString());
			}
			return val;
		};
	}

	private int compareSimpleTypeNamesOfParameters(Method m1, Method m2, int val, int parameterCountM1) {
		for (int i = 0; val == 0 && i < parameterCountM1; i++) {
			val = ClassNameUtils.extractSimpleName(m1.getParameters()[i].getParameterizedType().getTypeName())
					.toLowerCase().compareTo(
							ClassNameUtils.extractSimpleName(m2.getParameters()[i].getParameterizedType().getTypeName())
									.toLowerCase());
		}
		return val;
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
	 *            Provider of argument name information lost during compilation
	 * @param indentationUnit
	 *            spaces to be inserted n times at the beginning of lines based
	 *            on block depth
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(String targetInterfaceName, Method method, Set<String> importsOut,
			ArgumentNameSource argumentNameSource, String indentationUnit) {
		StringBuilder buf = new StringBuilder();
		appendMethodComment(method, indentationUnit, buf).append(NEWLINE)
				.append(this.makeMethodSignature(method, importsOut, argumentNameSource, indentationUnit)).append(" {")
				.append(NEWLINE).append(indentationUnit).append(indentationUnit)
				.append(this.makeDelegateCall(method, targetInterfaceName, importsOut, argumentNameSource))
				.append(NEWLINE).append(indentationUnit).append("}").append(NEWLINE);

		return buf.toString();
	}

	/**
	 * Add a javadoc comment for the generated method
	 * 
	 * @param method
	 *            The delegate method
	 * @param indentationUnit
	 *            Blank spaces for indentation
	 * @param buf
	 *            The StringBuilder for appending
	 * @return buf
	 */
	protected StringBuilder appendMethodComment(Method method, String indentationUnit, StringBuilder buf) {
		return buf.append(indentationUnit).append("/**").append(NEWLINE).append(indentationUnit)
				.append(" * Delegate call to ").append(method.toGenericString()).append(NEWLINE).append(indentationUnit)
				.append(" * ").append("{@link ").append(method.getDeclaringClass().getTypeName()).append('#')
				.append(method.getName()).append("(").append(generateParamsForJavadocLink(method)).append(")}")
				.append(NEWLINE).append(indentationUnit).append(" */");
	}

	private String generateParamsForJavadocLink(Method method) {
		StringBuilder paramsForJavadocLink = new StringBuilder();
		for (Type cur : method.getParameterTypes()) {
			if (paramsForJavadocLink.length() > 0) {
				paramsForJavadocLink.append(',');
			}
			paramsForJavadocLink.append(cur.getTypeName());
		}
		return paramsForJavadocLink.toString();
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
	 * @param indentationUnit
	 *            spaces to be inserted n times at the beginning of lines based
	 *            on block depth
	 * @return The signature
	 */
	protected String makeMethodSignature(Method method, Set<String> importNamesOut,
			ArgumentNameSource argumentNameSource, String indentationUnit) {
		StringBuilder buf = new StringBuilder();
		if (shouldDeprecate(method)) {
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

	private boolean shouldDeprecate(Method method) {
		return isDeprecated(method) && this.deprecationPolicy == DeprecationPolicy.PROPAGATE_DEPRECATION;
	}

	/**
	 * Check whether method is deprecated
	 * @param method
	 * @return true if deprecated, false if not
	 */
	protected boolean isDeprecated(Method method) {
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
	 * @return The line of code which calls the delegate class's static method
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
		}).sorted(Comparator.comparing(Field::getName)).collect(Collectors.toList());
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
		String type = extractShortNameAndUpdateImports(imports, getTypeNameFromFieldForConstant(field));
		buf.append(NEWLINE);
		appendCommentForConstant(field, fieldClass, buf, indentationUnit);
		appendConstantDeclaration(field, fieldClass, buf, targetInterfaceName, indentationUnit, type);
	}

	private void appendConstantDeclaration(Field field, Class<?> fieldClass, StringBuilder buf,
			String targetInterfaceName, String indentationUnit, String type) {
		buf.append(indentationUnit).append("public static final ").append(type).append(' ').append(field.getName())
				.append(" = ")
				.append(ClassNameUtils.getDelegateClassNameWithoutPackageIfNoConflict(fieldClass, targetInterfaceName))
				.append('.').append(field.getName()).append(";").append(NEWLINE);
	}

	/**
	 * Append a javadoc comment for a generated constant
	 * 
	 * @param field
	 * @param fieldClass
	 * @param buf
	 * @param indentationUnit
	 */
	protected void appendCommentForConstant(Field field, Class<?> fieldClass, StringBuilder buf,
			String indentationUnit) {
		buf.append(indentationUnit).append("/** ").append("{@link ").append(fieldClass.getTypeName()).append('#')
				.append(field.getName()).append("} */").append(NEWLINE);
	}

	private String getTypeNameFromFieldForConstant(Field field) {
		String typeName = field.getType().getTypeName();
		TypeVariable<?>[] typeParameters = field.getType().getTypeParameters();
		if (null != typeParameters && typeParameters.length > 0) {
			typeName += Arrays.asList(typeParameters).stream().map(t -> "Object")
					.collect(Collectors.joining(",", "<", ">"));
		}
		return typeName;
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
	 * @return The Java code declaring and initializing all constants for the
	 *         wrapper interface
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
		appendClassComment(delegateClass, buf);
		buf.append("public interface ").append(targetInterfaceName);
		buf.append(" {").append(NEWLINE).append(NEWLINE).append(NEWLINE);
		appendCommentBeforeConstants(buf, indentationUnit).append(constants).append(NEWLINE).append(NEWLINE);
		appendCommentBeforeMethods(buf, indentationUnit).append(methods).append(NEWLINE).append("}");
	}

	/**
	 * Append a comment to start the zone of methods
	 * 
	 * @param buf
	 *            target for append
	 * @param indentationUnit
	 *            Blank spaces for indentation
	 * @return buf
	 */
	protected StringBuilder appendCommentBeforeMethods(StringBuilder buf, String indentationUnit) {
		return buf.append(indentationUnit).append("// DELEGATE METHODS: ").append(NEWLINE);
	}

	/**
	 * Append a comment to start the zone of constants
	 * 
	 * @param buf
	 *            target for append
	 * @param indentationUnit
	 *            Blank spaces for indentation
	 * @return buf
	 */
	protected StringBuilder appendCommentBeforeConstants(StringBuilder buf, String indentationUnit) {
		return buf.append(indentationUnit).append("// CONSTANTS: ").append(NEWLINE);
	}

	/**
	 * Append a javadoc comment for the class
	 * 
	 * @param delegateClass
	 * @param buf
	 *            target for append
	 * @return buf
	 */
	protected StringBuilder appendClassComment(Class<?> delegateClass, StringBuilder buf) {
		return buf.append(NEWLINE).append("/** ").append(NEWLINE).append(" * Wrapper of static elements in ")
				.append(delegateClass.getTypeName()).append(NEWLINE)
				.append(" * Generated by Interface-It: https://github.com/aro-tech/interface-it").append(NEWLINE)
				.append(" * {@link ").append(delegateClass.getTypeName()).append("}").append(NEWLINE).append(" */")
				.append(NEWLINE);
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
			if (deprecationPolicyDoesNotForbid(cur)) {
				buf.append(NEWLINE).append(this.makeDelegateMethod(targetInterfaceName, cur, importsUpdated,
						argumentNameSource, indentationUnit)).append(NEWLINE).append(NEWLINE);
			}
		}
		return buf.toString();
	}

	/**
	 * Apply the deprecation policy to the method
	 * @param method
	 * @return true if the handling of the method is not blocked by the
	 *         deprecation policy, false if blocked
	 */
	protected boolean deprecationPolicyDoesNotForbid(Method method) {
		return !isDeprecated(method) || this.deprecationPolicy != DeprecationPolicy.IGNORE_DEPRECATED_METHODS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.interfaceit.ClassCodeGenerator#generateClassToFile(java.io.File,
	 * java.lang.String, java.lang.Class, java.lang.String, int)
	 */
	@Override
	public File generateClassToFile(File saveDirectory, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, ArgumentNameSource argumentNameSource, int indentationSpaces) throws IOException {
		return writeClassFile(saveDirectory, this.generateDelegateClassCode(targetPackageName, targetInterfaceName,
				delegateClass, argumentNameSource, indentationSpaces), interfaceNameToFileName(targetInterfaceName));
	}

	private String interfaceNameToFileName(String targetInterfaceName) {
		return targetInterfaceName + ".java";
	}

	private File writeClassFile(File dir, String content, String fileName)
			throws IOException, UnableToCreateOutputDirectory {
		fileSystem.makeOutputDirectoryIfAbsent(dir);
		return fileSystem.writeFile(dir, fileName, content);
	}
}
