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

import org.interfaceit.util.ClassNameUtils;

/**
 * Generates an interface with default methods that delegate to static methods
 * 
 * @author aro_tech
 *
 */
public class DelegateMethodGenerator {

	private static final int DEFAULT_INDENTATION_SPACES = 4;
	private static final String NEWLINE = System.lineSeparator();

	/**
	 * Get all static methods for a class
	 * 
	 * @param clazz
	 * @return
	 */
	protected List<Method> listStaticMethodsForClass(Class<?> clazz) {
		return Arrays.stream(clazz.getMethods()).filter(m -> Modifier.isStatic(m.getModifiers()))
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
		return this.makeDelegateMethod(method, importsOut, this.makeIndentationUnit(DEFAULT_INDENTATION_SPACES));
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
	public String makeDelegateMethod(Method method, Set<String> importsOut, String indentationUnit) {
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
				.append(indentationUnit).append(" */").append(NEWLINE).append(indentationUnit)
				.append(this.makeMethodSignature(method, importsOut)).append(" {").append(NEWLINE)
				.append(indentationUnit).append(indentationUnit).append(this.makeDelegateCall(method)).append(NEWLINE)
				.append(indentationUnit).append("}").append(NEWLINE);

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
	protected String makeMethodSignature(Method method, Set<String> importNamesOut) {
		StringBuilder buf = new StringBuilder();
		buf.append("default ").append(makeGenericMarkerAndUpdateImports(method, importNamesOut));
		buf.append(extractShortNameAndUpdateImports(importNamesOut, method.getGenericReturnType().getTypeName()));
		buf.append(' ').append(method.getName()).append('(');
		appendMethodArgumentsInSignature(method, importNamesOut, buf);
		buf.append(')');
		addThrowsClauseToSignatureUpdatingImports(method, importNamesOut, buf);
		return buf.toString();
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
		// Set<String> generics = new HashSet<String>();
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
					importNamesOut.add(ClassNameUtils.makeImport(typeName));
				}
			}

		}
		if (buf.length() > 0) {
			buf.append('>').append(' ');
		}
		// String generic = method.toGenericString();
		// String[] split = generic.split(" ");
		// Optional<String> genericPrefix = Arrays.stream(split).filter(s ->
		// s.startsWith("<") && s.endsWith(">"))
		// .findFirst();
		// if (genericPrefix.isPresent()) {
		// buf.append(genericPrefix.get()).append(' ');
		// }
		return buf.toString();
	}

	private void appendMethodArgumentsInSignature(Method method, Set<String> importNamesOut, StringBuilder buf) {
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
			buf.append(shortTypeName).append(' ').append(method.getParameters()[i].getName());
		}
	}

	private boolean isVarargsParameter(Method method, Type[] types, int i) {
		return i == types.length - 1 && method.isVarArgs();
	}

	private static String extractShortNameAndUpdateImports(Set<String> importNamesOut, String fullTypeName) {
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
		buf.append(method.getDeclaringClass().getSimpleName()).append(".").append(method.getName()).append("(");
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
		return Arrays.stream(clazz.getFields()).filter(f -> {
			int modifiers = f.getModifiers();
			return Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers);
		}).collect(Collectors.toList());
	}

	private void generateConstant(Field field, Class<?> fieldClass, Set<String> imports, StringBuilder buf,
			String indentationUnit) {
		String type = extractShortNameAndUpdateImports(imports, field.getType().getTypeName());
		buf.append(NEWLINE).append(indentationUnit).append("/** ").append("{@link ").append(fieldClass.getTypeName())
				.append('#').append(field.getName()).append("} */").append(NEWLINE);
		buf.append(indentationUnit).append("public static final ").append(type).append(' ').append(field.getName())
				.append(" = ").append(fieldClass.getSimpleName()).append('.').append(field.getName()).append(";")
				.append(NEWLINE);
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
	protected String generateConstantsForClassUpdatingImports(Class<?> clazz, Set<String> importsUpdated) {
		return this.generateConstantsForClassUpdatingImports(clazz, importsUpdated, DEFAULT_INDENTATION_SPACES);
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
	protected String generateConstantsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces) {
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		StringBuilder buf = new StringBuilder();
		this.listConstantsForClass(delegateClass).stream().forEach(field -> {
			generateConstant(field, delegateClass, importsUpdated, buf, indentationUnit);
		});

		importsUpdated.add(ClassNameUtils.makeImport(delegateClass.getTypeName()));
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
	public String generateDelegateClassCode(String targetPackageName, String targetInterfaceName,
			Class<?> delegateClass) {
		return this.generateDelegateClassCode(targetPackageName, targetInterfaceName, delegateClass,
				DEFAULT_INDENTATION_SPACES);
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
	public String generateDelegateClassCode(String targetPackageName, String targetInterfaceName,
			Class<?> delegateClass, int indentationSpaces) {
		StringBuilder buf = new StringBuilder();
		appendPackage(targetPackageName, buf);

		Set<String> importsUpdated = new HashSet<String>();
		String constants = this.generateConstantsForClassUpdatingImports(delegateClass, importsUpdated,
				indentationSpaces);
		String methods = this.generateMethodsForClassUpdatingImports(delegateClass, importsUpdated, indentationSpaces);

		appendSortedImports(buf, importsUpdated);

		String indentationUnit = makeIndentationUnit(indentationSpaces);

		buf.append(NEWLINE).append("/** ").append(NEWLINE).append(" * Wrapper of static elements in ")
				.append(delegateClass.getTypeName()).append(NEWLINE)
				.append(" * Generated by Interface-It: https://github.com/aro-tech/interface-it").append(NEWLINE)
				.append(" * {@link ").append(delegateClass.getTypeName()).append("}").append(NEWLINE).append(" */")
				.append(NEWLINE).append("public interface ").append(targetInterfaceName).append(" {").append(NEWLINE)
				.append(NEWLINE).append(NEWLINE).append(indentationUnit).append("// CONSTANTS: ").append(NEWLINE)
				.append(constants).append(NEWLINE).append(NEWLINE).append(indentationUnit)
				.append("// DELEGATE METHODS: ").append(NEWLINE).append(methods).append(NEWLINE).append("}");
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
			buf.append("package ").append(targetPackageName).append(";").append(NEWLINE).append(NEWLINE);
		}
	}

	private void appendSortedImports(StringBuilder buf, Set<String> importsUpdated) {
		String[] sortedImports = importsUpdated.toArray(new String[importsUpdated.size()]);
		Arrays.sort(sortedImports);
		for (String cur : sortedImports) {
			buf.append("import ").append(cur).append("; ").append(NEWLINE);
		}
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated) {
		return this.generateMethodsForClassUpdatingImports(delegateClass, importsUpdated, DEFAULT_INDENTATION_SPACES);
	}

	/**
	 * Generate java code for all delegate methods
	 * 
	 * @param delegateClass
	 * @param importsUpdated
	 * @param indentationSpaces
	 * @return code
	 */
	protected String generateMethodsForClassUpdatingImports(Class<?> delegateClass, Set<String> importsUpdated,
			int indentationSpaces) {
		StringBuilder buf = new StringBuilder();
		String indentationUnit = makeIndentationUnit(indentationSpaces);

		for (Method cur : this.listStaticMethodsForClass(delegateClass)) {
			buf.append(NEWLINE).append(this.makeDelegateMethod(cur, importsUpdated, indentationUnit)).append(NEWLINE)
					.append(NEWLINE);
		}
		return buf.toString();
	}

	/**
	 * Generate and write to file a delegate wrapping mix-in interface
	 * 
	 * @param dir
	 * @param targetInterfaceName
	 * @param delegateClass
	 * @param targetPackageName
	 * @param indentationSpaces
	 * @return
	 */
	public File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, int indentationSpaces) {
		String content = this.generateDelegateClassCode(targetPackageName, targetInterfaceName, delegateClass,
				indentationSpaces);
		File fileToWrite = new File(dir, targetInterfaceName + ".java");
		try (BufferedWriter w = new BufferedWriter(new FileWriter(fileToWrite))) {
			w.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileToWrite;
	}

}
