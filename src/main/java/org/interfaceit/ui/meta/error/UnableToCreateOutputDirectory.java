/**
 * 
 */
package org.interfaceit.ui.meta.error;

import java.io.File;
import java.io.IOException;

/**
 * IOException specifying failure of directory creation
 * 
 * @author aro_tech
 *
 */
public class UnableToCreateOutputDirectory extends IOException {
	private static final long serialVersionUID = 1L;
	private final File targetDirectory;

	/**
	 * 
	 * Constructor
	 * 
	 * @param targetDirectory
	 *            The directory for which creation failed
	 */
	public UnableToCreateOutputDirectory(File targetDirectory) {
		super("Unable to create directory: " + targetDirectory.getAbsolutePath());
		this.targetDirectory = targetDirectory;
	}

	/**
	 * @return the targetDirectory
	 */
	public File getTargetDirectory() {
		return targetDirectory;
	}

	/**
	 * 
	 * @return The absolute path of the target directory or the String "<NULL>"
	 *         if it's null.
	 */
	public String getTargetDirectoryPath() {
		if (null == targetDirectory) {
			return "<NULL>";
		}
		return targetDirectory.getAbsolutePath();
	}

}
