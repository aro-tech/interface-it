/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AbstractByteArrayAssert;
import org.interfaceit.util.mixin.Mockito;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests to verify parsing source code to get argument names
 * 
 * @author aro_tech
 *
 */
public class SourceLineReadingArgumentNameLoaderTest implements Mockito {
	private LookupArgumentNameSource target = mock(LookupArgumentNameSource.class);
	private SourceLineReadingArgumentNameLoader underTest = new SourceLineReadingArgumentNameLoader();
	private List<String> sourceLines = new ArrayList<>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_one_primitive_arg() {
		sourceLines.add("public static AbstractByteAssert<?> assertThat(byte actual) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("assertThat", 0, "byte", "actual");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_one_primitive_array_arg() {
		sourceLines.add("    public static AbstractByteArrayAssert<?> assertThat(byte[] actual) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("assertThat", 0, "byte[]", "actual");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_one_object_arg() {
		sourceLines.add(" public static BoingoBoingo flibble(java.util.OptionalLong thingy) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "OptionalLong", "thingy");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_ignore_extraneous_lines() {
		sourceLines.add("");
		sourceLines.add("/**");
		sourceLines.add(" * A method {@whatever}");
		sourceLines.add(" */");
		sourceLines.add(" public static BoingoBoingo flibble(java.util.OptionalLong thingy) {");
		sourceLines.add(" if(something()) {");
		sourceLines.add("     doStuff();");
		sourceLines.add(" }");

		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "OptionalLong", "thingy");
		verifyNoMoreInteractions(target);
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_one_template_arg() {
		sourceLines.add(" public static <Foosh> Foosh flibble(BoingoBoingo<Foosh> thingy) {");
		sourceLines.add("public static boolean booleanThat(ArgumentMatcher<Boolean> matcher) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "BoingoBoingo<Foosh>", "thingy");
		verify(target).add("booleanThat", 0, "ArgumentMatcher<Boolean>", "matcher");
	}
	
	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void can_handle_final_arg() {
		sourceLines.add("public static boolean booleanThing(final ArgumentMatcher<Boolean> matcher,final int int1, final int int2) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("booleanThing", 0, "ArgumentMatcher<Boolean>", "matcher");
		verify(target).add("booleanThing", 1, "int", "int1");
		verify(target).add("booleanThing", 2, "int", "int2");

	}


	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_two_template_args() {
		sourceLines.add(" public static <T,U> T flibble(BoingoBoingo<T> thingy, U whatsit) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "BoingoBoingo<T>", "thingy");
		verify(target).add("flibble", 1, "U", "whatsit");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_multi_line_signature() {
		sourceLines.add(" public static <T,U> T flibble(BoingoBoingo<T> thingy, U whatsit,   ");
		sourceLines.add("     Set<String> cheese, Scoop scoop,");
		sourceLines.add("       A a,B b) {");

		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "BoingoBoingo<T>", "thingy");
		verify(target).add("flibble", 1, "U", "whatsit");
		verify(target).add("flibble", 2, "Set<String>", "cheese");
		verify(target).add("flibble", 3, "Scoop", "scoop");
		verify(target).add("flibble", 4, "A", "a");
		verify(target).add("flibble", 5, "B", "b");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_ignore_comments() {
		sourceLines.add("");
		sourceLines.add("/*");
		sourceLines.add(" * public static void aThingy(String thing)");
		sourceLines.add(" */");
		sourceLines.add(" public static BoingoBoingo flobble(java.util.OptionalLong thingy, ");
		sourceLines.add(" // String anotherThing");
		sourceLines.add(" Board board /*, int whoops */) {");
		sourceLines.add(" {");

		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flobble", 0, "OptionalLong", "thingy");
		verify(target).add("flobble", 1, "Board", "board");
		verifyNoMoreInteractions(target);

	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_one_varargs_arg() {
		sourceLines.add("public static <T> void clearInvocations(T ... mocks) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("clearInvocations", 0, "T...", "mocks");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_load_for_type_with_extends() {
		sourceLines.add("public static Stubber doThrow(Class<? extends Throwable> toBeThrown) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("doThrow", 0, "Class<? extends Throwable>", "toBeThrown");
	}
	 
	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_handle_space_after_method_name() {
		sourceLines.add(" public static <Foosh> Foosh flibble (BoingoBoingo<Foosh> thingy) {");
		underTest.parseAndLoad(sourceLines, target);
		verify(target).add("flibble", 0, "BoingoBoingo<Foosh>", "thingy");
	}

	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_ignore_method_with_no_args() {
		sourceLines.add("public static void useDefaultDateFormatsOnly() { ");
		underTest.parseAndLoad(sourceLines, target);
		verifyZeroInteractions(target);
	}
	
	/**
	 * Test method for
	 * {@link org.interfaceit.meta.arguments.SourceLineReadingArgumentNameLoader#parseAndLoad(java.util.List, org.interfaceit.meta.arguments.LookupArgumentNameSource)}
	 * .
	 */
	@Test
	public void should_ignore_declaration_which_is_not_a_method() {
		sourceLines.add("public static final Thingy THINGY = new Thingy(){};");
		sourceLines.add("public static final Thingy THINGY ");
		sourceLines.add(" = new Thingy(){};");
		underTest.parseAndLoad(sourceLines, target);
		verifyZeroInteractions(target);
	}



}
