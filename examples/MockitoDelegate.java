package org.example;

import java.lang.Boolean; 
import java.lang.Byte; 
import java.lang.Character; 
import java.lang.Class; 
import java.lang.Double; 
import java.lang.Float; 
import java.lang.Integer; 
import java.lang.Long; 
import java.lang.Object; 
import java.lang.Short; 
import java.lang.String; 
import java.lang.Throwable; 
import java.util.Collection; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 
import org.hamcrest.Matcher; 
import org.mockito.InOrder; 
import org.mockito.MockSettings; 
import org.mockito.MockingDetails; 
import org.mockito.Mockito; 
import org.mockito.ReturnValues; 
import org.mockito.stubbing.Answer; 
import org.mockito.stubbing.DeprecatedOngoingStubbing; 
import org.mockito.stubbing.OngoingStubbing; 
import org.mockito.stubbing.Stubber; 
import org.mockito.stubbing.VoidMethodStubbable; 
import org.mockito.verification.VerificationAfterDelay; 
import org.mockito.verification.VerificationMode; 
import org.mockito.verification.VerificationWithTimeout; 

/** 
 * Wrapper of static elements in org.mockito.Mockito
 * Generated by Interface-It: https://github.com/aro-tech/interface-it
 * {@link org.mockito.Mockito}
 */
public interface MockitoDelegate {


    // CONSTANTS: 

    /** {@link org.mockito.Mockito#RETURNS_DEFAULTS} */
    public static final Answer RETURNS_DEFAULTS = Mockito.RETURNS_DEFAULTS;

    /** {@link org.mockito.Mockito#RETURNS_SMART_NULLS} */
    public static final Answer RETURNS_SMART_NULLS = Mockito.RETURNS_SMART_NULLS;

    /** {@link org.mockito.Mockito#RETURNS_MOCKS} */
    public static final Answer RETURNS_MOCKS = Mockito.RETURNS_MOCKS;

    /** {@link org.mockito.Mockito#RETURNS_DEEP_STUBS} */
    public static final Answer RETURNS_DEEP_STUBS = Mockito.RETURNS_DEEP_STUBS;

    /** {@link org.mockito.Mockito#CALLS_REAL_METHODS} */
    public static final Answer CALLS_REAL_METHODS = Mockito.CALLS_REAL_METHODS;


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static org.mockito.verification.VerificationAfterDelay org.mockito.Mockito.after(long)
     * {@link org.mockito.Mockito#after(long)}
     */
    default VerificationAfterDelay after(long arg0) {
        return Mockito.after(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.any()
     * {@link org.mockito.Matchers#any()}
     */
    default <T> T any() {
        return Matchers.any();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.any(java.lang.Class<T>)
     * {@link org.mockito.Matchers#any(java.lang.Class)}
     */
    default <T> T any(Class<T> arg0) {
        return Matchers.any(arg0);
    }



    /**
     * Delegate call to public static boolean org.mockito.Matchers.anyBoolean()
     * {@link org.mockito.Matchers#anyBoolean()}
     */
    default boolean anyBoolean() {
        return Matchers.anyBoolean();
    }



    /**
     * Delegate call to public static byte org.mockito.Matchers.anyByte()
     * {@link org.mockito.Matchers#anyByte()}
     */
    default byte anyByte() {
        return Matchers.anyByte();
    }



    /**
     * Delegate call to public static char org.mockito.Matchers.anyChar()
     * {@link org.mockito.Matchers#anyChar()}
     */
    default char anyChar() {
        return Matchers.anyChar();
    }



    /**
     * Delegate call to public static java.util.Collection org.mockito.Matchers.anyCollection()
     * {@link org.mockito.Matchers#anyCollection()}
     */
    default Collection anyCollection() {
        return Matchers.anyCollection();
    }



    /**
     * Delegate call to public static <T> java.util.Collection<T> org.mockito.Matchers.anyCollectionOf(java.lang.Class<T>)
     * {@link org.mockito.Matchers#anyCollectionOf(java.lang.Class)}
     */
    default <T> Collection<T> anyCollectionOf(Class<T> arg0) {
        return Matchers.anyCollectionOf(arg0);
    }



    /**
     * Delegate call to public static double org.mockito.Matchers.anyDouble()
     * {@link org.mockito.Matchers#anyDouble()}
     */
    default double anyDouble() {
        return Matchers.anyDouble();
    }



    /**
     * Delegate call to public static float org.mockito.Matchers.anyFloat()
     * {@link org.mockito.Matchers#anyFloat()}
     */
    default float anyFloat() {
        return Matchers.anyFloat();
    }



    /**
     * Delegate call to public static int org.mockito.Matchers.anyInt()
     * {@link org.mockito.Matchers#anyInt()}
     */
    default int anyInt() {
        return Matchers.anyInt();
    }



    /**
     * Delegate call to public static java.util.List org.mockito.Matchers.anyList()
     * {@link org.mockito.Matchers#anyList()}
     */
    default List anyList() {
        return Matchers.anyList();
    }



    /**
     * Delegate call to public static <T> java.util.List<T> org.mockito.Matchers.anyListOf(java.lang.Class<T>)
     * {@link org.mockito.Matchers#anyListOf(java.lang.Class)}
     */
    default <T> List<T> anyListOf(Class<T> arg0) {
        return Matchers.anyListOf(arg0);
    }



    /**
     * Delegate call to public static long org.mockito.Matchers.anyLong()
     * {@link org.mockito.Matchers#anyLong()}
     */
    default long anyLong() {
        return Matchers.anyLong();
    }



    /**
     * Delegate call to public static java.util.Map org.mockito.Matchers.anyMap()
     * {@link org.mockito.Matchers#anyMap()}
     */
    default Map anyMap() {
        return Matchers.anyMap();
    }



    /**
     * Delegate call to public static <K,V> java.util.Map<K, V> org.mockito.Matchers.anyMapOf(java.lang.Class<K>,java.lang.Class<V>)
     * {@link org.mockito.Matchers#anyMapOf(java.lang.Class,java.lang.Class)}
     */
    default <K,V> Map<K, V> anyMapOf(Class<K> arg0, Class<V> arg1) {
        return Matchers.anyMapOf(arg0, arg1);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.anyObject()
     * {@link org.mockito.Matchers#anyObject()}
     */
    default <T> T anyObject() {
        return Matchers.anyObject();
    }



    /**
     * Delegate call to public static java.util.Set org.mockito.Matchers.anySet()
     * {@link org.mockito.Matchers#anySet()}
     */
    default Set anySet() {
        return Matchers.anySet();
    }



    /**
     * Delegate call to public static <T> java.util.Set<T> org.mockito.Matchers.anySetOf(java.lang.Class<T>)
     * {@link org.mockito.Matchers#anySetOf(java.lang.Class)}
     */
    default <T> Set<T> anySetOf(Class<T> arg0) {
        return Matchers.anySetOf(arg0);
    }



    /**
     * Delegate call to public static short org.mockito.Matchers.anyShort()
     * {@link org.mockito.Matchers#anyShort()}
     */
    default short anyShort() {
        return Matchers.anyShort();
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.anyString()
     * {@link org.mockito.Matchers#anyString()}
     */
    default String anyString() {
        return Matchers.anyString();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.anyVararg()
     * {@link org.mockito.Matchers#anyVararg()}
     */
    default <T> T anyVararg() {
        return Matchers.anyVararg();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.argThat(org.hamcrest.Matcher<T>)
     * {@link org.mockito.Matchers#argThat(org.hamcrest.Matcher)}
     */
    default <T> T argThat(Matcher<T> arg0) {
        return Matchers.argThat(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atLeast(int)
     * {@link org.mockito.Mockito#atLeast(int)}
     */
    default VerificationMode atLeast(int arg0) {
        return Mockito.atLeast(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atLeastOnce()
     * {@link org.mockito.Mockito#atLeastOnce()}
     */
    default VerificationMode atLeastOnce() {
        return Mockito.atLeastOnce();
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atMost(int)
     * {@link org.mockito.Mockito#atMost(int)}
     */
    default VerificationMode atMost(int arg0) {
        return Mockito.atMost(arg0);
    }



    /**
     * Delegate call to public static boolean org.mockito.Matchers.booleanThat(org.hamcrest.Matcher<java.lang.Boolean>)
     * {@link org.mockito.Matchers#booleanThat(org.hamcrest.Matcher)}
     */
    default boolean booleanThat(Matcher<Boolean> arg0) {
        return Matchers.booleanThat(arg0);
    }



    /**
     * Delegate call to public static byte org.mockito.Matchers.byteThat(org.hamcrest.Matcher<java.lang.Byte>)
     * {@link org.mockito.Matchers#byteThat(org.hamcrest.Matcher)}
     */
    default byte byteThat(Matcher<Byte> arg0) {
        return Matchers.byteThat(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.calls(int)
     * {@link org.mockito.Mockito#calls(int)}
     */
    default VerificationMode calls(int arg0) {
        return Mockito.calls(arg0);
    }



    /**
     * Delegate call to public static char org.mockito.Matchers.charThat(org.hamcrest.Matcher<java.lang.Character>)
     * {@link org.mockito.Matchers#charThat(org.hamcrest.Matcher)}
     */
    default char charThat(Matcher<Character> arg0) {
        return Matchers.charThat(arg0);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.contains(java.lang.String)
     * {@link org.mockito.Matchers#contains(java.lang.String)}
     */
    default String contains(String arg0) {
        return Matchers.contains(arg0);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doAnswer(org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#doAnswer(org.mockito.stubbing.Answer)}
     */
    default Stubber doAnswer(Answer arg0) {
        return Mockito.doAnswer(arg0);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doCallRealMethod()
     * {@link org.mockito.Mockito#doCallRealMethod()}
     */
    default Stubber doCallRealMethod() {
        return Mockito.doCallRealMethod();
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doNothing()
     * {@link org.mockito.Mockito#doNothing()}
     */
    default Stubber doNothing() {
        return Mockito.doNothing();
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doReturn(java.lang.Object)
     * {@link org.mockito.Mockito#doReturn(java.lang.Object)}
     */
    default Stubber doReturn(Object arg0) {
        return Mockito.doReturn(arg0);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Class<? extends java.lang.Throwable>)
     * {@link org.mockito.Mockito#doThrow(java.lang.Class)}
     */
    default Stubber doThrow(Class<? extends Throwable> arg0) {
        return Mockito.doThrow(arg0);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Throwable)
     * {@link org.mockito.Mockito#doThrow(java.lang.Throwable)}
     */
    default Stubber doThrow(Throwable arg0) {
        return Mockito.doThrow(arg0);
    }



    /**
     * Delegate call to public static double org.mockito.Matchers.doubleThat(org.hamcrest.Matcher<java.lang.Double>)
     * {@link org.mockito.Matchers#doubleThat(org.hamcrest.Matcher)}
     */
    default double doubleThat(Matcher<Double> arg0) {
        return Matchers.doubleThat(arg0);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.endsWith(java.lang.String)
     * {@link org.mockito.Matchers#endsWith(java.lang.String)}
     */
    default String endsWith(String arg0) {
        return Matchers.endsWith(arg0);
    }



    /**
     * Delegate call to public static boolean org.mockito.Matchers.eq(boolean)
     * {@link org.mockito.Matchers#eq(boolean)}
     */
    default boolean eq(boolean arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static byte org.mockito.Matchers.eq(byte)
     * {@link org.mockito.Matchers#eq(byte)}
     */
    default byte eq(byte arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static char org.mockito.Matchers.eq(char)
     * {@link org.mockito.Matchers#eq(char)}
     */
    default char eq(char arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static double org.mockito.Matchers.eq(double)
     * {@link org.mockito.Matchers#eq(double)}
     */
    default double eq(double arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static float org.mockito.Matchers.eq(float)
     * {@link org.mockito.Matchers#eq(float)}
     */
    default float eq(float arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static int org.mockito.Matchers.eq(int)
     * {@link org.mockito.Matchers#eq(int)}
     */
    default int eq(int arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static long org.mockito.Matchers.eq(long)
     * {@link org.mockito.Matchers#eq(long)}
     */
    default long eq(long arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static short org.mockito.Matchers.eq(short)
     * {@link org.mockito.Matchers#eq(short)}
     */
    default short eq(short arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.eq(T)
     * {@link org.mockito.Matchers#eq(java.lang.Object)}
     */
    default <T> T eq(T arg0) {
        return Matchers.eq(arg0);
    }



    /**
     * Delegate call to public static float org.mockito.Matchers.floatThat(org.hamcrest.Matcher<java.lang.Float>)
     * {@link org.mockito.Matchers#floatThat(org.hamcrest.Matcher)}
     */
    default float floatThat(Matcher<Float> arg0) {
        return Matchers.floatThat(arg0);
    }



    /**
     * Delegate call to public static java.lang.Object[] org.mockito.Mockito.ignoreStubs(java.lang.Object...)
     * {@link org.mockito.Mockito#ignoreStubs(java.lang.Object[])}
     */
    default Object[] ignoreStubs(Object... arg0) {
        return Mockito.ignoreStubs(arg0);
    }



    /**
     * Delegate call to public static org.mockito.InOrder org.mockito.Mockito.inOrder(java.lang.Object...)
     * {@link org.mockito.Mockito#inOrder(java.lang.Object[])}
     */
    default InOrder inOrder(Object... arg0) {
        return Mockito.inOrder(arg0);
    }



    /**
     * Delegate call to public static int org.mockito.Matchers.intThat(org.hamcrest.Matcher<java.lang.Integer>)
     * {@link org.mockito.Matchers#intThat(org.hamcrest.Matcher)}
     */
    default int intThat(Matcher<Integer> arg0) {
        return Matchers.intThat(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.isA(java.lang.Class<T>)
     * {@link org.mockito.Matchers#isA(java.lang.Class)}
     */
    default <T> T isA(Class<T> arg0) {
        return Matchers.isA(arg0);
    }



    /**
     * Delegate call to public static java.lang.Object org.mockito.Matchers.isNotNull()
     * {@link org.mockito.Matchers#isNotNull()}
     */
    default Object isNotNull() {
        return Matchers.isNotNull();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.isNotNull(java.lang.Class<T>)
     * {@link org.mockito.Matchers#isNotNull(java.lang.Class)}
     */
    default <T> T isNotNull(Class<T> arg0) {
        return Matchers.isNotNull(arg0);
    }



    /**
     * Delegate call to public static java.lang.Object org.mockito.Matchers.isNull()
     * {@link org.mockito.Matchers#isNull()}
     */
    default Object isNull() {
        return Matchers.isNull();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.isNull(java.lang.Class<T>)
     * {@link org.mockito.Matchers#isNull(java.lang.Class)}
     */
    default <T> T isNull(Class<T> arg0) {
        return Matchers.isNull(arg0);
    }



    /**
     * Delegate call to public static long org.mockito.Matchers.longThat(org.hamcrest.Matcher<java.lang.Long>)
     * {@link org.mockito.Matchers#longThat(org.hamcrest.Matcher)}
     */
    default long longThat(Matcher<Long> arg0) {
        return Matchers.longThat(arg0);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.matches(java.lang.String)
     * {@link org.mockito.Matchers#matches(java.lang.String)}
     */
    default String matches(String arg0) {
        return Matchers.matches(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>)
     * {@link org.mockito.Mockito#mock(java.lang.Class)}
     */
    default <T> T mock(Class<T> arg0) {
        return Mockito.mock(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.stubbing.Answer)}
     */
    default <T> T mock(Class<T> arg0, Answer arg1) {
        return Mockito.mock(arg0, arg1);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.MockSettings)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.MockSettings)}
     */
    default <T> T mock(Class<T> arg0, MockSettings arg1) {
        return Mockito.mock(arg0, arg1);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.ReturnValues)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.ReturnValues)}
     */
    default <T> T mock(Class<T> arg0, ReturnValues arg1) {
        return Mockito.mock(arg0, arg1);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,java.lang.String)
     * {@link org.mockito.Mockito#mock(java.lang.Class,java.lang.String)}
     */
    default <T> T mock(Class<T> arg0, String arg1) {
        return Mockito.mock(arg0, arg1);
    }



    /**
     * Delegate call to public static org.mockito.MockingDetails org.mockito.Mockito.mockingDetails(java.lang.Object)
     * {@link org.mockito.Mockito#mockingDetails(java.lang.Object)}
     */
    default MockingDetails mockingDetails(Object arg0) {
        return Mockito.mockingDetails(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.never()
     * {@link org.mockito.Mockito#never()}
     */
    default VerificationMode never() {
        return Mockito.never();
    }



    /**
     * Delegate call to public static java.lang.Object org.mockito.Matchers.notNull()
     * {@link org.mockito.Matchers#notNull()}
     */
    default Object notNull() {
        return Matchers.notNull();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.notNull(java.lang.Class<T>)
     * {@link org.mockito.Matchers#notNull(java.lang.Class)}
     */
    default <T> T notNull(Class<T> arg0) {
        return Matchers.notNull(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.only()
     * {@link org.mockito.Mockito#only()}
     */
    default VerificationMode only() {
        return Mockito.only();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.refEq(T,java.lang.String...)
     * {@link org.mockito.Matchers#refEq(java.lang.Object,java.lang.String[])}
     */
    default <T> T refEq(T arg0, String... arg1) {
        return Matchers.refEq(arg0, arg1);
    }



    /**
     * Delegate call to public static <T> void org.mockito.Mockito.reset(T...)
     * {@link org.mockito.Mockito#reset(java.lang.Object[])}
     */
    default <T> void reset(T... arg0) {
        Mockito.reset(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.same(T)
     * {@link org.mockito.Matchers#same(java.lang.Object)}
     */
    default <T> T same(T arg0) {
        return Matchers.same(arg0);
    }



    /**
     * Delegate call to public static short org.mockito.Matchers.shortThat(org.hamcrest.Matcher<java.lang.Short>)
     * {@link org.mockito.Matchers#shortThat(org.hamcrest.Matcher)}
     */
    default short shortThat(Matcher<Short> arg0) {
        return Matchers.shortThat(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(java.lang.Class<T>)
     * {@link org.mockito.Mockito#spy(java.lang.Class)}
     */
    default <T> T spy(Class<T> arg0) {
        return Mockito.spy(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(T)
     * {@link org.mockito.Mockito#spy(java.lang.Object)}
     */
    default <T> T spy(T arg0) {
        return Mockito.spy(arg0);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.startsWith(java.lang.String)
     * {@link org.mockito.Matchers#startsWith(java.lang.String)}
     */
    default String startsWith(String arg0) {
        return Matchers.startsWith(arg0);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.DeprecatedOngoingStubbing<T> org.mockito.Mockito.stub(T)
     * {@link org.mockito.Mockito#stub(java.lang.Object)}
     */
    default <T> DeprecatedOngoingStubbing<T> stub(T arg0) {
        return Mockito.stub(arg0);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.VoidMethodStubbable<T> org.mockito.Mockito.stubVoid(T)
     * {@link org.mockito.Mockito#stubVoid(java.lang.Object)}
     */
    default <T> VoidMethodStubbable<T> stubVoid(T arg0) {
        return Mockito.stubVoid(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationWithTimeout org.mockito.Mockito.timeout(long)
     * {@link org.mockito.Mockito#timeout(long)}
     */
    default VerificationWithTimeout timeout(long arg0) {
        return Mockito.timeout(arg0);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.times(int)
     * {@link org.mockito.Mockito#times(int)}
     */
    default VerificationMode times(int arg0) {
        return Mockito.times(arg0);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.validateMockitoUsage()
     * {@link org.mockito.Mockito#validateMockitoUsage()}
     */
    default void validateMockitoUsage() {
        Mockito.validateMockitoUsage();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.verify(T)
     * {@link org.mockito.Mockito#verify(java.lang.Object)}
     */
    default <T> T verify(T arg0) {
        return Mockito.verify(arg0);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.verify(T,org.mockito.verification.VerificationMode)
     * {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}
     */
    default <T> T verify(T arg0, VerificationMode arg1) {
        return Mockito.verify(arg0, arg1);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyNoMoreInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyNoMoreInteractions(java.lang.Object[])}
     */
    default void verifyNoMoreInteractions(Object... arg0) {
        Mockito.verifyNoMoreInteractions(arg0);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyZeroInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyZeroInteractions(java.lang.Object[])}
     */
    default void verifyZeroInteractions(Object... arg0) {
        Mockito.verifyZeroInteractions(arg0);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.OngoingStubbing<T> org.mockito.Mockito.when(T)
     * {@link org.mockito.Mockito#when(java.lang.Object)}
     */
    default <T> OngoingStubbing<T> when(T arg0) {
        return Mockito.when(arg0);
    }



    /**
     * Delegate call to public static org.mockito.MockSettings org.mockito.Mockito.withSettings()
     * {@link org.mockito.Mockito#withSettings()}
     */
    default MockSettings withSettings() {
        return Mockito.withSettings();
    }



}