/**
 * 
 */
package com.github.aro_tech.interface_it.ui.meta.error;

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
		super("Unable to create directory: " + safeGetPath(targetDirectory));
		this.targetDirectory = targetDirectory;
	}

	/**
	 * 
	 * @return The absolute path of the target directory or the String "<NULL>"
	 *         if it's null.
	 */
	public String getTargetDirectoryPath() {
		return safeGetPath(targetDirectory);
	}

	private static String safeGetPath(File file) {
		if (null == file) {
			return "<NULL>";
		}
		return file.getAbsolutePath();
	}

}
