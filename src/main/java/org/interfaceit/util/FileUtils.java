/**
 * 
 */
package org.interfaceit.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.interfaceit.ui.meta.error.UnableToCreateOutputDirectory;

/**
 * Class for reading source files
 * 
 * @author aro_tech
 *
 */
public class FileUtils implements SourceFileReader, FileSystem {
	
	/**
	 * Static factory method
	 * @return The default FileSystem
	 */
	public static FileSystem getDefaultFileSystem() {
		return new FileUtils();
	}

	/**
	 * Static factory method
	 * @return The default SourceFileReader
	 */
	public static SourceFileReader getDefaultSourceFileReader() {
		return new FileUtils();
	}
	
	
	/**
	 * 
	 * Constructor
	 */
	protected FileUtils() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.interfaceit.util.SourceFileReader#readFilesInZipArchive(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public List<String> readFilesInZipArchive(File zipFile, String... filePathsWithinZipStructure) throws IOException {
		List<String> lines = new ArrayList<>();
		try (ZipFile zf = new ZipFile(zipFile)) {
			for (String filePathWithinZipStructure : filePathsWithinZipStructure) {
				readFileLinesIntoList(filePathWithinZipStructure, lines, zf);
			}
		}
		return lines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.interfaceit.util.SourceFileReader#readTrimmedLinesFromFilePath(java.
	 * nio.file.Path)
	 */
	@Override
	public List<String> readTrimmedLinesFromFilePath(Path path) {
		try {
			return Files.lines(path).filter(s -> s.trim().length() > 0).map(s -> s.trim()).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	private void readFileLinesIntoList(String filePathWithinZipStructure, List<String> lines, ZipFile zf)
			throws IOException {
		ZipEntry entry = zf.getEntry(filePathWithinZipStructure);
		if (null != entry) {
			readEntryLinesIntoList(lines, zf, entry);
		}
	}

	private void readEntryLinesIntoList(List<String> lines, ZipFile zf, ZipEntry entry) throws IOException {
		long size = entry.getSize();
		if (size > 0) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(entry)))) {
				String line;
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.interfaceit.FileSystem#writeFile(java.io.File, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public File writeFile(File dir, String fileName, String content) throws IOException {
		File fileToWrite = new File(dir, fileName);
		try (BufferedWriter w = new BufferedWriter(new FileWriter(fileToWrite))) {
			w.write(content);
		}
		return fileToWrite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.interfaceit.FileSystem#makeOutputDirectoryIfAbsent(java.io.File)
	 */
	@Override
	public void makeOutputDirectoryIfAbsent(File dir) throws UnableToCreateOutputDirectory {
		if (!dir.exists()) {
			makeAbsentDirectory(dir);
		}
	}

	private void makeAbsentDirectory(File dir) throws UnableToCreateOutputDirectory {
		if (!dir.mkdirs()) {
			throw new UnableToCreateOutputDirectory(dir);
		}
	}

}
