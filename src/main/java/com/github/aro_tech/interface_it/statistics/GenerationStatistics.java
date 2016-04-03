/**
 * 
 */
package com.github.aro_tech.interface_it.statistics;

/**
 * Statistics from a run of code generation
 * 
 * @author aro_tech
 *
 */
public class GenerationStatistics {
	private int constantCount = 0;
	private int methodCount = 0;
	private int deprecationCount = 0;
	private int skippedCount = 0;

	/**
	 * 
	 * @return The number of constants generated
	 */
	public int getConstantCount() {
		return constantCount;
	}

	/**
	 * 
	 * @return The number of methods generated
	 */
	public int getMethodCount() {
		return methodCount;
	}

	/**
	 * Add 1 to the method count
	 */
	public void incrementMethodCount() {
		this.methodCount++;
	}

	/**
	 * Add 1 to the constant count
	 */
	public void incrementConstantCount() {
		this.constantCount++;
	}

	/**
	 * 
	 * @return The number of methods generated which are deprecated
	 */
	public int getDeprecationCount() {
		return deprecationCount;
	}

	/**
	 * Add 1 to the deprecation count
	 */
	public void incrementDeprecationCount() {
		this.deprecationCount++;
	}

	/**
	 * 
	 * @return The number of static methods which were skipped (not wrapped)
	 *         because of deprecation policy
	 */
	public int getSkippedCount() {
		return skippedCount;
	}
	
	/**
	 * Add 1 to the skipped count
	 */
	public void incrementSkippedCount() {
		this.skippedCount++;
	}

}
