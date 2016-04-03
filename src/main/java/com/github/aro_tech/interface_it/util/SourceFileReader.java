package com.github.aro_tech.interface_it.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for reading source code from disk
 * @author aro_tech
 *
 */
public interface SourceFileReader {

	/**
	 * Extract the text lines from one or more files in a zip archive
	 * 
	 * @param zipFile
	 * @param filePathsWithinZipStructure
	 * @return Lines from all the files, in sequence
	 * @throws IOException
	 */
	List<String> readFilesInZipArchive(File zipFile, String... filePathsWithinZipStructure) throws IOException;

	/**
	 * 
	 * @param path
	 * @return Trimmed text lines from the given file
	 */
	List<String> readTrimmedLinesFromFilePath(Path path);

}