/**
 * 
 */
package com.github.aro_tech.interface_it.api;

import java.io.File;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.github.aro_tech.extended_mockito.ExtendedMockito;
import com.github.aro_tech.interface_it.format.CodeFormatter;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.policy.DeprecationPolicy;
import com.github.aro_tech.interface_it.util.mixin.AllAssertions;

/**
 * Unit tests for generating delegate methods
 * 
 * @author aro_tech
 *
 */
public class CoreMixinGeneratorTest implements AllAssertions, ExtendedMockito {
	private static final String TARGET_PACKAGE = "com.github.aro_tech.interface_it.results";
	private CoreMixinGenerator underTest = new CoreMixinGenerator();
	private Set<String> imports;
	private ArgumentNameSource mockArgNameSource = mock(ArgumentNameSource.class);
	private ArgumentNameSource defaultNameSource = new ArgumentNameSource() {
	};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		imports = new HashSet<>();
	}

	/**
	 * Test method for
	 * {@link com.github.aro_tech.interface_it.api.CoreMixinGenerator#listStaticMethodsForClass(java.lang.Class)}
	 * .
	 */
	@Test
	public void can_find_static_methods() {
		List<Method> results = underTest.listStaticMethodsForClass(Assertions.class);// (Thread.class);
		assertThat(results).isNotEmpty();
	}

	@Test
	public void can_find_constants() {
		List<Field> results = underTest.listConstantsForClass(org.mockito.Mockito.class);
		assertThat(results).isNotEmpty();
	}

	@Test
	public void can_generate_constants_code() {
		Set<String> imports = new HashSet<>();
		String result = underTest.generateConstantsForClassUpdatingImports(org.mockito.Mockito.class, imports,
				"MyMockito");
		assertThat(result)
				.contains("{@link org.mockito.Mockito#RETURNS_DEFAULTS}",
						"static final Answer<Object> RETURNS_DEFAULTS = Mockito.RETURNS_DEFAULTS;",
						"{@link org.mockito.Mockito#RETURNS_SMART_NULLS}",
						"static final Answer<Object> RETURNS_SMART_NULLS = Mockito.RETURNS_SMART_NULLS;")
				.doesNotContain("public static final");

		assertThat(imports).contains("org.mockito.stubbing.Answer", "org.mockito.Mockito");

	}

	@Test
	public void can_generate_when_method_signature_with_import() {
		Optional<Method> when = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		assertTrue(when.isPresent());

		assertThat(underTest.makeMethodSignature(when.get(), this.imports, defaultNameSource, ""))
				.startsWith("    default <T> OngoingStubbing<T> when(T");

		assertThat(this.imports).contains("org.mockito.stubbing.OngoingStubbing");
	}

	@Test
	public void can_generate_verify_method_signature_with_import() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeMethodSignature(verifyMethod.get(), imports, defaultNameSource, ""))
				.startsWith("    default <T> T verify(T").contains(", VerificationMode").endsWith(")");

		assertThat(this.imports).contains("org.mockito.verification.VerificationMode");
	}

	@Test
	public void can_generate_double_that_method_signature_with_import() {
		Optional<Method> doubleThatMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "doubleThat".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		assertTrue(doubleThatMethod.isPresent());

		assertThat(underTest.makeMethodSignature(doubleThatMethod.get(), imports, defaultNameSource, "").trim())
				.startsWith("default double doubleThat(ArgumentMatcher<Double> ").endsWith(")");

		assertThat(this.imports).contains("org.mockito.ArgumentMatcher");
	}

	@Test
	public void can_generate_catch_throwable_method_signature_with_import() {
		Optional<Method> method = underTest.listStaticMethodsForClass(Assertions.class).stream()
				.filter((Method m) -> "catchThrowable".equals(m.getName()) && m.getParameters().length == 1)
				.findFirst();

		assertTrue(method.isPresent());

		assertThat(underTest.makeMethodSignature(method.get(), imports, defaultNameSource, "").trim())
				.startsWith("default Throwable catchThrowable(ThrowableAssert.ThrowingCallable ").endsWith(")");

		assertThat(this.imports).contains("org.assertj.core.api.ThrowableAssert");
	}

	@Test
	public void can_generate_generic_extends_adt_method_signature_with_import() {
		Optional<Method> method = underTest.listStaticMethodsForClass(Assertions.class).stream()
				.filter((Method m) -> "assertThat".equals(m.getName()) && m.getParameters().length == 1
						&& m.getParameterTypes()[0].getSimpleName().equals("AssertDelegateTarget"))
				.findFirst();

		assertTrue(method.isPresent());

		assertThat(underTest.makeMethodSignature(method.get(), imports, defaultNameSource, "").trim())
				.startsWith("default <T extends AssertDelegateTarget> T assertThat(T ").endsWith(")");

		assertThat(this.imports).contains("org.assertj.core.api.AssertDelegateTarget");
	}

	@Test
	public void can_generate_entry_method_signature_with_import() {
		Optional<Method> entryMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "entry".equals(m.getName()) && m.getParameters().length == 2)
				.findFirst();

		assertTrue(entryMethod.isPresent());

		assertThat(underTest.makeMethodSignature(entryMethod.get(), imports, defaultNameSource, ""))
				.isEqualToIgnoringWhitespace("default <K,V> MapEntry<K, V> entry(K arg0, V arg1)");

		assertThat(this.imports).contains("org.assertj.core.data.MapEntry");

	}

	@Test
	public void can_generate_complex_asert_method_signature_with_import() {
		Optional<Method> assertMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "assertThat".equals(m.getName()) && m.getParameters().length == 1
						&& m.getParameters()[0].getParameterizedType().toString().contains("CharSeq"))
				.findFirst();

		assertTrue(assertMethod.isPresent());

		assertThat(underTest.makeMethodSignature(assertMethod.get(), imports, defaultNameSource, ""))
				.isEqualToIgnoringWhitespace(
						"default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(CharSequence arg0)");

		assertThat(this.imports).contains("org.assertj.core.api.AbstractCharSequenceAssert");
	}

	@Test
	public void can_generate_varargs_method_signature_with_import() {
		Optional<Method> tupleMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "tuple".equals(m.getName())).findFirst();

		assertTrue(tupleMethod.isPresent());

		assertThat(underTest.makeMethodSignature(tupleMethod.get(), imports, defaultNameSource, ""))
				.isEqualToIgnoringWhitespace("default Tuple tuple(Object... arg0)");

		assertThat(this.imports).contains("org.assertj.core.groups.Tuple");
	}

	@Test
	public void can_generate_verify_method_delegate_call() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeDelegateCall(verifyMethod.get(), "MyMockito", imports, defaultNameSource))
				.isEqualToIgnoringWhitespace("return Mockito.verify(arg0, arg1);");
	}

	@Test
	public void can_generate_when_method_delegate_call() {
		Optional<Method> whenMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		assertTrue(whenMethod.isPresent());

		assertThat(underTest.makeDelegateCall(whenMethod.get(), "MyMockito", imports, defaultNameSource))
				.isEqualToIgnoringWhitespace("return Mockito.when(arg0);");
	}

	@Test
	public void can_generate_reset_method_delegate_call() {
		Optional<Method> resetMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "reset".equals(m.getName())).findFirst();

		assertTrue(resetMethod.isPresent());

		assertThat(underTest.makeDelegateCall(resetMethod.get(), "MyMockito", imports, defaultNameSource))
				.isEqualToIgnoringWhitespace("Mockito.reset(arg0);");
	}

	@Test
	public void can_generate_verify_method_delegation_with_import() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod("MyMockito", verifyMethod.get(), imports,
				defaultNameSource);
		assertThat(delegateMethod).contains("/**")
				.contains("* Delegate call to " + verifyMethod.get().toGenericString())
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/").contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);").endsWith("}" + System.lineSeparator());
		assertThat(this.imports).contains("org.mockito.verification.VerificationMode");
	}

	@Test
	public void can_generate_anydouble_method_delegation_with_import() {
		Optional<Method> method = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "anyDouble".equals(m.getName())).findFirst();

		assertTrue(method.isPresent());

		String delegateMethod = underTest.makeDelegateMethod("Mockito", method.get(), imports, defaultNameSource);
		assertThat(delegateMethod).contains("/**").contains("* Delegate call to " + method.get().toGenericString())
				.contains("* {@link org.mockito.Matchers#anyDouble()}").contains("*/")
				.contains("default double anyDouble() {").contains("return Matchers.anyDouble();")
				.endsWith("}" + System.lineSeparator());
		assertThat(this.imports).contains("org.mockito.Matchers");
	}

	@Test
	public void can_generate_when_method_delegation_with_import() {
		Optional<Method> whenMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		assertTrue(whenMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod("MyMockito", whenMethod.get(), imports, defaultNameSource);
		assertThat(delegateMethod).contains("/**").contains("* Delegate call to " + whenMethod.get().toGenericString())
				.contains("{@link org.mockito.Mockito#when(java.lang.Object)}").contains("*/")
				.contains("default <T> OngoingStubbing<T> when(T arg0) {").contains("return Mockito.when(arg0);")
				.endsWith("}" + System.lineSeparator());
		assertThat(this.imports).contains("org.mockito.stubbing.OngoingStubbing");
	}

	@Test
	public void signature_includes_throws() throws NoSuchMethodException, SecurityException {
		HashSet<String> importNamesOut = new HashSet<String>();
		String signature = underTest.makeMethodSignature(Thread.class.getMethod("sleep", long.class), importNamesOut,
				defaultNameSource, "");

		assertThat(signature).contains(" throws InterruptedException");
		assertThat(importNamesOut).contains("java.lang.InterruptedException");
	}

	@Test
	public void can_generate_whole_interface() {

		String targetInterfaceName = "Mockito";
		Class<org.mockito.Mockito> delegateClass = org.mockito.Mockito.class;
		String classText = underTest.generateDelegateClassCode(TARGET_PACKAGE, targetInterfaceName, delegateClass,
				defaultNameSource);
		assertThat(classText)
				.startsWith("package com.github.aro_tech.interface_it.results;" + System.lineSeparator()
						+ System.lineSeparator())
				.doesNotContain("import java.lang").doesNotContain("import org.mockito.Mockito")
				.doesNotContain("import java.lang.Class<").contains("/**").contains("* Delegate call to ")
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/").contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return org.mockito.Mockito.verify(arg0, arg1);").contains("return Matchers.");
	}

	@Test
	public void can_filter_out_methods() {

		Class<org.mockito.Mockito> delegateClass = org.mockito.Mockito.class;
		MultiFileOutputOptions options = new MultiFileOutputOptions() {

			@Override
			public String getTargetPackageNameForDelegate(Class<?> delegateClass) {
				return TARGET_PACKAGE;
			}

			@Override
			public File getMixinSaveDirectoryForDelegate(Class<?> delegate) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
			 * getMethodFilter()
			 */
			@Override
			public Predicate<? super Method> getMethodFilterForDelegate(Class<?> delegate) {
				return m -> false; // ignore all methods
			}
		};

		String classText = underTest.generateDelegateClassCode(delegateClass, defaultNameSource, options);
		assertThat(classText)
				.startsWith("package com.github.aro_tech.interface_it.results;" + System.lineSeparator()
						+ System.lineSeparator())
				.contains("public interface MockitoMixin").doesNotContain("return ").doesNotContain("default ");
	}

	@Test
	public void can_generate_mixin_with_supertype() {

		Class<org.mockito.Mockito> delegateClass = org.mockito.Mockito.class;
		MultiFileOutputOptions options = new MultiFileOutputOptions() {

			@Override
			public String getTargetPackageNameForDelegate(Class<?> delegateClass) {
				return TARGET_PACKAGE;
			}

			@Override
			public File getMixinSaveDirectoryForDelegate(Class<?> delegate) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.github.aro_tech.interface_it.api.MultiFileOutputOptions#
			 * getSuperTypes()
			 */
			@Override
			public Set<String> getSuperTypes(Class<?> clazz) {
				return new HashSet<String>() {
					{
						add("TypeA");
						add("TypeB");
						add("TypeC");
					}
				};
			}

		};

		String classText = underTest.generateDelegateClassCode(delegateClass, defaultNameSource, options);
		assertThat(classText)
				.startsWith("package com.github.aro_tech.interface_it.results;" + System.lineSeparator()
						+ System.lineSeparator())
				.contains("public interface MockitoMixin extends TypeA, TypeB, TypeC {")
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/").contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);");
	}

	@Test
	public void verify_indentation() {
		for (int i = 0; i < 10; i++) {
			testWithIndentation(i);
		}
	}

	private void testWithIndentation(final int indentationSpaces) {
		String classText = new CoreMixinGenerator(null, DeprecationPolicy.PROPAGATE_DEPRECATION,
				new CodeFormatter(indentationSpaces)).generateDelegateClassCode(TARGET_PACKAGE, "MyMath", Math.class,
						defaultNameSource);
		String[] lines = classText.split("\n");
		IndentationContext indentation = new IndentationContext();
		for (int lineNum = 0; lineNum < lines.length; lineNum++) {
			verifyCurrentLine(indentationSpaces, lines, indentation, lineNum);
		}
	}

	private static class IndentationContext {
		private int indentationLevel = 0;

		int getLevel() {
			return indentationLevel;
		}

		void incrementLevel() {
			this.indentationLevel++;
		}

		void decrementLevel() {
			this.indentationLevel--;
		}

	}

	private static class LineContext {
		private int firstNonWSIndex = -1;
		private int leadingSpaces = 0;
		private int i = 0;
		private final char[] lineChars;
		private char currentChar = '-';
		private boolean done = false;

		public LineContext(String line) {
			super();
			this.lineChars = line.toCharArray();
			if (this.lineChars.length > 0) {
				currentChar = lineChars[0];
			} else {
				done = true;
			}
		}

		int getFirstNonWSIndex() {
			return firstNonWSIndex;
		}

		void setFirstNonWSIndex(int firstNonWSIndex) {
			this.firstNonWSIndex = firstNonWSIndex;
		}

		int getLeadingSpaces() {
			return leadingSpaces;
		}

		void incrementLeadingSpaces() {
			this.leadingSpaces++;
		}

		boolean continueLoop() {
			if (done || i + 1 >= lineChars.length) {
				return false;
			}
			i++;
			currentChar = this.lineChars[i];
			return true;
		}

		int getI() {
			return i;
		}

		char getCurrentChar() {
			return currentChar;
		}

	}

	private void verifyCurrentLine(final int indentationSpaces, String[] lines, IndentationContext indentation,
			int lineNum) {
		final String line = lines[lineNum];
		LineContext ctx = new LineContext(line);
		if (line.length() > 0) {
			do {
				verifyForCurrentCharacter(indentation, ctx, line, lineNum, indentationSpaces);
			} while (ctx.continueLoop());
		}
	}

	private void verifyForCurrentCharacter(IndentationContext indentationCxt, LineContext lineCtx, final String line,
			int lineNum, final int indentationSpaces) {
		decrementIndentationContextIfNecessary(indentationCxt, lineCtx);
		if (wasWhitespaceToThisPoint(lineCtx)) {
			handleMoreWhitespaceOrEndOfWhitespace(indentationCxt, lineCtx, line, lineNum, indentationSpaces);
		}
		incrementIndentationContextIfNecessary(indentationCxt, lineCtx);
	}

	private void handleMoreWhitespaceOrEndOfWhitespace(IndentationContext indentationCxt, LineContext lineCtx,
			final String line, int lineNum, final int indentationSpaces) {
		if (isCurrentlyWhitespace(lineCtx)) {
			lineCtx.incrementLeadingSpaces();
		} else {
			handleFirstNonWhitespaceCharacterAndVerifyIndentation(indentationCxt, lineCtx, line, lineNum,
					indentationSpaces);
		}
	}

	private void decrementIndentationContextIfNecessary(IndentationContext indentationCxt, LineContext lineCtx) {
		if (lineCtx.getCurrentChar() == '}') {
			indentationCxt.decrementLevel();
		}
	}

	private boolean isCurrentlyWhitespace(LineContext lineCtx) {
		return Character.isWhitespace(lineCtx.getCurrentChar());
	}

	private boolean wasWhitespaceToThisPoint(LineContext lineCtx) {
		return lineCtx.getFirstNonWSIndex() < 0;
	}

	private void handleFirstNonWhitespaceCharacterAndVerifyIndentation(IndentationContext indentationCxt,
			LineContext lineCtx, final String line, int lineNum, final int indentationSpaces) {
		lineCtx.setFirstNonWSIndex(lineCtx.getI());
		int expectedIndentationSpaces = calculateExpectedIndentationSpaces(indentationSpaces, indentationCxt.getLevel(),
				lineCtx.getCurrentChar());
		assertEquals(
				"Expected " + expectedIndentationSpaces + " and not " + lineCtx.getLeadingSpaces()
						+ " leading spaces for line " + lineNum + ": " + line,
				expectedIndentationSpaces, lineCtx.getLeadingSpaces());
	}

	private void incrementIndentationContextIfNecessary(IndentationContext indentationCxt, LineContext lineCtx) {
		if (lineCtx.getCurrentChar() == '{') {
			indentationCxt.incrementLevel();
		}
	}

	private int calculateExpectedIndentationSpaces(final int indentationSpaces, int indentationLevel,
			char currentChar) {
		int expectedIndentationSpaces = indentationLevel * indentationSpaces;
		if (currentChar == '*') {
			expectedIndentationSpaces++; // expect an extra space
											// when in a javadoc
											// comment
		}
		return expectedIndentationSpaces;
	}

	@Test
	public void delegation_code_compiles_and_runs() {
		// eating own dog food:
		Writer mock = mock(java.io.Writer.class);
		assertThat(mock).isNotNull();
	}

	@Test
	public void should_use_package_name_in_delegate_call_if_delegate_class_name_equals_target_class_name() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeDelegateCall(verifyMethod.get(), "Mockito", imports, defaultNameSource))
				.isEqualToIgnoringWhitespace("return org.mockito.Mockito.verify(arg0, arg1);");

	}

	@Test
	public void should_use_package_name_in_constant_def_if_delegate_class_name_equals_target_class_name()
			throws NoSuchFieldException, SecurityException {
		Field field = org.mockito.Mockito.class.getDeclaredField("RETURNS_MOCKS");
		StringBuilder buf = new StringBuilder();
		underTest.generateConstant(field, org.mockito.Mockito.class, imports, buf, "Mockito", "");
		assertThat(buf.toString())
				.contains("static final Answer<Object> RETURNS_MOCKS = org.mockito.Mockito.RETURNS_MOCKS;");

	}

	@Test
	public void can_use_argument_names_in_signature_and_delegation() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());

		when(this.mockArgNameSource.getArgumentNameFor(verifyMethod.get(), 0)).thenReturn("mock");
		when(this.mockArgNameSource.getArgumentNameFor(verifyMethod.get(), 1)).thenReturn("mode");

		assertThat(underTest.makeMethodSignature(verifyMethod.get(), imports, mockArgNameSource, ""))
				.isEqualToIgnoringWhitespace("default <T> T verify(T mock, VerificationMode mode)");

		assertThat(underTest.makeDelegateCall(verifyMethod.get(), "Mockito", imports, mockArgNameSource))
				.isEqualToIgnoringWhitespace("return org.mockito.Mockito.verify(mock, mode);");

	}

	@Test
	public void should_propagate_deprecation() {
		Optional<Method> deprecatedMethod = getAndVerifyDeprecatedMethod();

		assertThat(underTest.makeMethodSignature(deprecatedMethod.get(), imports, defaultNameSource, ""))
				.startsWith("    @Deprecated" + System.lineSeparator() + "    default");
	}

	private Optional<Method> getAndVerifyDeprecatedMethod() {
		Optional<Method> deprecatedMethod = underTest.listStaticMethodsForClass(java.net.URLEncoder.class).stream()
				.filter((Method m) -> m.getDeclaredAnnotationsByType(Deprecated.class).length > 0).findFirst();
		assertTrue(deprecatedMethod.isPresent());
		return deprecatedMethod;
	}

	@Test
	public void should_not_propagate_deprecation_if_propagation_disabled() {
		Optional<Method> deprecatedMethod = getAndVerifyDeprecatedMethod();
		underTest = new CoreMixinGenerator(null, DeprecationPolicy.WRAP_WITHOUT_DEPRECATING,
				CodeFormatter.getDefault());
		assertThat(underTest.makeMethodSignature(deprecatedMethod.get(), imports, defaultNameSource, ""))
				.startsWith("    default");
	}

	@Test
	public void should_not_propagate_deprecation_if_propagation_disabled_LEGACY() {
		Optional<Method> deprecatedMethod = getAndVerifyDeprecatedMethod();
		underTest = new CoreMixinGenerator(null, DeprecationPolicy.WRAP_WITHOUT_DEPRECATING,
				CodeFormatter.getDefault());
		assertThat(underTest.makeMethodSignature(deprecatedMethod.get(), imports, defaultNameSource))
				.startsWith("    default");
	}

	@Test
	public void should_ignore_deprecated_method_following_policy() {
		Optional<Method> deprecatedMethod = getAndVerifyDeprecatedMethod();
		underTest = new CoreMixinGenerator(null, DeprecationPolicy.IGNORE_DEPRECATED_METHODS,
				CodeFormatter.getDefault());
		String allMethods = underTest.generateMethodsForClassUpdatingImports(java.net.URLEncoder.class, new HashSet<>(),
				"MyURLEncoder", new ArgumentNameSource() {
				});
		assertThat(allMethods)
				.doesNotContain(
						underTest.makeMethodSignature(deprecatedMethod.get(), imports, defaultNameSource, "URLEncoder"))
				.contains("encode");
	}

	@Test
	public void should_ignore_deprecated_method_following_policy_LEGACY() {
		Optional<Method> deprecatedMethod = getAndVerifyDeprecatedMethod();
		underTest = new CoreMixinGenerator(null, DeprecationPolicy.IGNORE_DEPRECATED_METHODS,
				CodeFormatter.getDefault());
		String allMethods = underTest.generateMethodsForClassUpdatingImports(java.net.URLEncoder.class, new HashSet<>(),
				"MyURLEncoder", new ArgumentNameSource() {
				});
		assertThat(allMethods)
				.doesNotContain(underTest.makeMethodSignature(deprecatedMethod.get(), imports, defaultNameSource))
				.contains("encode");
	}

	@Test
	public void should_qualify_return_type_when_mixin_name_equals_delegate_name_and_return_type_is_delegate_type() {
		Optional<Method> concatMethod = underTest.listStaticMethodsForClass(java.util.stream.IntStream.class).stream()
				.filter((Method m) -> "concat".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(concatMethod.isPresent());

		assertThat(underTest.makeMethodSignature(concatMethod.get(), this.imports, defaultNameSource, "IntStream"))
				.startsWith("    default java.util.stream.IntStream concat(");
	}

	@Test
	public void should_qualify_argument_type_when_mixin_name_equals_delegate_name_and_return_type_is_delegate_type() {
		Optional<Method> concatMethod = underTest.listStaticMethodsForClass(java.util.stream.IntStream.class).stream()
				.filter((Method m) -> "concat".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(concatMethod.isPresent());

		assertThat(underTest.makeMethodSignature(concatMethod.get(), this.imports, defaultNameSource, "IntStream"))
				.contains("concat(java.util.stream.IntStream arg0, java.util.stream.IntStream arg1)");
	}

	// default IntStream.Builder builder() {
	@Test
	public void should_qualify_return_type_when_mixin_name_equals_delegate_name_and_return_type_is_nested_type_of_delegate_type() {
		Optional<Method> builderMethod = underTest.listStaticMethodsForClass(java.util.stream.IntStream.class).stream()
				.filter((Method m) -> "builder".equals(m.getName()) && m.getParameters().length == 0).findFirst();

		assertTrue(builderMethod.isPresent());

		assertThat(underTest.makeMethodSignature(builderMethod.get(), this.imports, defaultNameSource, "IntStream"))
				.startsWith("    default java.util.stream.IntStream.Builder builder()");
	}

}
