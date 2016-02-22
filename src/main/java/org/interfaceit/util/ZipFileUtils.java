/**
 * 
 */
package org.interfaceit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class for reading zipped files
 * @author aro_tech
 *
 */
public class ZipFileUtils {

	/**
	 * @param zipFilePath
	 * @param filePathWithinZipStructure
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFileInZipArchive(File zipFile, String filePathWithinZipStructure)
			throws IOException {
		List<String> lines = new ArrayList<>();

		try(ZipFile zf = new ZipFile(zipFile)) {
			ZipEntry entry = zf.getEntry(filePathWithinZipStructure);
			if (null != entry) {
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
		}
		return lines;
	}
}
