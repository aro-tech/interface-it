package org.interfaceit;

import java.io.File;
import java.io.IOException;

/**
 * Interface which creates and saves to disk a wrapper class
 * @author aro_tech
 *
 */
public interface ClassCodeGenerator {
	
	/**
	 * The version to display
	 */
	String PRODUCT_VERSION = "0.1";

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
	 * @param indentationSpaces
	 *            Formatting for the generated Java code
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass, String targetPackageName,
			int indentationSpaces) throws IOException;

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
	 * @return The .java file which was written
	 * @throws IOException
	 */
	File generateClassToFile(File dir, String targetInterfaceName, Class<?> delegateClass, String targetPackageName)
			throws IOException;

}