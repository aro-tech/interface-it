/**
 * 
 */
package org.interfaceit;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.interfaceit.util.mixin.AssertJ;
import org.interfaceit.util.mixin.MockitoDelegate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for generating delegate methods
 * 
 * @author aro_tech
 *
 */
public class DelegateMethodGeneratorTest implements AssertJ, MockitoDelegate {
	private static final String TARGET_PACKAGE = "org.interfaceit.results";
	private DelegateMethodGenerator underTest = new DelegateMethodGenerator();
	private Set<String> imports;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		imports = new HashSet<>();
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.DelegateMethodGenerator#listStaticMethodsForClass(java.lang.Class)}
	 * .
	 */
	@Test
	public void can_find_static_methods() {
		List<Method> results = underTest.listStaticMethodsForClass(Assertions.class);// (Thread.class);
		assertThat(results).isNotEmpty();
		// for (Method cur : results) {
		//
		// if (cur.getName().equals("assertThat")) {
		// for (Parameter p : cur.getParameters()) {
		// System.out.println("type=" + p.getType());
		// }
		// }
		// }
	}

	@Test
	public void can_find_constants() {
		List<Field> results = underTest.listConstantsForClass(org.mockito.Mockito.class);
		assertThat(results).isNotEmpty();
		// System.out.println(results);
	}

	@Test
	public void can_generate_constants_code() {
		Set<String> imports = new HashSet<>();
		String result = underTest.generateConstantsForClassUpdatingImports(org.mockito.Mockito.class, imports);
		assertThat(result).contains("{@link org.mockito.Mockito#RETURNS_DEFAULTS}",
				"public static final Answer RETURNS_DEFAULTS = Mockito.RETURNS_DEFAULTS;",
				"{@link org.mockito.Mockito#RETURNS_SMART_NULLS}",
				"public static final Answer RETURNS_SMART_NULLS = Mockito.RETURNS_SMART_NULLS;");
		// System.out.println(result);

		assertThat(imports).contains("org.mockito.stubbing.Answer", "org.mockito.Mockito");

	}

	@Test
	public void can_generate_when_method_signature_with_import() {
		Optional<Method> when = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		Assert.assertTrue(when.isPresent());

		assertThat(underTest.makeMethodSignature(when.get(), this.imports))
				.startsWith("default <T> OngoingStubbing<T> when(T");

		assertThat(this.imports).contains("org.mockito.stubbing.OngoingStubbing");
	}

	@Test
	public void can_generate_verify_method_signature_with_import() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeMethodSignature(verifyMethod.get(), this.imports)).startsWith("default <T> T verify(T")
				.contains(", VerificationMode").endsWith(")");

		assertThat(this.imports).contains("org.mockito.verification.VerificationMode");
	}

	@Test
	public void can_generate_double_that_method_signature_with_import() {
		Optional<Method> doubleThatMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "doubleThat".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		Assert.assertTrue(doubleThatMethod.isPresent());

		assertThat(underTest.makeMethodSignature(doubleThatMethod.get(), this.imports).trim())
				.startsWith("default double doubleThat(Matcher<Double> ").endsWith(")");

		assertThat(this.imports).contains("org.hamcrest.Matcher");
	}

	@Test
	public void can_generate_catch_throwable_method_signature_with_import() {
		Optional<Method> method = underTest.listStaticMethodsForClass(Assertions.class).stream()
				.filter((Method m) -> "catchThrowable".equals(m.getName()) && m.getParameters().length == 1)
				.findFirst();

		Assert.assertTrue(method.isPresent());

		assertThat(underTest.makeMethodSignature(method.get(), this.imports).trim())
				.startsWith("default Throwable catchThrowable(ThrowableAssert.ThrowingCallable ").endsWith(")");

		assertThat(this.imports).contains("org.assertj.core.api.ThrowableAssert.ThrowingCallable");
	}

	// <T extends AssertDelegateTarget> T assertThat(T assertion)
	@Test
	public void can_generate_generic_extends_adt_method_signature_with_import() {
		Optional<Method> method = underTest.listStaticMethodsForClass(Assertions.class).stream()
				.filter((Method m) -> "assertThat".equals(m.getName()) && m.getParameters().length == 1
						&& m.getParameterTypes()[0].getSimpleName().equals("AssertDelegateTarget"))
				.findFirst();

		Assert.assertTrue(method.isPresent());

		assertThat(underTest.makeMethodSignature(method.get(), this.imports).trim())
				.startsWith("default <T extends AssertDelegateTarget> T assertThat(T ").endsWith(")");

		assertThat(this.imports).contains("org.assertj.core.api.AssertDelegateTarget");
	}

	@Test
	public void can_generate_entry_method_signature_with_import() {
		Optional<Method> entryMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "entry".equals(m.getName()) && m.getParameters().length == 2)
				.findFirst();

		Assert.assertTrue(entryMethod.isPresent());

		assertThat(underTest.makeMethodSignature(entryMethod.get(), this.imports))
				.isEqualToIgnoringWhitespace("default <K,V> MapEntry<K, V> entry(K arg0, V arg1)");

		assertThat(this.imports).contains("org.assertj.core.data.MapEntry");

	}

	@Test
	public void can_generate_complex_asert_method_signature_with_import() {

		/*
		 * TODO: test for return type of: public static
		 * AbstractCharSequenceAssert<?, ? extends CharSequence>
		 * assertThat(CharSequence actual) { return
		 * AssertionsForInterfaceTypes.assertThat(actual); }
		 */

		Optional<Method> assertMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "assertThat".equals(m.getName()) && m.getParameters().length == 1
						&& m.getParameters()[0].getParameterizedType().toString().contains("CharSeq"))
				.findFirst();

		Assert.assertTrue(assertMethod.isPresent());

		assertThat(underTest.makeMethodSignature(assertMethod.get(), this.imports)).isEqualToIgnoringWhitespace(
				"default AbstractCharSequenceAssert<?, ? extends CharSequence> assertThat(CharSequence arg0)");

		assertThat(this.imports).contains("org.assertj.core.api.AbstractCharSequenceAssert");

	}

	@Test
	public void can_generate_varargs_method_signature_with_import() {
		Optional<Method> tupleMethod = underTest.listStaticMethodsForClass(org.assertj.core.api.Assertions.class)
				.stream().filter((Method m) -> "tuple".equals(m.getName())).findFirst();

		Assert.assertTrue(tupleMethod.isPresent());

		assertThat(underTest.makeMethodSignature(tupleMethod.get(), this.imports))
				.isEqualToIgnoringWhitespace("default Tuple tuple(Object... arg0)");

		assertThat(this.imports).contains("org.assertj.core.groups.Tuple");

	}

	@Test
	public void can_generate_verify_method_delegate_call() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeDelegateCall(verifyMethod.get(), "MyMockito"))
				.isEqualToIgnoringWhitespace("return Mockito.verify(arg0, arg1);");
	}

	@Test
	public void can_generate_when_method_delegate_call() {
		Optional<Method> whenMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		Assert.assertTrue(whenMethod.isPresent());

		assertThat(underTest.makeDelegateCall(whenMethod.get(), "MyMockito"))
				.isEqualToIgnoringWhitespace("return Mockito.when(arg0);");
	}

	@Test
	public void can_generate_reset_method_delegate_call() {
		Optional<Method> resetMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "reset".equals(m.getName())).findFirst();

		Assert.assertTrue(resetMethod.isPresent());

		assertThat(underTest.makeDelegateCall(resetMethod.get(), "MyMockito"))
				.isEqualToIgnoringWhitespace("Mockito.reset(arg0);");
	}

	@Test
	public void can_generate_verify_method_delegation_with_import() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod(verifyMethod.get(), "MyMockito", this.imports);
		assertThat(delegateMethod).contains("/**")
				.contains("* Delegate call to " + verifyMethod.get().toGenericString())
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/").contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);").endsWith("}" + System.lineSeparator());
		// System.out.println(delegateMethod);

		assertThat(this.imports).contains("org.mockito.verification.VerificationMode");
	}

	@Test
	public void can_generate_when_method_delegation_with_import() {
		Optional<Method> whenMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "when".equals(m.getName()) && m.getParameters().length == 1).findFirst();

		Assert.assertTrue(whenMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod(whenMethod.get(), "MyMockito", this.imports);
		assertThat(delegateMethod).contains("/**").contains("* Delegate call to " + whenMethod.get().toGenericString())
				.contains("{@link org.mockito.Mockito#when(java.lang.Object)}").contains("*/")
				.contains("default <T> OngoingStubbing<T> when(T arg0) {")
				.contains("return Mockito.when(arg0);").endsWith("}" + System.lineSeparator());
		// System.out.println(delegateMethod);

		assertThat(this.imports).contains("org.mockito.stubbing.OngoingStubbing");
	}

	@Test
	public void signature_includes_throws() throws NoSuchMethodException, SecurityException {
		// public static native void java.lang.Thread.sleep(long) throws
		// java.lang.InterruptedException

		HashSet<String> importNamesOut = new HashSet<String>();
		String signature = underTest.makeMethodSignature(Thread.class.getMethod("sleep", long.class), importNamesOut);

		assertThat(signature).contains(" throws InterruptedException");
		assertThat(importNamesOut).contains("java.lang.InterruptedException");
	}

	@Test
	public void can_generate_whole_interface() {

		String targetInterfaceName = "MockitoDelegate";
		Class<Mockito> delegateClass = Mockito.class;
		String classText = underTest.generateDelegateClassCode(TARGET_PACKAGE, targetInterfaceName, delegateClass);

		// System.out.println(classText);

		assertThat(classText)
				.startsWith("package org.interfaceit.results;" + System.lineSeparator() + System.lineSeparator())
				.doesNotContain("import java.lang.Class<").contains("/**").contains("* Delegate call to ")
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/").contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);");

	}

	@Test
	public void verify_indentation() {
		final int indentationSpaces = 5;
		String classText = underTest.generateDelegateClassCode(TARGET_PACKAGE, "MyMath", Math.class, indentationSpaces);
		// System.out.println(classText);

		String[] lines = classText.split("\n");
		int indentationLevel = 0;
		for (int lineNum = 0; lineNum < lines.length; lineNum++) {
			String line = lines[lineNum];
			int firstNonWSIndex = -1;
			int leadingSpaces = 0;
			char[] lineChars = line.toCharArray();
			for (int i = 0; i < lineChars.length; i++) {
				if (lineChars[i] == '}') {
					indentationLevel--;
				}
				if (firstNonWSIndex < 0) {
					if (Character.isWhitespace(lineChars[i])) {
						leadingSpaces++;
					} else {
						firstNonWSIndex = i;
						int expectedIndentationSpaces = indentationLevel * indentationSpaces;

						if (lineChars[i] == '*') {
							expectedIndentationSpaces++; // expect extra space
															// in javadoc
															// comment
						}
						Assert.assertEquals(
								"Expected " + expectedIndentationSpaces + " and not " + leadingSpaces
										+ " leading spaces for line " + lineNum + ": " + line,
								expectedIndentationSpaces, leadingSpaces);
					}
				}
				if (lineChars[i] == '{') {
					indentationLevel++;
				}
			}
		}
	}
	
	@Test
	public void delegation_code_compiles_and_runs() {
		// eating own dog food:
		Writer mock = mock(java.io.Writer.class);
		assertThat(mock).isNotNull();
	}
	
	@Test
	public void should_use_package_name_if_delegate_class_name_equals_target_class_name() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		assertThat(underTest.makeDelegateCall(verifyMethod.get(), "Mockito"))
				.isEqualToIgnoringWhitespace("return org.mockito.Mockito.verify(arg0, arg1);");

	}

	// TODO: TDD case where conflict between delegate class name and wrapper
	// ...class name (e.g.: Mockito) - must use full package name instead of
	// ...simple name in method code
	// TODO: mapping argument names
	// TODO: generate whole interface file with imports
	// TODO: handle singleton calls?
	// TODO: mapping from Javadoc

}
