package com.github.aro_tech.interface_it.util;

import java.io.File;
import java.io.IOException;

import com.github.aro_tech.interface_it.ui.meta.error.UnableToCreateOutputDirectory;

public interface FileSystem {

	/**
	 * Write the given content to a file with the given name in the given directory
	 * @param dir
	 * @param fileName
	 * @param content
	 * @return The File object representing the file which was written
	 * @throws IOException
	 */
	File writeFile(File dir, String fileName, String content) throws IOException;

	/**
	 * If dir does not exist, create it and any parent directories needed
	 * 
	 * @param dir
	 *            The target directory
	 * @throws UnableToCreateOutputDirectory
	 */
	void makeOutputDirectoryIfAbsent(File dir) throws UnableToCreateOutputDirectory;

}