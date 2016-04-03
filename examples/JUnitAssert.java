package com.github.aro_tech.interface_it.util.mixin;

import java.lang.Object; 
import java.lang.String; 
import org.hamcrest.Matcher; 
import org.junit.Assert; 
import org.junit.internal.ArrayComparisonFailure; 

/** 
 * Wrapper of static elements in org.junit.Assert
 * Generated by Interface-It: https://github.com/aro-tech/interface-it
 * {@link org.junit.Assert}
 */
public interface JUnitAssert {


    // CONSTANTS: 


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(boolean[],boolean[])
     * {@link org.junit.Assert#assertArrayEquals(boolean[],boolean[])}
     */
    default void assertArrayEquals(boolean[] expecteds, boolean[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(byte[],byte[])
     * {@link org.junit.Assert#assertArrayEquals(byte[],byte[])}
     */
    default void assertArrayEquals(byte[] expecteds, byte[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(char[],char[])
     * {@link org.junit.Assert#assertArrayEquals(char[],char[])}
     */
    default void assertArrayEquals(char[] expecteds, char[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(int[],int[])
     * {@link org.junit.Assert#assertArrayEquals(int[],int[])}
     */
    default void assertArrayEquals(int[] expecteds, int[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(long[],long[])
     * {@link org.junit.Assert#assertArrayEquals(long[],long[])}
     */
    default void assertArrayEquals(long[] expecteds, long[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.Object[],java.lang.Object[])
     * {@link org.junit.Assert#assertArrayEquals(java.lang.Object[],java.lang.Object[])}
     */
    default void assertArrayEquals(Object[] expecteds, Object[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(short[],short[])
     * {@link org.junit.Assert#assertArrayEquals(short[],short[])}
     */
    default void assertArrayEquals(short[] expecteds, short[] actuals) {
        Assert.assertArrayEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(double[],double[],double)
     * {@link org.junit.Assert#assertArrayEquals(double[],double[],double)}
     */
    default void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
        Assert.assertArrayEquals(expecteds, actuals, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(float[],float[],float)
     * {@link org.junit.Assert#assertArrayEquals(float[],float[],float)}
     */
    default void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
        Assert.assertArrayEquals(expecteds, actuals, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,boolean[],boolean[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,boolean[],boolean[])}
     */
    default void assertArrayEquals(String message, boolean[] expecteds, boolean[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,byte[],byte[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,byte[],byte[])}
     */
    default void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,char[],char[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,char[],char[])}
     */
    default void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,int[],int[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,int[],int[])}
     */
    default void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,long[],long[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,long[],long[])}
     */
    default void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,java.lang.Object[],java.lang.Object[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,java.lang.Object[],java.lang.Object[])}
     */
    default void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,short[],short[]) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,short[],short[])}
     */
    default void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,double[],double[],double) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,double[],double[],double)}
     */
    default void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertArrayEquals(java.lang.String,float[],float[],float) throws org.junit.internal.ArrayComparisonFailure
     * {@link org.junit.Assert#assertArrayEquals(java.lang.String,float[],float[],float)}
     */
    default void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
        Assert.assertArrayEquals(message, expecteds, actuals, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(double,double)
     * {@link org.junit.Assert#assertEquals(double,double)}
     */
    @Deprecated
    default void assertEquals(double expected, double actual) {
        Assert.assertEquals(expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(long,long)
     * {@link org.junit.Assert#assertEquals(long,long)}
     */
    default void assertEquals(long expected, long actual) {
        Assert.assertEquals(expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertEquals(java.lang.Object,java.lang.Object)}
     */
    default void assertEquals(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.Object[],java.lang.Object[])
     * {@link org.junit.Assert#assertEquals(java.lang.Object[],java.lang.Object[])}
     */
    @Deprecated
    default void assertEquals(Object[] expecteds, Object[] actuals) {
        Assert.assertEquals(expecteds, actuals);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(double,double,double)
     * {@link org.junit.Assert#assertEquals(double,double,double)}
     */
    default void assertEquals(double expected, double actual, double delta) {
        Assert.assertEquals(expected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(float,float,float)
     * {@link org.junit.Assert#assertEquals(float,float,float)}
     */
    default void assertEquals(float expected, float actual, float delta) {
        Assert.assertEquals(expected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,double,double)
     * {@link org.junit.Assert#assertEquals(java.lang.String,double,double)}
     */
    @Deprecated
    default void assertEquals(String message, double actual, double delta) {
        Assert.assertEquals(message, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,long,long)
     * {@link org.junit.Assert#assertEquals(java.lang.String,long,long)}
     */
    default void assertEquals(String message, long expected, long actual) {
        Assert.assertEquals(message, expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertEquals(java.lang.String,java.lang.Object,java.lang.Object)}
     */
    default void assertEquals(String message, Object expected, Object actual) {
        Assert.assertEquals(message, expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,java.lang.Object[],java.lang.Object[])
     * {@link org.junit.Assert#assertEquals(java.lang.String,java.lang.Object[],java.lang.Object[])}
     */
    @Deprecated
    default void assertEquals(String message, Object[] arg1, Object[] arg2) {
        Assert.assertEquals(message, arg1, arg2);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,double,double,double)
     * {@link org.junit.Assert#assertEquals(java.lang.String,double,double,double)}
     */
    default void assertEquals(String message, double expected, double actual, double delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertEquals(java.lang.String,float,float,float)
     * {@link org.junit.Assert#assertEquals(java.lang.String,float,float,float)}
     */
    default void assertEquals(String message, float expected, float actual, float delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertFalse(boolean)
     * {@link org.junit.Assert#assertFalse(boolean)}
     */
    default void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertFalse(java.lang.String,boolean)
     * {@link org.junit.Assert#assertFalse(java.lang.String,boolean)}
     */
    default void assertFalse(String message, boolean condition) {
        Assert.assertFalse(message, condition);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(long,long)
     * {@link org.junit.Assert#assertNotEquals(long,long)}
     */
    default void assertNotEquals(long unexpected, long actual) {
        Assert.assertNotEquals(unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertNotEquals(java.lang.Object,java.lang.Object)}
     */
    default void assertNotEquals(Object unexpected, Object actual) {
        Assert.assertNotEquals(unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(double,double,double)
     * {@link org.junit.Assert#assertNotEquals(double,double,double)}
     */
    default void assertNotEquals(double unexpected, double actual, double delta) {
        Assert.assertNotEquals(unexpected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(float,float,float)
     * {@link org.junit.Assert#assertNotEquals(float,float,float)}
     */
    default void assertNotEquals(float unexpected, float actual, float delta) {
        Assert.assertNotEquals(unexpected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(java.lang.String,long,long)
     * {@link org.junit.Assert#assertNotEquals(java.lang.String,long,long)}
     */
    default void assertNotEquals(String message, long unexpected, long actual) {
        Assert.assertNotEquals(message, unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(java.lang.String,java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertNotEquals(java.lang.String,java.lang.Object,java.lang.Object)}
     */
    default void assertNotEquals(String message, Object unexpected, Object actual) {
        Assert.assertNotEquals(message, unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(java.lang.String,double,double,double)
     * {@link org.junit.Assert#assertNotEquals(java.lang.String,double,double,double)}
     */
    default void assertNotEquals(String message, double unexpected, double actual, double delta) {
        Assert.assertNotEquals(message, unexpected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotEquals(java.lang.String,float,float,float)
     * {@link org.junit.Assert#assertNotEquals(java.lang.String,float,float,float)}
     */
    default void assertNotEquals(String message, float unexpected, float actual, float delta) {
        Assert.assertNotEquals(message, unexpected, actual, delta);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotNull(java.lang.Object)
     * {@link org.junit.Assert#assertNotNull(java.lang.Object)}
     */
    default void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotNull(java.lang.String,java.lang.Object)
     * {@link org.junit.Assert#assertNotNull(java.lang.String,java.lang.Object)}
     */
    default void assertNotNull(String message, Object object) {
        Assert.assertNotNull(message, object);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotSame(java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertNotSame(java.lang.Object,java.lang.Object)}
     */
    default void assertNotSame(Object unexpected, Object actual) {
        Assert.assertNotSame(unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNotSame(java.lang.String,java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertNotSame(java.lang.String,java.lang.Object,java.lang.Object)}
     */
    default void assertNotSame(String message, Object unexpected, Object actual) {
        Assert.assertNotSame(message, unexpected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNull(java.lang.Object)
     * {@link org.junit.Assert#assertNull(java.lang.Object)}
     */
    default void assertNull(Object object) {
        Assert.assertNull(object);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertNull(java.lang.String,java.lang.Object)
     * {@link org.junit.Assert#assertNull(java.lang.String,java.lang.Object)}
     */
    default void assertNull(String message, Object object) {
        Assert.assertNull(message, object);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertSame(java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertSame(java.lang.Object,java.lang.Object)}
     */
    default void assertSame(Object expected, Object actual) {
        Assert.assertSame(expected, actual);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertSame(java.lang.String,java.lang.Object,java.lang.Object)
     * {@link org.junit.Assert#assertSame(java.lang.String,java.lang.Object,java.lang.Object)}
     */
    default void assertSame(String message, Object expected, Object actual) {
        Assert.assertSame(message, expected, actual);
    }



    /**
     * Delegate call to public static <T> void org.junit.Assert.assertThat(T,org.hamcrest.Matcher<? super T>)
     * {@link org.junit.Assert#assertThat(java.lang.Object,org.hamcrest.Matcher)}
     */
    default <T> void assertThat(T actual, Matcher<? super T> matcher) {
        Assert.assertThat(actual, matcher);
    }



    /**
     * Delegate call to public static <T> void org.junit.Assert.assertThat(java.lang.String,T,org.hamcrest.Matcher<? super T>)
     * {@link org.junit.Assert#assertThat(java.lang.String,java.lang.Object,org.hamcrest.Matcher)}
     */
    default <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        Assert.assertThat(reason, actual, matcher);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertTrue(boolean)
     * {@link org.junit.Assert#assertTrue(boolean)}
     */
    default void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }



    /**
     * Delegate call to public static void org.junit.Assert.assertTrue(java.lang.String,boolean)
     * {@link org.junit.Assert#assertTrue(java.lang.String,boolean)}
     */
    default void assertTrue(String message, boolean condition) {
        Assert.assertTrue(message, condition);
    }



    /**
     * Delegate call to public static void org.junit.Assert.fail()
     * {@link org.junit.Assert#fail()}
     */
    default void fail() {
        Assert.fail();
    }



    /**
     * Delegate call to public static void org.junit.Assert.fail(java.lang.String)
     * {@link org.junit.Assert#fail(java.lang.String)}
     */
    default void fail(String message) {
        Assert.fail(message);
    }



}