/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author arothkopf
 *
 */
public class DelegateMethodGeneratorTest {
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
		List<Method> results = underTest
				.listStaticMethodsForClass(Thread.class);
		Assertions.assertThat(results).isNotEmpty();
		// System.out.println(results);
	}

	@Test
	public void can_find_constants() {
		List<Field> results = underTest
				.listConstantsForClass(org.mockito.Mockito.class);
		Assertions.assertThat(results).isNotEmpty();
		// System.out.println(results);
	}

	@Test
	public void can_generate_constants_code() {
		Set<String> imports = new HashSet<>();
		String result = underTest.generateConstantsForClassUpdatingImports(
				org.mockito.Mockito.class, imports);
		Assertions
				.assertThat(result)
				.contains(
						"{@link org.mockito.Mockito#RETURNS_DEFAULTS}",
						"public static final Answer RETURNS_DEFAULTS = Mockito.RETURNS_DEFAULTS;",
						"{@link org.mockito.Mockito#RETURNS_SMART_NULLS}",
						"public static final Answer RETURNS_SMART_NULLS = Mockito.RETURNS_SMART_NULLS;");
		// System.out.println(result);

		Assertions.assertThat(imports).contains("org.mockito.stubbing.*",
				"org.mockito.*");

	}

	@Test
	public void can_generate_when_method_signature_with_import() {
		Optional<Method> when = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "when".equals(m.getName())
						&& m.getParameters().length == 1).findFirst();

		Assert.assertTrue(when.isPresent());

		Assertions.assertThat(
				underTest.makeMethodSignature(when.get(), this.imports))
				.startsWith("default <T> OngoingStubbing<T> when(T");

		Assertions.assertThat(this.imports).contains("org.mockito.stubbing.*");
	}

	@Test
	public void can_generate_verify_method_signature_with_import() {
		Optional<Method> verifyMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "verify".equals(m.getName())
						&& m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		Assertions
				.assertThat(
						underTest.makeMethodSignature(verifyMethod.get(),
								this.imports))
				.startsWith("default <T> T verify(T")
				.contains(", VerificationMode").endsWith(")");

		Assertions.assertThat(this.imports).contains(
				"org.mockito.verification.*");
	}

	@Test
	public void can_generate_entry_method_signature_with_import() {
		Optional<Method> entryMethod = underTest
				.listStaticMethodsForClass(
						org.assertj.core.api.Assertions.class)
				.stream()
				.filter((Method m) -> "entry".equals(m.getName())
						&& m.getParameters().length == 2).findFirst();

		Assert.assertTrue(entryMethod.isPresent());

		Assertions.assertThat(
				underTest.makeMethodSignature(entryMethod.get(), this.imports))
				.isEqualToIgnoringWhitespace(
						"default <K,V> MapEntry<K, V> entry(K arg0, V arg1)");

		Assertions.assertThat(this.imports).contains("org.assertj.core.data.*");

	}

	@Test
	public void can_generate_varargs_method_signature_with_import() {
		Optional<Method> tupleMethod = underTest
				.listStaticMethodsForClass(
						org.assertj.core.api.Assertions.class).stream()
				.filter((Method m) -> "tuple".equals(m.getName())).findFirst();

		Assert.assertTrue(tupleMethod.isPresent());

		Assertions.assertThat(
				underTest.makeMethodSignature(tupleMethod.get(), this.imports))
				.isEqualToIgnoringWhitespace(
						"default Tuple tuple(Object... arg0)");

		Assertions.assertThat(this.imports).contains(
				"org.assertj.core.groups.*");

	}

	@Test
	public void can_generate_verify_method_delegate_call() {
		Optional<Method> verifyMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "verify".equals(m.getName())
						&& m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		Assertions.assertThat(underTest.makeDelegateCall(verifyMethod.get()))
				.isEqualToIgnoringWhitespace(
						"return Mockito.verify(arg0, arg1);");
	}

	@Test
	public void can_generate_when_method_delegate_call() {
		Optional<Method> whenMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "when".equals(m.getName())
						&& m.getParameters().length == 1).findFirst();

		Assert.assertTrue(whenMethod.isPresent());

		Assertions.assertThat(underTest.makeDelegateCall(whenMethod.get()))
				.isEqualToIgnoringWhitespace("return Mockito.when(arg0);");
	}

	@Test
	public void can_generate_reset_method_delegate_call() {
		Optional<Method> resetMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "reset".equals(m.getName())).findFirst();

		Assert.assertTrue(resetMethod.isPresent());

		Assertions.assertThat(underTest.makeDelegateCall(resetMethod.get()))
				.isEqualToIgnoringWhitespace("Mockito.reset(arg0);");
	}

	@Test
	public void can_generate_verify_method_delegation_with_import() {
		Optional<Method> verifyMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "verify".equals(m.getName())
						&& m.getParameters().length == 2).findFirst();

		Assert.assertTrue(verifyMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod(
				verifyMethod.get(), this.imports);
		Assertions
				.assertThat(delegateMethod)
				.contains("/**")
				.contains(
						"* Delegate call to "
								+ verifyMethod.get().toGenericString())
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/")
				.contains(
						"default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);").endsWith("}\n");
		// System.out.println(delegateMethod);

		Assertions.assertThat(this.imports).contains(
				"org.mockito.verification.*");
	}

	@Test
	public void can_generate_when_method_delegation_with_import() {
		Optional<Method> whenMethod = underTest
				.listStaticMethodsForClass(org.mockito.Mockito.class)
				.stream()
				.filter((Method m) -> "when".equals(m.getName())
						&& m.getParameters().length == 1).findFirst();

		Assert.assertTrue(whenMethod.isPresent());

		String delegateMethod = underTest.makeDelegateMethod(whenMethod.get(),
				this.imports);
		Assertions
				.assertThat(delegateMethod)
				.contains("/**")
				.contains(
						"* Delegate call to "
								+ whenMethod.get().toGenericString())
				.contains("{@link org.mockito.Mockito#when(java.lang.Object)}")
				.contains("*/")
				.contains("default <T> OngoingStubbing<T> when(T arg0) {")
				.contains("return Mockito.when(arg0);").endsWith("}\n");
		// System.out.println(delegateMethod);

		Assertions.assertThat(this.imports).contains("org.mockito.stubbing.*");
	}

	@Test
	public void signature_includes_throws() throws NoSuchMethodException,
			SecurityException {
		// public static native void java.lang.Thread.sleep(long) throws
		// java.lang.InterruptedException

		HashSet<String> importNamesOut = new HashSet<String>();
		String signature = underTest.makeMethodSignature(
				Thread.class.getMethod("sleep", long.class), importNamesOut);

		Assertions.assertThat(signature).contains(
				" throws InterruptedException");
		Assertions.assertThat(importNamesOut).contains("java.lang.*");
	}

	@Test
	public void can_generate_whole_interface() {

		String targetInterfaceName = "Mockito";
		Class<Mockito> delegateClass = Mockito.class;
		String classText = underTest.generateDelegateClassCode(TARGET_PACKAGE,
				targetInterfaceName, delegateClass);

		System.out.println(classText);

		Assertions
				.assertThat(classText)
				.startsWith("package org.interfaceit.results;\n")
				.doesNotContain("import java.lang.Class<")
				.contains("/**")
				.contains("* Delegate call to ")
				.contains(
						"* {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}")
				.contains("*/")
				.contains(
						"default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);");

	}

	@Test
	public void verify_indentation() {
		final int indentationSpaces = 5;
		String classText = underTest.generateDelegateClassCode(TARGET_PACKAGE,
				"MyMath", Math.class, indentationSpaces);
		System.out.println(classText);

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
						int expectedIndentationSpaces = indentationLevel
								* indentationSpaces;

						if (lineChars[i] == '*') {
							expectedIndentationSpaces++; // expect extra space
															// in javadoc
															// comment
						}
						Assert.assertEquals("Expected "
								+ expectedIndentationSpaces + " and not "
								+ leadingSpaces + " leading spaces for line " + lineNum + ": "
								+ line, expectedIndentationSpaces,
								leadingSpaces);
					}
				}
				if (lineChars[i] == '{') {
					indentationLevel++;
				} 
			}
		}
		// TODO: split class generated based newline, track curly braces and
		// verify indentation accordingly
	}

	// TODO: mapping argument names
	// TODO: generate whole interface file with imports
	// TODO: handle singleton calls?
	// TODO: git
	// TODO: mapping from Javadoc

}
