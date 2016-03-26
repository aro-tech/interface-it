/**
 * 
 */
package org.interfaceit.statistics;

/**
 * Statistics from a run of code generation
 * @author aro_tech
 *
 */
public class GenerationStatistics {
	private int constantCount = 0;
	private int methodCount = 0;

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
}
