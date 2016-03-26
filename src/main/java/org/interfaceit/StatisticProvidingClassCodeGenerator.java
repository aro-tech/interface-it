/**
 * 
 */
package org.interfaceit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.interfaceit.meta.arguments.ArgumentNameSource;

/**
 * Implementation of ClassCodeGenerator which provides statistics about the generated code
 * @author aro_tech
 *
 */
public class StatisticProvidingClassCodeGenerator extends DelegateMethodGenerator implements ClassCodeGenerator {
	private GenerationStatistics generationStatistics = new GenerationStatistics();
	
	public GenerationStatistics getStatistics() {
		return this.generationStatistics;
	}

	/* (non-Javadoc)
	 * @see org.interfaceit.DelegateMethodGenerator#makeDelegateMethod(java.lang.String, java.lang.reflect.Method, java.util.Set, org.interfaceit.meta.arguments.ArgumentNameSource, java.lang.String)
	 */
	@Override
	public String makeDelegateMethod(String targetInterfaceName, Method method, Set<String> importsOut,
			ArgumentNameSource argumentNameSource, String indentationUnit) {
		this.generationStatistics.incrementMethodCount();
		return super.makeDelegateMethod(targetInterfaceName, method, importsOut, argumentNameSource, indentationUnit);
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

	public void resetStatistics() {
		this.generationStatistics = new GenerationStatistics();
		
	}
	
	

}
