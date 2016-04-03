package com.github.aro_tech.interface_it.api;

import java.io.File;
import java.io.IOException;

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
	String PRODUCT_VERSION = "1.0.1-SNAPSHOT";

	/**
	 * Generate and write to file a delegate wrapping mix-in interface
	 * 
	 * @param saveDirectory
	 *            The directory where the file will be saved
	 * @param targetInterfaceName
	 *            the name of the Jave interface (which is also the file name,
	 *            without the ".java" suffix
	 * @param delegateClass
	 *            The class whose static methods will be called by the mix-in
	 *            interface
	 * @param targetPackageName
	 *            package name for the mix-in interface
	 * @param argumentNameSource
	 *            Source of names for method arguments
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateMixinJavaFile(File saveDirectory, String targetInterfaceName, Class<?> delegateClass, String targetPackageName,
			ArgumentNameSource argumentNameSource) throws IOException;

}