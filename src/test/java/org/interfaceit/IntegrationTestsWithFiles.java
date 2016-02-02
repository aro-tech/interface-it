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
 * @author arothkopf
 *
 */
public class IntegrationTestsWithFiles {

	private DelegateMethodGenerator underTest = new DelegateMethodGenerator();
	// private Set<String> imports;
	private static File dir;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		dir = new File("./tmp");
		dir.mkdirs();
		// imports = new HashSet<>();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		dir.delete();
	}

	/**
	 */
	@Test
	public void can_write_mockito_to_file() {
		File resultFile = underTest.generateClassToFile(dir, "MockitoEnabled", Mockito.class, "org.interfaceit.test",
				5);

		URL expectedURL = this.getClass().getResource("/MockitoEnabled.txt");
		File expected = new File(expectedURL.getPath());
		//System.out.println(resultFile.getAbsolutePath());
		Assertions.assertThat(resultFile).exists().canRead();
		List<String> resultLines = readTrimmedLines(resultFile.toPath());
		List<String> expectedLines = readTrimmedLines(expected.toPath());
		Assertions.assertThat(resultLines).hasSameSizeAs(expectedLines).containsAll(expectedLines);
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
