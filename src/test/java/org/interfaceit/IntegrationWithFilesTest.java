/**
 * 
 */
package org.interfaceit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
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
public class IntegrationWithFilesTest {

	private DelegateMethodGenerator underTest = new DelegateMethodGenerator();
	// private Set<String> imports;
	private static File tmpDir;
	private static File examplesDir;

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
	 * @throws IOException 
	 */
	@Test
	public void can_write_mockito_to_file() throws IOException {
		File resultFile = underTest.generateClassToFile(tmpDir, "MockitoEnabled", Mockito.class, "org.interfaceit.test",
				5);

		URL expectedURL = this.getClass().getResource("/MockitoEnabled.txt");
		File expected = new File(expectedURL.getPath());
		// System.out.println(resultFile.getAbsolutePath());
		Assertions.assertThat(resultFile).exists().canRead();
		List<String> resultLines = readTrimmedLines(resultFile.toPath());
		List<String> expectedLines = readTrimmedLines(expected.toPath());
		Assertions.assertThat(resultLines).hasSameSizeAs(expectedLines).containsAll(expectedLines);
	}

	/**
	 * Overwrites auto-generated examples stored in Git
	 * @throws IOException 
	 */
	@Test
	public void build_examples() throws IOException {
		final String packageName = "org.example";
		File resultFile;
		try {
			resultFile = underTest.generateClassToFile(examplesDir, "Mockito", org.mockito.Mockito.class, packageName,
					4);
			Assertions.assertThat(resultFile).exists().canRead();

			resultFile = underTest.generateClassToFile(examplesDir, "AssertJ", org.assertj.core.api.Assertions.class,
					packageName, 4);
			Assertions.assertThat(resultFile).exists().canRead();

			resultFile = underTest.generateClassToFile(examplesDir, "Math", java.lang.Math.class, packageName, 4);
			Assertions.assertThat(resultFile).exists().canRead();
			//System.out.println(resultFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	private static List<String> readTrimmedLines(Path path) {
		try {
			return Files.lines(path).filter(s -> s.trim().length() > 0).map(s -> s.trim()).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

}
