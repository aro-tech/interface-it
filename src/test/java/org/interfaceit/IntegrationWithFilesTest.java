/**
 * 
 */
package org.interfaceit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.interfaceit.meta.arguments.ArgumentNameSource;
import org.interfaceit.meta.arguments.LookupArgumentNameSource;
import org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader;
import org.interfaceit.ui.commandline.CommandLineMain;
import org.interfaceit.util.FileUtils;
import org.interfaceit.util.SourceFileReader;
import org.interfaceit.util.mixin.AssertJ;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

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
	private SourceFileReader sourceReader = new FileUtils();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		tmpDir = new File("./tmp");
		examplesDir = new File("./examples");
		tmpDir.mkdirs();
		// imports = new HashSet<>();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		for (File f : tmpDir.listFiles()) {
			f.delete();
		}
		tmpDir.delete();
	}

	/**
	 * Test Java file generation
	 * 
	 * @throws IOException
	 */
	@Test
	public void can_write_mockito_to_file() throws IOException {
		File resultFile = underTest.generateClassToFile(tmpDir, "MockitoEnabled", Mockito.class, "org.interfaceit.test",
				new ArgumentNameSource() {
				}, 5);
		
		URL expectedURL = this.getClass().getResource("/MockitoEnabled.txt");
		File expected = new File(expectedURL.getPath());
		System.out.println(resultFile.getAbsolutePath());
		Assertions.assertThat(resultFile).exists().canRead();
		List<String> resultLines = sourceReader.readTrimmedLinesFromFilePath(resultFile.toPath());
		List<String> expectedLines = sourceReader.readTrimmedLinesFromFilePath(expected.toPath());
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

		final String packageName = "org.interfaceit.util.mixin";
		File resultFile;
		try {
			buildAndVerifyMockito(nameSource, packageName);

			resultFile = underTest.generateClassToFile(examplesDir, "AssertJ", org.assertj.core.api.Assertions.class,
					packageName, nameSource, 4);
			Assertions.assertThat(resultFile).exists().canRead();
			verifyCountOccurences(resultFile, 0, "arg0)");
			verifyCountOccurences(resultFile, 52, "        return Assertions.assertThat(actual);");

			generateClassFromCommandLineMainAndVerify(packageName, "java.lang.Math", "Math", examplesSourceZip);

			generateClassFromCommandLineMainAndVerify(packageName, "java.net.URLEncoder", "URLEncoding", getExampleSourceFile());

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
				nameSource, 4);
		Assertions.assertThat(resultFile).exists().canRead();
		verifyCountOccurences(resultFile, 0, "arg0)");
	}

	private void verifyCountOccurences(File resultFile, int expectedOcurrenceCount, final String expected) {
		Path path = FileSystems.getDefault().getPath(resultFile.getAbsolutePath());
		long count = 0;
		try {
			count = Files.lines(path).filter(str -> str.contains(expected)).count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertThat(count).as("Occurrence count of " + expectedOcurrenceCount + " for expected line: " + expected)
				.isEqualTo(expectedOcurrenceCount);
	}

	private LookupArgumentNameSource loadArgumentNames(File examplesZipFile) throws IOException {
		List<String> lines = sourceReader.readFilesInZipArchive(examplesZipFile, "org/mockito/Mockito.java",
				"org/mockito/Matchers.java", "org/assertj/core/api/Assertions.java");
		LookupArgumentNameSource nameSource = new LookupArgumentNameSource();
		new SourceLineReadingArgumentNameLoader().parseAndLoad(lines, nameSource);
		return nameSource;
	}

}
