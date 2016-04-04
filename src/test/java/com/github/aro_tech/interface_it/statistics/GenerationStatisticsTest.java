package com.github.aro_tech.interface_it.statistics;

import org.junit.Test;

import com.github.aro_tech.interface_it.util.mixin.AllAssertions;

public class GenerationStatisticsTest implements AllAssertions {
	GenerationStatistics underTest = new GenerationStatistics();

	@Test
	public void can_summarize_all_zero() {
		assertThat(underTest.summarizeStatistics()).isEqualTo("Generated 0 constants and 0 methods.");
	}

	@Test
	public void can_summarize_all_one() {
		increment(1, 1, 1, 1);
		assertThat(underTest.summarizeStatistics()).isEqualTo(
				"Generated 1 constant and 1 method." + System.lineSeparator() + "1 generated method is deprecated."
						+ System.lineSeparator() + "Skipped 1 static method because of deprecation policy.");
	}

	@Test
	public void can_summarize_mixed_values() {
		increment(8,5,3,2);
		assertThat(underTest.summarizeStatistics()).isEqualTo(
				"Generated 8 constants and 5 methods." + System.lineSeparator() + "3 generated methods are deprecated."
						+ System.lineSeparator() + "Skipped 2 static methods because of deprecation policy.");
	}

	
	private void increment(int constants, int methods, int deprecated, int skipped) {
		callNTimes(constants, () -> {
			underTest.incrementConstantCount();
		});
		callNTimes(methods, () -> {
			underTest.incrementMethodCount();
		});
		callNTimes(deprecated, () -> {
			underTest.incrementDeprecationCount();
		});
		callNTimes(skipped, () -> {
			underTest.incrementSkippedCount();
		});
	}

	private void callNTimes(int n, Runnable call) {
		for (int i = 0; i < n; i++) {
			call.run();
		}
	}
}
