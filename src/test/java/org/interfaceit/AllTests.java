package org.interfaceit;

import org.interfaceit.meta.arguments.LookupArgumentNameSourceTest;
import org.interfaceit.ui.commandline.ArgumentParserTest;
import org.interfaceit.ui.commandline.CommandLineMainTest;
import org.interfaceit.util.ClassNameUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * All Unit tests, including integration tests
 * 
 * @author aro_tech
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ DelegateMethodGeneratorTest.class, IntegrationWithFilesTest.class, ClassNameUtilsTest.class,
		ArgumentParserTest.class, CommandLineMainTest.class, LookupArgumentNameSourceTest.class })
public class AllTests {

}
