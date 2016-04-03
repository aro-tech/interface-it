package com.github.aro_tech.interface_it.api;

import com.github.aro_tech.interface_it.statistics.GenerationStatistics;

public interface StatisticsProvider {

	GenerationStatistics getStatistics();

	void resetStatistics();

}