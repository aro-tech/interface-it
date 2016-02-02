package org.interfaceit;

import org.interfaceit.util.ClassNameUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Unit tests, including integration tests
 * @author aro_tech
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ DelegateMethodGeneratorTest.class, IntegrationTestsWithFiles.class, ClassNameUtilsTest.class })
public class AllTests {

}
