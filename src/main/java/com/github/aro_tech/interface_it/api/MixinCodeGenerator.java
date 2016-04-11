package com.github.aro_tech.interface_it.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;

/**
 * Interface which creates and saves to disk a wrapper interface
 * 
 * @author aro_tech
 *
 */
public interface MixinCodeGenerator {

	/**
	 * The version to display
	 */
	String PRODUCT_VERSION = "1.1.0";

	/**
	 * Generate and write to file a delegate wrapping mix-in interface
	 * 
	 * @param saveDirectory
	 *            The directory where the generated file will be saved
	 * @param targetInterfaceName
	 *            the name of the Jave interface (which is also the file name,
	 *            without the ".java" suffix
	 * @param delegateClass
	 *            The class whose static methods will be called by the generated
	 *            mix-in interface
	 * @param targetPackageName
	 *            package name for the generated mix-in interface
	 * @param argumentNameSource
	 *            Source of names for method arguments
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateMixinJavaFile(File saveDirectory, String targetInterfaceName, Class<?> delegateClass,
			String targetPackageName, ArgumentNameSource argumentNameSource) throws IOException;

	/**
	 * Generate and write multiple wrapping mix-in interface files
	 * 
	 * @param argumentNameSource
	 *            Source of names for method arguments
	 * @param delegateClasses
	 *            Classes whose static methods will be called by the generated
	 *            mix-in interfaces
	 * @return The .java file which was written
	 * @throws IOException
	 */
	List<File> generateMixinJavaFiles(MultiFileOutputOptions options, ArgumentNameSource argumentNameSource,
			Class<?>... delegateClasses) throws IOException;

}