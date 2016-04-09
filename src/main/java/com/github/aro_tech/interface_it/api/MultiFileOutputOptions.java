package com.github.aro_tech.interface_it.api;

import java.io.File;

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
	String getTargetInterfaceNameForDelegate(Class<?> delegateClass);

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

}
