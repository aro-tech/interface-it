/**
 * 
 */
package com.github.aro_tech.interface_it.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.aro_tech.interface_it.api.options.SimpleSingleFileOutputOptions;
import com.github.aro_tech.interface_it.format.CodeFormatter;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.policy.DeprecationPolicy;
import com.github.aro_tech.interface_it.ui.meta.error.UnableToCreateOutputDirectory;
import com.github.aro_tech.interface_it.util.ClassNameUtils;
import com.github.aro_tech.interface_it.util.FileSystem;
import com.github.aro_tech.interface_it.util.FileUtils;

/**
 * Generates an interface with default methods that delegate to static methods
 * 
 * @author aro_tech
 *
 */
public class CoreMixinGenerator implements MixinCodeGenerator {

	private final FileSystem fileSystem;
	private final DeprecationPolicy deprecationPolicy;
	private final CodeFormatter formatter;

	/**
	 * Predicate to filter out static methods
	 */
	final Predicate<? super Method> STATIC_METHODS_ONLY = m -> Modifier.isStatic(m.getModifiers());

	/**
	 * Constructor using default FileSystem
	 */
	public CoreMixinGenerator() {
		super();
		this.fileSystem = FileUtils.getDefaultFileSystem();
		this.deprecationPolicy = DeprecationPolicy.PROPAGATE_DEPRECATION;
		this.formatter = CodeFormatter.getDefault();
	}

	/**
	 * Constructor
	 * 
	 * @param fileSystem
	 * @param deprecationPolicy
	 * @param formatter
	 */
	public CoreMixinGenerator(FileSystem fileSystem, DeprecationPolicy deprecationPolicy, CodeFormatter formatter) {
		super();
		this.fileSystem = fileSystem;
		this.deprecationPolicy = deprecationPolicy;
		this.formatter = formatter;
	}

	/**
	 * Get all static methods for a class
	 * 
	 * @param clazz
	 * @return List of all static methods
	 */
	protected List<Method> listStaticMethodsForClass(Class<?> clazz) {
		return listFilteredSortedMethodsForClass(clazz, STATIC_METHODS_ONLY);
	}

	private List<Method> listFilteredSortedMethodsForClass(Class<?> clazz, Predicate<? super Method> methodFilter) {
		return Arrays.stream(clazz.getMethods()).filter(methodFilter).sorted(makeMethodSorter())
				.collect(Collectors.toList());
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
	 * @return the code for the method delegation
	 */
	public String makeDelegateMethod(String targetInterfaceName, Method method, Set<String> importsOut,
			ArgumentNameSource argumentNameSource) {
		StringBuilder buf = new StringBuilder();
		String genericStringForMethod = method.toGenericString();
		String declaringClassTypeName = method.getDeclaringClass().getTypeName();
		String methodName = method.getName();

		formatter
				.appendMethodComment(buf, methodName, genericStringForMethod, declaringClassTypeName,
						generateParamsForJavadocLink(method))
				.append(lineBreak(0))
				.append(this.makeMethodSignature(method, importsOut, argumentNameSource, targetInterfaceName))
				.append(" {").append(lineBreak(2))
				.append(this.makeDelegateCall(method, targetInterfaceName, importsOut, argumentNameSource))
				.append(lineBreak(1)).append("}").append(lineBreak(0));

		return buf.toString();
	}

	private String lineBreak(int indentations) {
		return formatter.newlineWithIndentations(indentations);
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
	 * @return The signature
	 * @deprecated A new version of makeMethodSignature requires the name of the
	 *             generated mixin interface.
	 */
	@Deprecated
	protected String makeMethodSignature(Method method, Set<String> importNamesOut,
			ArgumentNameSource argumentNameSource) {
		return this.makeMethodSignature(method, importNamesOut, argumentNameSource, "");
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
	 * @param
	 * @return The signature
	 */
	protected String makeMethodSignature(Method method, Set<String> importNamesOut,
			ArgumentNameSource argumentNameSource, String targetInterfaceName) {
		String indentationUnit = formatter.getIndentationUnit();
		StringBuilder buf = new StringBuilder();
		if (shouldDeprecate(method)) {
			buf.append(indentationUnit).append("@Deprecated").append(lineBreak(0));
		}
		buf.append(indentationUnit).append("default ")
				.append(makeGenericMarkerAndUpdateImports(method, importNamesOut));
		String extractedReturnTypeName = adjustTypeNameAndUpdateImportsIfAppropriate(importNamesOut,
				targetInterfaceName, method.getGenericReturnType().getTypeName());
		buf.append(extractedReturnTypeName);
		buf.append(' ').append(method.getName()).append('(');
		appendMethodArgumentsInSignature(method, importNamesOut, buf, argumentNameSource, targetInterfaceName);
		buf.append(')');
		addThrowsClauseToSignatureUpdatingImports(method, importNamesOut, buf);
		return buf.toString();
	}

	private String adjustTypeNameAndUpdateImportsIfAppropriate(Set<String> importNamesOut, String targetInterfaceName,
			String rawTypeName) {
		String extractedTypeName = rawTypeName;
		if (doNotNeedToQualifyTypeName(targetInterfaceName, rawTypeName)) {
			extractedTypeName = extractShortNameAndUpdateImports(importNamesOut, rawTypeName);
		}
		return extractedTypeName.replace('$', '.');
	}

	private boolean doNotNeedToQualifyTypeName(String targetInterfaceName, String rawTypeName) {
		return !hasTypeConflict(ClassNameUtils.extractSimpleName(rawTypeName), "" + targetInterfaceName);
	}

	private boolean hasTypeConflict(String simpleTypeName, String conflictingName) {
		if (simpleTypeName.startsWith(conflictingName)) {
			return isSameTypeOrNestedType(simpleTypeName, conflictingName);
		}
		return false;
	}

	private boolean isSameTypeOrNestedType(String simpleTypeName, String conflictingName) {
		return simpleTypeName.length() == conflictingName.length()
				|| simpleTypeName.charAt(conflictingName.length()) == '.';
	}

	private boolean shouldDeprecate(Method method) {
		return isDeprecated(method) && this.deprecationPolicy == DeprecationPolicy.PROPAGATE_DEPRECATION;
	}

	/**
	 * Check whether method is deprecated
	 * 
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
			appendOpeningOrDelimiter(buf);
			buf.append(cur.getName());
			Type[] bounds = cur.getBounds();
			if (hasSuperclassBound(bounds)) {
				appendAndImportExtendedType(importNamesOut, buf, bounds);
			}
		}
		appendClosingIfOpened(buf);
		return buf.toString();
	}

	private void appendClosingIfOpened(StringBuilder buf) {
		if (buf.length() > 0) {
			buf.append('>').append(' ');
		}
	}

	private void appendOpeningOrDelimiter(StringBuilder buf) {
		if (buf.length() > 0) {
			buf.append(",");
		} else {
			buf.append('<');
		}
	}

	private boolean hasSuperclassBound(Type[] bounds) {
		return bounds.length > 0 && Arrays.stream(bounds).noneMatch(t -> t.getTypeName().endsWith("java.lang.Object"));
	}

	private void appendAndImportExtendedType(Set<String> importNamesOut, StringBuilder buf, Type[] bounds) {
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

	private void appendMethodArgumentsInSignature(Method method, Set<String> importNamesOut, StringBuilder buf,
			ArgumentNameSource argumentNameSource, String targetInterfaceName) {
		Type[] types = method.getGenericParameterTypes();
		for (int i = 0; i < types.length; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			appendOneArgument(method, importNamesOut, buf, argumentNameSource, types, i, targetInterfaceName);
		}
	}

	private void appendOneArgument(Method method, Set<String> importNamesOut, StringBuilder buf,
			ArgumentNameSource argumentNameSource, Type[] types, int i, String targetInterfaceName) {
		String fullTypeName = types[i].getTypeName();
		buf.append(extractAndProcessShortTypeName(method, importNamesOut, types, i, fullTypeName, targetInterfaceName))
				.append(' ').append(argumentNameSource.getArgumentNameFor(method, i));
	}

	private String extractAndProcessShortTypeName(Method method, Set<String> importNamesOut, Type[] types, int i,
			String fullTypeName, String targetInterfaceName) {
		String shortTypeName = adjustTypeNameAndUpdateImportsIfAppropriate(importNamesOut, targetInterfaceName,
				fullTypeName);
		// extractShortNameAndUpdateImports(importNamesOut, fullTypeName);
		if (isVarargsParameter(method, types, i)) {
			shortTypeName = ClassNameUtils.convertToVarArgs(shortTypeName);
		}
		return shortTypeName;
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
		appendReturnIfNotVoid(method, buf);
		Class<?> declaringClass = method.getDeclaringClass();
		String delegateClassName = ClassNameUtils.getDelegateClassNameWithoutPackageIfNoConflict(declaringClass,
				targetInterfaceName);
		addImportIfNeeded(importsOut, declaringClass, delegateClassName);
		appendStaticCall(method, argumentNameSource, buf, delegateClassName);
		return buf.toString();
	}

	private void addImportIfNeeded(Set<String> importsOut, Class<?> declaringClass, String delegateClassName) {
		if (needToImport(delegateClassName)) {
			importsOut.add(declaringClass.getCanonicalName());
		}
	}

	private void appendStaticCall(Method method, ArgumentNameSource argumentNameSource, StringBuilder buf,
			String delegateClassName) {
		buf.append(delegateClassName).append(".").append(method.getName()).append("(");
		appendArgumentsInDelegateCall(method, argumentNameSource, buf);
		buf.append(");");
	}

	private void appendReturnIfNotVoid(Method method, StringBuilder buf) {
		if (!"void".equals(method.getReturnType().getTypeName())) {
			buf.append("return ");
		}
	}

	private void appendArgumentsInDelegateCall(Method method, ArgumentNameSource argumentNameSource,
			StringBuilder buf) {
		int parameterCount = method.getParameterCount();
		for (int i = 0; i < parameterCount; i++) {
			if (i > 0) {
				buf.append(", ");
			}
			buf.append(argumentNameSource.getArgumentNameFor(method, i));
		}
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
		buf.append(lineBreak(0));
		appendCommentForConstant(field, fieldClass, buf, indentationUnit);
		appendConstantDeclaration(field, fieldClass, buf, targetInterfaceName, indentationUnit, type);
	}

	private void appendConstantDeclaration(Field field, Class<?> fieldClass, StringBuilder buf,
			String targetInterfaceName, String indentationUnit, String type) {
		buf.append(indentationUnit).append("static final ").append(type).append(' ').append(field.getName())
				.append(" = ")
				.append(ClassNameUtils.getDelegateClassNameWithoutPackageIfNoConflict(fieldClass, targetInterfaceName))
				.append('.').append(field.getName()).append(";").append(lineBreak(0));
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
				.append(field.getName()).append("} */").append(lineBreak(0));
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
			String targetInterfaceName) {
		String indentationUnit = formatter.getIndentationUnit();

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
				new SimpleSingleFileOutputOptions(targetInterfaceName, targetPackageName, null));
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @param argumentNameSource
	 * @param parentInterfaces
	 * @return Generated code
	 */
	public String generateDelegateClassCode(String targetPackageName, String targetInterfaceName,
			Class<?> delegateClass, ArgumentNameSource argumentNameSource, MultiFileOutputOptions options) {
		Set<String> imports = new HashSet<String>();
		StringBuilder result = new StringBuilder();
		String constants = this.generateConstantsForClassUpdatingImports(delegateClass, imports, targetInterfaceName);
		String methods = this.generateMethodsForClassUpdatingImports(delegateClass, imports, argumentNameSource,
				options);
		appendPackage(targetPackageName, result);
		appendSortedImports(result, imports, targetInterfaceName);
		appendInterfaceBody(targetInterfaceName, delegateClass, result, constants, methods, options.getSuperTypes(delegateClass));
		return result.toString();
	}

	/**
	 * Generate the code of a java interface file delegating to the target
	 * class's static fields and methods
	 * 
	 * @param targetPackageName
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @param argumentNameSource
	 * @param options
	 * @return Generated code
	 */
	public String generateDelegateClassCode(Class<?> delegateClass, ArgumentNameSource argumentNameSource,
			MultiFileOutputOptions options) {
		final String targetInterfaceName = options.getTargetInterfaceNameForDelegate(delegateClass);
		Set<String> imports = new HashSet<String>();
		StringBuilder result = new StringBuilder();
		String constants = this.generateConstantsForClassUpdatingImports(delegateClass, imports, targetInterfaceName);
		String methods = this.generateMethodsForClassUpdatingImports(delegateClass, imports, argumentNameSource,
				options);
		appendPackage(options.getTargetPackageNameForDelegate(delegateClass), result);
		appendSortedImports(result, imports, targetInterfaceName);
		appendInterfaceBody(targetInterfaceName, delegateClass, result, constants, methods,
				options.getSuperTypes(delegateClass));
		return result.toString();
	}

	private void appendInterfaceBody(String targetInterfaceName, Class<?> delegateClass, StringBuilder buf,
			String constants, String methods, Set<String> superTypes) {
		formatter.appendClassComment(delegateClass, buf);
		appendInterfaceDeclaration(targetInterfaceName, buf, superTypes);
		buf.append(" {");
		for (int i = 0; i < 3; i++) {
			buf.append(lineBreak(0));
		}
		formatter.appendCommentBeforeConstants(buf).append(constants).append(lineBreak(0)).append(lineBreak(0));
		formatter.appendCommentBeforeMethods(buf).append(methods).append(lineBreak(0)).append("}");
	}

	private void appendInterfaceDeclaration(String targetInterfaceName, StringBuilder buf, Set<String> superTypes) {
		buf.append("public interface ").append(targetInterfaceName);
		appendAnySupertypeDeclarations(buf, superTypes);
	}

	private void appendAnySupertypeDeclarations(StringBuilder buf, Set<String> superTypes) {
		if (null != superTypes && superTypes.size() > 0) {
			buf.append(" extends ").append(makeCommaDelimitedSuperTypeList(superTypes));
		}
	}

	private String makeCommaDelimitedSuperTypeList(Set<String> superTypes) {
		StringJoiner joiner = new StringJoiner(", ");
		superTypes.stream().sorted().forEachOrdered(str -> joiner.add(str));
		final String superTypeList = joiner.toString();
		return superTypeList;
	}

	private void appendPackage(String targetPackageName, StringBuilder buf) {
		if (null != targetPackageName && !targetPackageName.isEmpty()) {
			buf.append("package ").append(targetPackageName).append(";").append(lineBreak(0)).append(lineBreak(0));
		}
	}

	private void appendSortedImports(StringBuilder buf, Set<String> importsUpdated, String targetInterfaceName) {
		String[] sortedImports = importsUpdated.toArray(new String[importsUpdated.size()]);
		Arrays.sort(sortedImports);
		for (String cur : sortedImports) {
			if (hasNoConflict(targetInterfaceName, cur) && doesNotCreatePMDWarning(cur)) {
				buf.append("import ").append(cur).append("; ").append(lineBreak(0));
			}
		}
	}

	private boolean doesNotCreatePMDWarning(String cur) {
		return !cur.startsWith("java.lang.");
	}

	private boolean hasNoConflict(String targetInterfaceName, String cur) {
		return !cur.endsWith("." + targetInterfaceName);
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @param argumentNameSource
	 * @param options
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			ArgumentNameSource argumentNameSource, MultiFileOutputOptions options) {
		return generateMethodsForClassUpdatingImports(delegateClass, importsUpdated,
				options.getTargetInterfaceNameForDelegate(delegateClass), argumentNameSource,
				options.getMethodFilterForDelegate(delegateClass));
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
			String targetInterfaceName, ArgumentNameSource argumentNameSource) {
		return generateMethodsForClassUpdatingImports(delegateClass, importsUpdated, targetInterfaceName,
				argumentNameSource, m -> true);
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
			String targetInterfaceName, ArgumentNameSource argumentNameSource,
			final Predicate<? super Method> extraMethodFilter) {
		StringBuilder buf = new StringBuilder();

		final Predicate<? super Method> methodFilter = STATIC_METHODS_ONLY
				.and(m -> deprecationPolicyDoesNotForbid((Method) m)).and(m2 -> extraMethodFilter.test((Method) m2));
		for (Method cur : this.listFilteredSortedMethodsForClass(delegateClass, methodFilter)) {
			buf.append(lineBreak(0))
					.append(this.makeDelegateMethod(targetInterfaceName, cur, importsUpdated, argumentNameSource))
					.append(lineBreak(0)).append(lineBreak(0));
		}
		return buf.toString();
	}

	/**
	 * Apply the deprecation policy to the method
	 * 
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
	 * @see
	 * com.github.aro_tech.interface_it.MixinCodeGenerator#generateClassToFile(
	 * java.io.File, java.lang.String, java.lang.Class, java.lang.String)
	 */
	@Override
	public File generateMixinJavaFile(File saveDirectory, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, ArgumentNameSource argumentNameSource) throws IOException {
		return writeClassFile(saveDirectory, this.generateDelegateClassCode(targetPackageName, targetInterfaceName,
				delegateClass, argumentNameSource), interfaceNameToFileName(targetInterfaceName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.aro_tech.interface_it.api.MixinCodeGenerator#
	 * generateMixinJavaFiles(com.github.aro_tech.interface_it.api.
	 * MultiFileOutputOptions,
	 * com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource,
	 * java.lang.Class[])
	 */
	@Override
	public List<File> generateMixinJavaFiles(MultiFileOutputOptions options, ArgumentNameSource argumentNameSource,
			Class<?>... delegateClasses) throws IOException {
		List<File> results = new ArrayList<File>();
		for (Class<?> delegate : delegateClasses) {
			String targetInterfaceName = options.getTargetInterfaceNameForDelegate(delegate);
			File current = writeClassFile(options.getMixinSaveDirectoryForDelegate(delegate),
					this.generateDelegateClassCode(options.getTargetPackageNameForDelegate(delegate),
							targetInterfaceName, delegate, argumentNameSource, options),
					interfaceNameToFileName(targetInterfaceName));
			results.add(current);
		}
		return results;
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
