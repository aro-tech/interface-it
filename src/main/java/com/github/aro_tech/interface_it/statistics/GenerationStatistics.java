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

	/**
	 * Generate a textual summary of the statistics
	 * 
	 * @return
	 */
	public String summarizeStatistics() {
		StringBuilder buf = new StringBuilder();
		buf.append("Generated ").append(getConstantCount()).append(" constant").append(makePluralText(getConstantCount())).append(" and "
				+ getMethodCount()).append(" method").append(makePluralText(getMethodCount())).append(".");
		summarizeDeprecationPolicyResults(buf);
		return buf.toString();
	}

	private String makePluralText(int count) {
		if (count == 1) {
			return "";
		}
		return "s";
	}

	private void summarizeDeprecationPolicyResults(StringBuilder buf) {
		writeDeprecationCountIfNonZero(buf);
		writeSkippedCountIfNonZero(buf);

	}

	private void writeDeprecationCountIfNonZero(StringBuilder buf) {
		if (getDeprecationCount() > 0) {
			buf.append(System.lineSeparator());
			if (getDeprecationCount() > 1) {
				buf.append(getDeprecationCount()).append(" generated methods are deprecated.");
			} else {
				buf.append("1 generated method is deprecated.");
			}
		}
	}

	private void writeSkippedCountIfNonZero(StringBuilder buf) {
		if (getSkippedCount() == 1) {
			buf.append(System.lineSeparator());
			buf.append("Skipped 1 static method because of deprecation policy.");
		} else if (getSkippedCount() > 1) {
			buf.append(System.lineSeparator());
			buf.append("Skipped ").append(getSkippedCount()).append(" static methods because of deprecation policy.");
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenerationStatistics [constantCount=" + constantCount + ", methodCount=" + methodCount
				+ ", deprecationCount=" + deprecationCount + ", skippedCount=" + skippedCount + "]";
	}
	
	

}
