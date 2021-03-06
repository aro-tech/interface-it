/**
 * 
 */
package com.github.aro_tech.interface_it.format;

import org.junit.Before;
import org.junit.Test;

import com.github.aro_tech.tdd_mixins.AllAssertions;

/**
 * Unit tests for CodeFormatter
 * 
 * @author aro_tech
 *
 */
public class CodeFormatterTest implements AllAssertions {
	private CodeFormatter underTest = CodeFormatter.getDefault();
	private StringBuilder buf = new StringBuilder();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void should_append_comment_before_methods() {
		underTest.appendCommentBeforeMethods(buf);
		assertEquals("    // DELEGATE METHODS: " + System.lineSeparator(), buf.toString());
	}

	@Test
	public void should_append_comment_before_constants() {
		underTest.appendCommentBeforeConstants(buf);
		assertEquals("    // CONSTANTS: " + System.lineSeparator(), buf.toString());
	}

	@Test
	public void should_append_class_comment() {
		underTest.appendClassComment(Math.class, buf);
		assertThat(buf.toString()).contains("/** ", " * Wrapper of static elements in java.lang.Math",
				" * Generated by Interface-It: https://github.com/aro-tech/interface-it", " * {@link java.lang.Math}",
				" */");
	}

	@Test
	public void should_append_method_comment() {
		underTest.appendMethodComment(buf, "foo", "void foo()", "MyDelegate", "String");
		assertThat(buf.toString()).contains("/**", " * Delegate call to void foo()", " * {@link MyDelegate#foo(String)}", " */");
	}

	@Test
	public void should_generate_newline_with_spaces() {
		assertEquals(System.lineSeparator() + "        ", underTest.newlineWithIndentations(2));
	}
}
