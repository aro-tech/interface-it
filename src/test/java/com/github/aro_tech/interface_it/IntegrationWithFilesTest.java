/**
 * 
 */
package com.github.aro_tech.interface_it;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.aro_tech.interface_it.ClassCodeGenerator;
import com.github.aro_tech.interface_it.DelegateMethodGenerator;
import com.github.aro_tech.interface_it.DeprecationPolicy;
import com.github.aro_tech.interface_it.format.CodeFormatter;
import com.github.aro_tech.interface_it.meta.arguments.ArgumentNameSource;
import com.github.aro_tech.interface_it.meta.arguments.LookupArgumentNameSource;
import com.github.aro_tech.interface_it.meta.arguments.SourceLineReadingArgumentNameLoader;
import com.github.aro_tech.interface_it.ui.commandline.CommandLineMain;
import com.github.aro_tech.interface_it.util.FileUtils;
import com.github.aro_tech.interface_it.util.SourceFileReader;
import com.github.aro_tech.interface_it.util.mixin.AssertJ;

/**
 * Integration tests using files
 * 
 * @author aro_tech
 *
 */
public class IntegrationWithFilesTest implements AssertJ {

	private ClassCodeGenerator underTest = new DelegateMethodGenerator();
	private static File tmpDir;
	private static File examplesDir;
	private SourceFileReader sourceReader = FileUtils.getDefaultSourceFileReader();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		tmpDir = new File("./tmp");
		examplesDir = new File("./examples");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		for (File f : tmpDir.listFiles()) {
			f.delete();
		}
		if (!tmpDir.delete()) {
			System.out.println("In post-test clean-up, unable to delete directory: " + tmpDir.getCanonicalPath()
					+ " - Please delete it manually.");
		}
	}

	/**
	 * Test Java file generation
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_write_mockito_to_file() throws IOException {
		File resultFile = new DelegateMethodGenerator(FileUtils.getDefaultFileSystem(),
				DeprecationPolicy.PROPAGATE_DEPRECATION, new CodeFormatter(5)).generateClassToFile(tmpDir,
						"MockitoEnabled", Mockito.class, "com.github.aro_tech.interface_it.test", new ArgumentNameSource() {
						});

		URL expectedURL = this.getClass().getResource("/MockitoEnabled.txt");
		File expected = new File(expectedURL.getPath());
		System.out.println(resultFile.getAbsolutePath());
		Assertions.assertThat(resultFile).exists().canRead();
		List<String> resultLines = readFileLines(resultFile);
		List<String> expectedLines = readFileLines(expected);

		Assertions.assertThat(resultLines).hasSameSizeAs(expectedLines).containsAll(expectedLines);
	}

	@Test
	public void can_read_zip_file_lines() throws IOException {
		URL testZipURL = this.getClass().getResource("/test.zip");
		File testZip = new File(testZipURL.getPath());
		List<String> lines = sourceReader.readFilesInZipArchive(testZip, "a/b/ziptest.txt");
		assertThat(lines).contains("Test line 1", "Test line 2");
	}

	@Test
	public void can_handle_reading_nonexistent_file_in_zip() throws IOException {
		URL testZipURL = this.getClass().getResource("/test.zip");
		File testZip = new File(testZipURL.getPath());
		List<String> lines = sourceReader.readFilesInZipArchive(testZip, "c/d/bogus.txt");
		assertThat(lines).isEmpty();
	}

	/**
	 * Overwrites auto-generated examples stored in Git
	 * 
	 * @throws IOException
	 */
	@Test
	public void build_examples() throws IOException {
		File examplesSourceZip = getExamplesZipFile();
		LookupArgumentNameSource nameSource = loadArgumentNames(examplesSourceZip);

		final String packageName = "com.github.aro_tech.interface_it.util.mixin";
		File resultFile;
		try {
			buildAndVerifyMockito(nameSource, packageName);

			resultFile = underTest.generateClassToFile(examplesDir, "AssertJ", org.assertj.core.api.Assertions.class,
					packageName, nameSource);
			Assertions.assertThat(resultFile).exists().canRead();
			verifyCountOccurences(resultFile, 0, "arg0)");
			verifyCountOccurences(resultFile, 52, "        return Assertions.assertThat(actual);");

			generateClassFromCommandLineMainAndVerify(packageName, "java.lang.Math", "Math", examplesSourceZip);

			generateClassFromCommandLineMainAndVerify(packageName, "java.net.URLEncoder", "URLEncoding",
					getExampleSourceFile());

			generateClassFromCommandLineMainAndVerify(packageName, "org.junit.Assert", "JUnitAssert",
					getExampleSourceFile());

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private File getExampleSourceFile() {
		return getResourceFile("/exampleSrc.txt");
	}

	private File getResourceFile(String fileName) {
		URL url = this.getClass().getResource(fileName);
		File file = new File(url.getPath());
		return file;
	}

	private File getExamplesZipFile() {
		return getResourceFile("/examples.zip");
	}

	private void generateClassFromCommandLineMainAndVerify(final String packageName, String targetClass,
			String generatedClassName, File sourcePath) {
		File resultFile;
		String[] args = { "-d", examplesDir.getAbsolutePath(), "-n", generatedClassName, "-c", targetClass, "-p",
				packageName, "-s", sourcePath.getAbsolutePath() };
		CommandLineMain.main(args);
		resultFile = new File(examplesDir.getAbsolutePath() + "/" + generatedClassName + ".java");
		Assertions.assertThat(resultFile).exists().canRead();
		verifyCountOccurences(resultFile, 0, "arg0)");
	}

	private void buildAndVerifyMockito(LookupArgumentNameSource nameSource, final String packageName)
			throws IOException {
		File resultFile;
		resultFile = underTest.generateClassToFile(examplesDir, "Mockito", org.mockito.Mockito.class, packageName,
				nameSource);
		Assertions.assertThat(resultFile).exists().canRead();
		verifyCountOccurences(resultFile, 0, "arg0)");
	}

	private void verifyCountOccurences(File resultFile, int expectedOcurrenceCount, final String expected) {
		long count = 0;
		try {
			count = readFileLines(resultFile).stream().filter(str -> str.contains(expected)).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertThat(count).as("Occurrence count of " + expectedOcurrenceCount + " for expected line: " + expected)
				.isEqualTo(expectedOcurrenceCount);
	}

	private static List<String> readFileLines(File file) throws FileNotFoundException, IOException {
		String line = null;
		List<String> lines = new ArrayList<>();
		try (InputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}

	private LookupArgumentNameSource loadArgumentNames(File examplesZipFile) throws IOException {
		List<String> lines = sourceReader.readFilesInZipArchive(examplesZipFile, "org/mockito/Mockito.java",
				"org/mockito/Matchers.java", "org/assertj/core/api/Assertions.java");
		LookupArgumentNameSource nameSource = new LookupArgumentNameSource();
		new SourceLineReadingArgumentNameLoader().parseAndLoad(lines, nameSource);
		return nameSource;
	}

}