package com.github.aro_tech.interface_it;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.aro_tech.interface_it.format.CodeFormatterTest;
import com.github.aro_tech.interface_it.meta.arguments.LookupArgumentNameSourceTest;
import com.github.aro_tech.interface_it.meta.arguments.SourceLineReadingArgumentNameLoaderTest;
import com.github.aro_tech.interface_it.ui.commandline.ArgumentParserTest;
import com.github.aro_tech.interface_it.ui.commandline.CommandLineMainTest;
import com.github.aro_tech.interface_it.util.ClassNameUtilsTest;

/**
 * All Unit tests, including integration tests
 * 
 * @author aro_tech
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ DelegateMethodGeneratorTest.class, IntegrationWithFilesTest.class, ClassNameUtilsTest.class,
		ArgumentParserTest.class, CommandLineMainTest.class, LookupArgumentNameSourceTest.class,
		SourceLineReadingArgumentNameLoaderTest.class, StatisticProvidingClassCodeGeneratorTest.class,
		CodeFormatterTest.class })
public class AllTests {

}