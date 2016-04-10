/**
 * 
 */
package com.github.aro_tech.interface_it.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import com.github.aro_tech.interface_it.format.CodeFormatter;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.policy.DeprecationPolicy;
import com.github.aro_tech.interface_it.statistics.GenerationStatistics;
import com.github.aro_tech.interface_it.util.FileSystem;

/**
 * Implementation of MixinCodeGenerator which provides statistics about the generated code
 * @author aro_tech
 *
 */
public class StatisticProvidingMixinGenerator extends CoreMixinGenerator implements MixinCodeGenerator, StatisticsProvider {
	private GenerationStatistics generationStatistics = new GenerationStatistics();	
	
	/**
	 * 
	 * Constructor
	 */
	public StatisticProvidingMixinGenerator() {
		super();
	}
	
	


	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.api.CoreMixinGenerator#generateDelegateClassCode(java.lang.Class, com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource, com.github.aro_tech.interface_it.api.MultiFileOutputOptions)
	 */
	@Override
	public String generateDelegateClassCode(Class<?> delegateClass, ArgumentNameSource argumentNameSource,
			MultiFileOutputOptions options) {
		this.setCurrentTag(options.getTargetInterfaceNameForDelegate(delegateClass) + ".java");
		return super.generateDelegateClassCode(delegateClass, argumentNameSource, options);
	}



	/**
	 * 
	 * Constructor
	 * @param fileSystem
	 * @param deprecationPolicy
	 * @param formatter
	 */
	public StatisticProvidingMixinGenerator(FileSystem fileSystem, DeprecationPolicy deprecationPolicy,
			CodeFormatter formatter) {
		super(fileSystem, deprecationPolicy, formatter);
	}



	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.StatisticsProvider#getStatistics()
	 */
	@Override
	public GenerationStatistics getStatistics() {
		return this.generationStatistics;
	}

	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.CoreMixinGenerator#makeDelegateMethod(java.lang.String, java.lang.reflect.Method, java.util.Set, com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource, java.lang.String)
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
	 * @see com.github.aro_tech.interface_it.CoreMixinGenerator#deprecationPolicyDoesNotForbid(java.lang.reflect.Method)
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
	 * @see com.github.aro_tech.interface_it.CoreMixinGenerator#generateConstant(java.lang.reflect.Field, java.lang.Class, java.util.Set, java.lang.StringBuilder, java.lang.String, java.lang.String)
	 */
	@Override
	protected void generateConstant(Field field, Class<?> fieldClass, Set<String> imports, StringBuilder buf,
			String targetInterfaceName, String indentationUnit) {
		this.generationStatistics.incrementConstantCount();
		super.generateConstant(field, fieldClass, imports, buf, targetInterfaceName, indentationUnit);
	}

	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.StatisticsProvider#resetStatistics()
	 */
	@Override
	public void resetStatistics() {
		this.generationStatistics = new GenerationStatistics();
		
	}


	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.api.StatisticsProvider#getStatisticsFor(java.lang.String)
	 */
	@Override
	public GenerationStatistics getStatisticsFor(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.github.aro_tech.interface_it.api.StatisticsProvider#setCurrentTag(java.lang.String)
	 */
	@Override
	public void setCurrentTag(String tag) {
		// TODO Auto-generated method stub
		
	}

}
