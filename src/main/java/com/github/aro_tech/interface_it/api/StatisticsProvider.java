package com.github.aro_tech.interface_it.api;

import com.github.aro_tech.interface_it.statistics.GenerationStatistics;

/**
 * Interface for provider of statistics about mixin generation Translation:
 * Gives you stats to show what interface-it did
 * 
 * @author aro_tech
 *
 */
public interface StatisticsProvider {

	/**
	 * 
	 * @return Global generation statistics
	 */
	GenerationStatistics getStatistics();

	/**
	 * Set all statistics values to 0 or defaults and delete any tags
	 */
	void resetStatistics();

	/**
	 * Will start collection of statistics for the given tag and stop colection
	 * for any previous tag. Global statistics are unaffected.
	 * 
	 * 
	 * @param tag
	 */
	void setCurrentTag(String tag);

	/**
	 * Get statistics for a particular tag
	 * 
	 * @param tag
	 * @return The statistics for the given tag, global statistics if the tag is
	 *         not found
	 */
	GenerationStatistics getStatisticsFor(String tag);

}