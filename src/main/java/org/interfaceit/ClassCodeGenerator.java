package org.interfaceit;

import java.io.File;
import java.io.IOException;

import org.interfaceit.meta.arguments.ArgumentNameSource;

/**
 * Interface which creates and saves to disk a wrapper class
 * @author aro_tech
 *
 */
public interface ClassCodeGenerator {
	
	/**
	 * The version to display
	 */
	String PRODUCT_VERSION = "0.6.0";

	/**
	 * Generate and write to file a delegate wrapping mix-in interface
	 * 
	 * @param dir
	 *            The directory where the file will be saved
	 * @param targetInterfaceName
	 *            the name of the Jave interface (which is also the file name,
	 *            without the ".java" suffix
	 * @param delegateClass
	 *            The class whose static methods will be called by the mix-in
	 *            interface
	 * @param targetPackageName
	 *            package name for the mix-in interface
	 * @param argumentNameSource Source of names for method arguments
	 * @param indentationSpaces
	 *            Formatting for the generated Java code
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass, String targetPackageName,
			ArgumentNameSource argumentNameSource, int indentationSpaces) throws IOException;

	/**
	 * Generate and write to file a delegate wrapping mix-in interface Uses
	 * default indentation
	 * 
	 * @param dir
	 *            The directory where the file will be saved
	 * @param targetInterfaceName
	 *            the name of the Jave interface (which is also the file name,
	 *            without the ".java" suffix
	 * @param delegateClass
	 *            The class whose static methods will be called by the mix-in
	 *            interface
	 * @param targetPackageName
	 *            package name for the mix-in interface
	 * @param argumentNameSource Source of names for method arguments
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass, String targetPackageName, ArgumentNameSource argumentNameSource)
			throws IOException;

}