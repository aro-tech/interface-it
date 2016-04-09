package com.github.aro_tech.interface_it.util.mixin;

import java.util.Collection; 
import java.util.List; 
import java.util.Map; 
import java.util.Set; 
import org.mockito.ArgumentMatcher; 
import org.mockito.InOrder; 
import org.mockito.Matchers; 
import org.mockito.MockSettings; 
import org.mockito.MockingDetails; 
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
public interface Mockito {


    // CONSTANTS: 

    /** {@link org.mockito.Mockito#CALLS_REAL_METHODS} */
    static final Answer<Object> CALLS_REAL_METHODS = org.mockito.Mockito.CALLS_REAL_METHODS;

    /** {@link org.mockito.Mockito#RETURNS_DEEP_STUBS} */
    static final Answer<Object> RETURNS_DEEP_STUBS = org.mockito.Mockito.RETURNS_DEEP_STUBS;

    /** {@link org.mockito.Mockito#RETURNS_DEFAULTS} */
    static final Answer<Object> RETURNS_DEFAULTS = org.mockito.Mockito.RETURNS_DEFAULTS;

    /** {@link org.mockito.Mockito#RETURNS_MOCKS} */
    static final Answer<Object> RETURNS_MOCKS = org.mockito.Mockito.RETURNS_MOCKS;

    /** {@link org.mockito.Mockito#RETURNS_SELF} */
    static final Answer<Object> RETURNS_SELF = org.mockito.Mockito.RETURNS_SELF;

    /** {@link org.mockito.Mockito#RETURNS_SMART_NULLS} */
    static final Answer<Object> RETURNS_SMART_NULLS = org.mockito.Mockito.RETURNS_SMART_NULLS;


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static org.mockito.verification.VerificationAfterDelay org.mockito.Mockito.after(long)
     * {@link org.mockito.Mockito#after(long)}
     */
    default VerificationAfterDelay after(long millis) {
        return org.mockito.Mockito.after(millis);
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
    default <T> T any(Class<T> clazz) {
        return Matchers.any(clazz);
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
    default <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return Matchers.anyCollectionOf(clazz);
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
    default <T> List<T> anyListOf(Class<T> clazz) {
        return Matchers.anyListOf(clazz);
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
    default <K,V> Map<K, V> anyMapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return Matchers.anyMapOf(keyClazz, valueClazz);
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
    default <T> Set<T> anySetOf(Class<T> clazz) {
        return Matchers.anySetOf(clazz);
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
     * Delegate call to public static <T> T org.mockito.Matchers.argThat(org.mockito.ArgumentMatcher<T>)
     * {@link org.mockito.Matchers#argThat(org.mockito.ArgumentMatcher)}
     */
    default <T> T argThat(ArgumentMatcher<T> matcher) {
        return Matchers.argThat(matcher);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atLeast(int)
     * {@link org.mockito.Mockito#atLeast(int)}
     */
    default VerificationMode atLeast(int minNumberOfInvocations) {
        return org.mockito.Mockito.atLeast(minNumberOfInvocations);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atLeastOnce()
     * {@link org.mockito.Mockito#atLeastOnce()}
     */
    default VerificationMode atLeastOnce() {
        return org.mockito.Mockito.atLeastOnce();
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atMost(int)
     * {@link org.mockito.Mockito#atMost(int)}
     */
    default VerificationMode atMost(int maxNumberOfInvocations) {
        return org.mockito.Mockito.atMost(maxNumberOfInvocations);
    }



    /**
     * Delegate call to public static boolean org.mockito.Matchers.booleanThat(org.mockito.ArgumentMatcher<java.lang.Boolean>)
     * {@link org.mockito.Matchers#booleanThat(org.mockito.ArgumentMatcher)}
     */
    default boolean booleanThat(ArgumentMatcher<Boolean> matcher) {
        return Matchers.booleanThat(matcher);
    }



    /**
     * Delegate call to public static byte org.mockito.Matchers.byteThat(org.mockito.ArgumentMatcher<java.lang.Byte>)
     * {@link org.mockito.Matchers#byteThat(org.mockito.ArgumentMatcher)}
     */
    default byte byteThat(ArgumentMatcher<Byte> matcher) {
        return Matchers.byteThat(matcher);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.calls(int)
     * {@link org.mockito.Mockito#calls(int)}
     */
    default VerificationMode calls(int wantedNumberOfInvocations) {
        return org.mockito.Mockito.calls(wantedNumberOfInvocations);
    }



    /**
     * Delegate call to public static char org.mockito.Matchers.charThat(org.mockito.ArgumentMatcher<java.lang.Character>)
     * {@link org.mockito.Matchers#charThat(org.mockito.ArgumentMatcher)}
     */
    default char charThat(ArgumentMatcher<Character> matcher) {
        return Matchers.charThat(matcher);
    }



    /**
     * Delegate call to public static <T> void org.mockito.Mockito.clearInvocations(T...)
     * {@link org.mockito.Mockito#clearInvocations(java.lang.Object[])}
     */
    default <T> void clearInvocations(T... mocks) {
        org.mockito.Mockito.clearInvocations(mocks);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.contains(java.lang.String)
     * {@link org.mockito.Matchers#contains(java.lang.String)}
     */
    default String contains(String substring) {
        return Matchers.contains(substring);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.description(java.lang.String)
     * {@link org.mockito.Mockito#description(java.lang.String)}
     */
    default VerificationMode description(String description) {
        return org.mockito.Mockito.description(description);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doAnswer(org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#doAnswer(org.mockito.stubbing.Answer)}
     */
    default Stubber doAnswer(Answer answer) {
        return org.mockito.Mockito.doAnswer(answer);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doCallRealMethod()
     * {@link org.mockito.Mockito#doCallRealMethod()}
     */
    default Stubber doCallRealMethod() {
        return org.mockito.Mockito.doCallRealMethod();
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doNothing()
     * {@link org.mockito.Mockito#doNothing()}
     */
    default Stubber doNothing() {
        return org.mockito.Mockito.doNothing();
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doReturn(java.lang.Object)
     * {@link org.mockito.Mockito#doReturn(java.lang.Object)}
     */
    default Stubber doReturn(Object toBeReturned) {
        return org.mockito.Mockito.doReturn(toBeReturned);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doReturn(java.lang.Object,java.lang.Object...)
     * {@link org.mockito.Mockito#doReturn(java.lang.Object,java.lang.Object[])}
     */
    default Stubber doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return org.mockito.Mockito.doReturn(toBeReturned, toBeReturnedNext);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Class<? extends java.lang.Throwable>)
     * {@link org.mockito.Mockito#doThrow(java.lang.Class)}
     */
    default Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return org.mockito.Mockito.doThrow(toBeThrown);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Throwable...)
     * {@link org.mockito.Mockito#doThrow(java.lang.Throwable[])}
     */
    default Stubber doThrow(Throwable... toBeThrown) {
        return org.mockito.Mockito.doThrow(toBeThrown);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Class<? extends java.lang.Throwable>,java.lang.Class<? extends java.lang.Throwable>...)
     * {@link org.mockito.Mockito#doThrow(java.lang.Class,java.lang.Class[])}
     */
    default Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... toBeThrownNext) {
        return org.mockito.Mockito.doThrow(toBeThrown, toBeThrownNext);
    }



    /**
     * Delegate call to public static double org.mockito.Matchers.doubleThat(org.mockito.ArgumentMatcher<java.lang.Double>)
     * {@link org.mockito.Matchers#doubleThat(org.mockito.ArgumentMatcher)}
     */
    default double doubleThat(ArgumentMatcher<Double> matcher) {
        return Matchers.doubleThat(matcher);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.endsWith(java.lang.String)
     * {@link org.mockito.Matchers#endsWith(java.lang.String)}
     */
    default String endsWith(String suffix) {
        return Matchers.endsWith(suffix);
    }



    /**
     * Delegate call to public static boolean org.mockito.Matchers.eq(boolean)
     * {@link org.mockito.Matchers#eq(boolean)}
     */
    default boolean eq(boolean value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static byte org.mockito.Matchers.eq(byte)
     * {@link org.mockito.Matchers#eq(byte)}
     */
    default byte eq(byte value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static char org.mockito.Matchers.eq(char)
     * {@link org.mockito.Matchers#eq(char)}
     */
    default char eq(char value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static double org.mockito.Matchers.eq(double)
     * {@link org.mockito.Matchers#eq(double)}
     */
    default double eq(double value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static float org.mockito.Matchers.eq(float)
     * {@link org.mockito.Matchers#eq(float)}
     */
    default float eq(float value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static int org.mockito.Matchers.eq(int)
     * {@link org.mockito.Matchers#eq(int)}
     */
    default int eq(int value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static long org.mockito.Matchers.eq(long)
     * {@link org.mockito.Matchers#eq(long)}
     */
    default long eq(long value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static short org.mockito.Matchers.eq(short)
     * {@link org.mockito.Matchers#eq(short)}
     */
    default short eq(short value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.eq(T)
     * {@link org.mockito.Matchers#eq(java.lang.Object)}
     */
    default <T> T eq(T value) {
        return Matchers.eq(value);
    }



    /**
     * Delegate call to public static float org.mockito.Matchers.floatThat(org.mockito.ArgumentMatcher<java.lang.Float>)
     * {@link org.mockito.Matchers#floatThat(org.mockito.ArgumentMatcher)}
     */
    default float floatThat(ArgumentMatcher<Float> matcher) {
        return Matchers.floatThat(matcher);
    }



    /**
     * Delegate call to public static java.lang.Object[] org.mockito.Mockito.ignoreStubs(java.lang.Object...)
     * {@link org.mockito.Mockito#ignoreStubs(java.lang.Object[])}
     */
    default Object[] ignoreStubs(Object... mocks) {
        return org.mockito.Mockito.ignoreStubs(mocks);
    }



    /**
     * Delegate call to public static org.mockito.InOrder org.mockito.Mockito.inOrder(java.lang.Object...)
     * {@link org.mockito.Mockito#inOrder(java.lang.Object[])}
     */
    default InOrder inOrder(Object... mocks) {
        return org.mockito.Mockito.inOrder(mocks);
    }



    /**
     * Delegate call to public static int org.mockito.Matchers.intThat(org.mockito.ArgumentMatcher<java.lang.Integer>)
     * {@link org.mockito.Matchers#intThat(org.mockito.ArgumentMatcher)}
     */
    default int intThat(ArgumentMatcher<Integer> matcher) {
        return Matchers.intThat(matcher);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.isA(java.lang.Class<T>)
     * {@link org.mockito.Matchers#isA(java.lang.Class)}
     */
    default <T> T isA(Class<T> clazz) {
        return Matchers.isA(clazz);
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
    default <T> T isNotNull(Class<T> clazz) {
        return Matchers.isNotNull(clazz);
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
    default <T> T isNull(Class<T> clazz) {
        return Matchers.isNull(clazz);
    }



    /**
     * Delegate call to public static long org.mockito.Matchers.longThat(org.mockito.ArgumentMatcher<java.lang.Long>)
     * {@link org.mockito.Matchers#longThat(org.mockito.ArgumentMatcher)}
     */
    default long longThat(ArgumentMatcher<Long> matcher) {
        return Matchers.longThat(matcher);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.matches(java.lang.String)
     * {@link org.mockito.Matchers#matches(java.lang.String)}
     */
    default String matches(String regex) {
        return Matchers.matches(regex);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>)
     * {@link org.mockito.Mockito#mock(java.lang.Class)}
     */
    default <T> T mock(Class<T> classToMock) {
        return org.mockito.Mockito.mock(classToMock);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.stubbing.Answer)}
     */
    default <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return org.mockito.Mockito.mock(classToMock, defaultAnswer);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.MockSettings)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.MockSettings)}
     */
    default <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return org.mockito.Mockito.mock(classToMock, mockSettings);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,java.lang.String)
     * {@link org.mockito.Mockito#mock(java.lang.Class,java.lang.String)}
     */
    default <T> T mock(Class<T> classToMock, String name) {
        return org.mockito.Mockito.mock(classToMock, name);
    }



    /**
     * Delegate call to public static org.mockito.MockingDetails org.mockito.Mockito.mockingDetails(java.lang.Object)
     * {@link org.mockito.Mockito#mockingDetails(java.lang.Object)}
     */
    default MockingDetails mockingDetails(Object toInspect) {
        return org.mockito.Mockito.mockingDetails(toInspect);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.never()
     * {@link org.mockito.Mockito#never()}
     */
    default VerificationMode never() {
        return org.mockito.Mockito.never();
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
    default <T> T notNull(Class<T> clazz) {
        return Matchers.notNull(clazz);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.only()
     * {@link org.mockito.Mockito#only()}
     */
    default VerificationMode only() {
        return org.mockito.Mockito.only();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.refEq(T,java.lang.String...)
     * {@link org.mockito.Matchers#refEq(java.lang.Object,java.lang.String[])}
     */
    default <T> T refEq(T value, String... excludeFields) {
        return Matchers.refEq(value, excludeFields);
    }



    /**
     * Delegate call to public static <T> void org.mockito.Mockito.reset(T...)
     * {@link org.mockito.Mockito#reset(java.lang.Object[])}
     */
    default <T> void reset(T... mocks) {
        org.mockito.Mockito.reset(mocks);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Matchers.same(T)
     * {@link org.mockito.Matchers#same(java.lang.Object)}
     */
    default <T> T same(T value) {
        return Matchers.same(value);
    }



    /**
     * Delegate call to public static short org.mockito.Matchers.shortThat(org.mockito.ArgumentMatcher<java.lang.Short>)
     * {@link org.mockito.Matchers#shortThat(org.mockito.ArgumentMatcher)}
     */
    default short shortThat(ArgumentMatcher<Short> matcher) {
        return Matchers.shortThat(matcher);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(java.lang.Class<T>)
     * {@link org.mockito.Mockito#spy(java.lang.Class)}
     */
    default <T> T spy(Class<T> classToSpy) {
        return org.mockito.Mockito.spy(classToSpy);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(T)
     * {@link org.mockito.Mockito#spy(java.lang.Object)}
     */
    default <T> T spy(T object) {
        return org.mockito.Mockito.spy(object);
    }



    /**
     * Delegate call to public static java.lang.String org.mockito.Matchers.startsWith(java.lang.String)
     * {@link org.mockito.Matchers#startsWith(java.lang.String)}
     */
    default String startsWith(String prefix) {
        return Matchers.startsWith(prefix);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.DeprecatedOngoingStubbing<T> org.mockito.Mockito.stub(T)
     * {@link org.mockito.Mockito#stub(java.lang.Object)}
     */
    default <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
        return org.mockito.Mockito.stub(methodCall);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.VoidMethodStubbable<T> org.mockito.Mockito.stubVoid(T)
     * {@link org.mockito.Mockito#stubVoid(java.lang.Object)}
     */
    default <T> VoidMethodStubbable<T> stubVoid(T mock) {
        return org.mockito.Mockito.stubVoid(mock);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationWithTimeout org.mockito.Mockito.timeout(long)
     * {@link org.mockito.Mockito#timeout(long)}
     */
    default VerificationWithTimeout timeout(long millis) {
        return org.mockito.Mockito.timeout(millis);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.times(int)
     * {@link org.mockito.Mockito#times(int)}
     */
    default VerificationMode times(int wantedNumberOfInvocations) {
        return org.mockito.Mockito.times(wantedNumberOfInvocations);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.validateMockitoUsage()
     * {@link org.mockito.Mockito#validateMockitoUsage()}
     */
    default void validateMockitoUsage() {
        org.mockito.Mockito.validateMockitoUsage();
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.verify(T)
     * {@link org.mockito.Mockito#verify(java.lang.Object)}
     */
    default <T> T verify(T mock) {
        return org.mockito.Mockito.verify(mock);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.verify(T,org.mockito.verification.VerificationMode)
     * {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}
     */
    default <T> T verify(T mock, VerificationMode mode) {
        return org.mockito.Mockito.verify(mock, mode);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyNoMoreInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyNoMoreInteractions(java.lang.Object[])}
     */
    default void verifyNoMoreInteractions(Object... mocks) {
        org.mockito.Mockito.verifyNoMoreInteractions(mocks);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyZeroInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyZeroInteractions(java.lang.Object[])}
     */
    default void verifyZeroInteractions(Object... mocks) {
        org.mockito.Mockito.verifyZeroInteractions(mocks);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.OngoingStubbing<T> org.mockito.Mockito.when(T)
     * {@link org.mockito.Mockito#when(java.lang.Object)}
     */
    default <T> OngoingStubbing<T> when(T methodCall) {
        return org.mockito.Mockito.when(methodCall);
    }



    /**
     * Delegate call to public static org.mockito.MockSettings org.mockito.Mockito.withSettings()
     * {@link org.mockito.Mockito#withSettings()}
     */
    default MockSettings withSettings() {
        return org.mockito.Mockito.withSettings();
    }



}