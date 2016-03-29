/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;

import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.statistics.GenerationStatistics;
import org.interfaceit.util.mixin.AllAssertions;
import org.interfaceit.util.mixin.Mockito;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for StatisticProvidingClassCodeGenerator
 * 
 * @author aro_tech
 *
 */
public class StatisticProvidingClassCodeGeneratorTest implements AllAssertions, Mockito {
	private static final int NUMBER_OF_STATIC_METHODS_IN_MATH_CLASS = 73;

	private StatisticProvidingClassCodeGenerator underTest;

	private ArgumentNameSource defaultNameSource = new ArgumentNameSource() {
	};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		underTest = new StatisticProvidingClassCodeGenerator();
	}

	@Test
	public void should_provide_statistics() {
		String result = underTest.generateDelegateClassCode("org.whatever", "MyMath", Math.class, defaultNameSource, 5);
		assertThat(countOccurrencesInString(result, "default ")).isEqualTo(NUMBER_OF_STATIC_METHODS_IN_MATH_CLASS);
		verifyConstantAndMethodCounts(2, NUMBER_OF_STATIC_METHODS_IN_MATH_CLASS);
	}

	private int countOccurrencesInString(String src, String target) {
		int count = 0;
		int tlen = target.length();
		char[] srcChars = src.toCharArray();
		char[] targetChars = target.toCharArray();
		for (int i = 0; i < srcChars.length; i++) {
			if (srcChars[i] == targetChars[0] && i + tlen < srcChars.length) {
				if (countMatchingChars(tlen, srcChars, targetChars, i) == tlen) {
					count++;
				}
			}
		}
		return count;
	}

	private int countMatchingChars(int tlen, char[] srcChars, char[] targetChars, int i) {
		int matchCount = 1;
		for (int j = 1; j < tlen && matchCount == j; j++) {
			if (srcChars[i + j] == targetChars[j]) {
				matchCount++;
			}
		}
		return matchCount;
	}

	@Test
	public void generating_method_should_increment_statistics() {
		callAndVerifyMakeDelegateMethod();
		GenerationStatistics stats = underTest.getStatistics();
		assertThat(stats.getConstantCount()).isEqualTo(0);
		assertThat(stats.getMethodCount()).isEqualTo(1);
		assertThat(stats.getDeprecationCount()).isEqualTo(0);
	}

	private void callAndVerifyMakeDelegateMethod() {
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());
		String result = underTest.makeDelegateMethod("whatever", verifyMethod.get(), new HashSet<String>(),
				defaultNameSource, " ");
		assertThat(result).contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);");
	}

	@Test
	public void generating_constant_should_increment_statistics() throws NoSuchFieldException, SecurityException {
		callAndVerifyGenerateConstant();
		verifyConstantAndMethodCounts(1, 0);
	}

	private void verifyConstantAndMethodCounts(int expectedConstantCount, int expectedMethodCount) {
		GenerationStatistics stats = underTest.getStatistics();
		assertThat(stats.getConstantCount()).isEqualTo(expectedConstantCount);
		assertThat(stats.getMethodCount()).isEqualTo(expectedMethodCount);
	}

	private void callAndVerifyGenerateConstant() throws NoSuchFieldException {
		StringBuilder buf = new StringBuilder();
		underTest.generateConstant(Math.class.getDeclaredField("PI"), Math.class, new HashSet<String>(), buf, "MyMath",
				"    ");
		assertThat(buf.toString()).contains("public static final double PI");
	}

	@Test
	public void can_reset_statistics() throws NoSuchFieldException, SecurityException {
		callAndVerifyGenerateConstant();
		callAndVerifyMakeDelegateMethod();
		verifyConstantAndMethodCounts(1, 1);
		underTest.resetStatistics();
		verifyAllCountsAreZero();

	}

	private void verifyAllCountsAreZero() {
		verifyConstantAndMethodCounts(0, 0);
		assertThat(underTest.getStatistics().getDeprecationCount()).isEqualTo(0);
		assertThat(underTest.getStatistics().getSkippedCount()).isEqualTo(0);
	}

	@Test
	public void can_track_deprecations() {
		callAndVerifyDeprecatedMethod();
		assertThat(underTest.getStatistics().getDeprecationCount()).isEqualTo(1);
	}

	@Test
	public void can_track_skipped_deprecated_methods() {
		underTest = new StatisticProvidingClassCodeGenerator(null, DeprecationPolicy.IGNORE_DEPRECATED_METHODS);
		String result = underTest.generateMethodsForClassUpdatingImports(java.net.URLEncoder.class, new HashSet<>(), 4,
				"Enc", new ArgumentNameSource() {
				});
		assertThat(underTest.getStatistics().getSkippedCount()).isEqualTo(1);
		assertThat(result)
				.contains("default String encode(String arg0, String arg1) throws UnsupportedEncodingException {");
	}

	private void callAndVerifyDeprecatedMethod() {
		Method depMethod = getAndVerifyDeprecatedMethod().get();
		assertThat(underTest.makeDelegateMethod("Enc", depMethod, new HashSet<>(), new ArgumentNameSource() {
		}, "    ")).contains(depMethod.getName());
	}

	private Optional<Method> getAndVerifyDeprecatedMethod() {
		Optional<Method> deprecatedMethod = underTest.listStaticMethodsForClass(java.net.URLEncoder.class).stream()
				.filter((Method m) -> m.getDeclaredAnnotationsByType(Deprecated.class).length > 0).findFirst();
		assertTrue(deprecatedMethod.isPresent());
		return deprecatedMethod;
	}
}
