/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.interfaceit.format.CodeFormatter;
import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.statistics.GenerationStatistics;
import org.interfaceit.statistics.StatisticsProvider;
import org.interfaceit.util.FileSystem;

/**
 * Implementation of ClassCodeGenerator which provides statistics about the generated code
 * @author aro_tech
 *
 */
public class StatisticProvidingClassCodeGenerator extends DelegateMethodGenerator implements ClassCodeGenerator, StatisticsProvider {
	private GenerationStatistics generationStatistics = new GenerationStatistics();	
	
	/**
	 * 
	 * Constructor
	 */
	public StatisticProvidingClassCodeGenerator() {
		super();
	}


	/**
	 * 
	 * Constructor
	 * @param fileSystem
	 * @param deprecationPolicy
	 * @param formatter
	 */
	public StatisticProvidingClassCodeGenerator(FileSystem fileSystem, DeprecationPolicy deprecationPolicy,
			CodeFormatter formatter) {
		super(fileSystem, deprecationPolicy, formatter);
	}



	/* (non-Javadoc)
	 * @see org.interfaceit.StatisticsProvider#getStatistics()
	 */
	@Override
	public GenerationStatistics getStatistics() {
		return this.generationStatistics;
	}

	/* (non-Javadoc)
	 * @see org.interfaceit.DelegateMethodGenerator#makeDelegateMethod(java.lang.String, java.lang.reflect.Method, java.util.Set, org.interfaceit.meta.arguments.ArgumentNameSource, java.lang.String)
	 */
	@Override
	public String makeDelegateMethod(String targetInterfaceName, Method method, Set<String> importsOut,
			ArgumentNameSource argumentNameSource) {
		this.generationStatistics.incrementMethodCount();
		if(isDeprecated(method)) {
			this.generationStatistics.incrementDeprecationCount();
		}
		return super.makeDelegateMethod(targetInterfaceName, method, importsOut, argumentNameSource);
	}
	
	

	/* (non-Javadoc)
	 * @see org.interfaceit.DelegateMethodGenerator#deprecationPolicyDoesNotForbid(java.lang.reflect.Method)
	 */
	@Override
	protected boolean deprecationPolicyDoesNotForbid(Method method) {
		boolean notBlocked = super.deprecationPolicyDoesNotForbid(method);
		incrementSkippedCountIfBlocked(notBlocked);
		return notBlocked;
	}

	private void incrementSkippedCountIfBlocked(boolean notBlocked) {
		if(!notBlocked) {
			this.generationStatistics.incrementSkippedCount();
		}
	}

	/* (non-Javadoc)
	 * @see org.interfaceit.DelegateMethodGenerator#generateConstant(java.lang.reflect.Field, java.lang.Class, java.util.Set, java.lang.StringBuilder, java.lang.String, java.lang.String)
	 */
	@Override
	protected void generateConstant(Field field, Class<?> fieldClass, Set<String> imports, StringBuilder buf,
			String targetInterfaceName, String indentationUnit) {
		this.generationStatistics.incrementConstantCount();
		super.generateConstant(field, fieldClass, imports, buf, targetInterfaceName, indentationUnit);
	}

	/* (non-Javadoc)
	 * @see org.interfaceit.StatisticsProvider#resetStatistics()
	 */
	@Override
	public void resetStatistics() {
		this.generationStatistics = new GenerationStatistics();
		
	}
	
	

}
