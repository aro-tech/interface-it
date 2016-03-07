package org.interfaceit.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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

	List<String> readTrimmedLinesFromFilePath(Path path);

}