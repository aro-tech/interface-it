package com.github.aro_tech.interface_it.api;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Interface specifying preference information for the generation of multiple
 * mixin interface files
 * 
 * @author aro_tech
 *
 */
public interface MultiFileOutputOptions {

	/**
	 * Get the name of the mixin interface to generate for the given delegate
	 * class
	 * 
	 * @param delegateClass
	 * @return The name of the mixin
	 */
	default String getTargetInterfaceNameForDelegate(Class<?> delegateClass) {
		return delegateClass.getSimpleName() + "Mixin";
	}

	/**
	 * Get the name of the package where the mixin interface will be generated
	 * for the given delegate class
	 * 
	 * @param delegateClass
	 * @return The package name of the mixin
	 */
	String getTargetPackageNameForDelegate(Class<?> delegateClass);

	/**
	 * Get the directory where the mixin generated for the given delegate class
	 * will be written
	 * 
	 * @param delegate
	 * @return The output directory
	 */
	File getMixinSaveDirectoryForDelegate(Class<?> delegate);

	/**
	 * Predicate to do additional filtering of static methods to be processed
	 * Does not affect filtering out of non-static methods or deprecated methods
	 * @param delegate
	 * 
	 * @return Predicate to be applied
	 */
	default Predicate<? super Method> getMethodFilterForDelegate(Class<?> delegate) {
		return m -> true; // by default, no additional filtering
	}

	/**
	 * @param delegateClass 
	 * @return Set of all the supertypes to list in the "extends" clause of the
	 *         generated mixin interface declaration
	 */
	default Set<String> getSuperTypes(Class<?> delegateClass) {
		return Collections.emptySet();
	}

	/**
	 * Predicate to do additional filtering of constants to be processed
	 * Does not affect filtering out of non-static members
	 * @param delegate
	 * 
	 * @return Predicate to be applied
	 */
	default Predicate<? super Field> getConstantsFilterForDelegate(Class<?> delegateClass) {
		return m -> true; // by default, no additional filtering
	}

}
