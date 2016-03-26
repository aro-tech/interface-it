/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;

import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.util.mixin.AllAssertions;
import org.interfaceit.util.mixin.Mockito;
import org.junit.Before;
import org.junit.Test;

/**
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
		GenerationStatistics stats = underTest.getStatistics();
		assertThat(countOccurrencesInString(result, "default ")).isEqualTo(NUMBER_OF_STATIC_METHODS_IN_MATH_CLASS);
		assertThat(stats.getConstantCount()).isEqualTo(2);
		assertThat(stats.getMethodCount()).isEqualTo(NUMBER_OF_STATIC_METHODS_IN_MATH_CLASS);

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
		Optional<Method> verifyMethod = underTest.listStaticMethodsForClass(org.mockito.Mockito.class).stream()
				.filter((Method m) -> "verify".equals(m.getName()) && m.getParameters().length == 2).findFirst();

		assertTrue(verifyMethod.isPresent());
		String result = underTest.makeDelegateMethod("whatever", verifyMethod.get(), new HashSet<String>(),
				defaultNameSource, " ");
		assertThat(result).contains("default <T> T verify(T arg0, VerificationMode arg1) {")
				.contains("return Mockito.verify(arg0, arg1);");
		GenerationStatistics stats = underTest.getStatistics();
		assertThat(stats.getConstantCount()).isEqualTo(0);
		assertThat(stats.getMethodCount()).isEqualTo(1);
	}

	@Test
	public void generating_constant_should_increment_statistics() throws NoSuchFieldException, SecurityException {
		StringBuilder buf = new StringBuilder();
		underTest.generateConstant(Math.class.getDeclaredField("PI"), Math.class, new HashSet<String>(), buf, "MyMath",
				"    ");
		assertThat(buf.toString()).contains("public static final double PI");
		GenerationStatistics stats = underTest.getStatistics();
		assertThat(stats.getConstantCount()).isEqualTo(1);
		assertThat(stats.getMethodCount()).isEqualTo(0);
	}

}
