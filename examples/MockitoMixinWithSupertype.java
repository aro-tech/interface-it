package com.github.aro_tech.interface_it.util.mixin;

import org.mockito.InOrder; 
import org.mockito.MockSettings; 
import org.mockito.MockingDetails; 
import org.mockito.Mockito; 
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
public interface MockitoMixinWithSupertype extends MatchersMixin {


    // CONSTANTS: 

    /** {@link org.mockito.Mockito#CALLS_REAL_METHODS} */
    static final Answer<Object> CALLS_REAL_METHODS = Mockito.CALLS_REAL_METHODS;

    /** {@link org.mockito.Mockito#RETURNS_DEEP_STUBS} */
    static final Answer<Object> RETURNS_DEEP_STUBS = Mockito.RETURNS_DEEP_STUBS;

    /** {@link org.mockito.Mockito#RETURNS_DEFAULTS} */
    static final Answer<Object> RETURNS_DEFAULTS = Mockito.RETURNS_DEFAULTS;

    /** {@link org.mockito.Mockito#RETURNS_MOCKS} */
    static final Answer<Object> RETURNS_MOCKS = Mockito.RETURNS_MOCKS;

    /** {@link org.mockito.Mockito#RETURNS_SELF} */
    static final Answer<Object> RETURNS_SELF = Mockito.RETURNS_SELF;

    /** {@link org.mockito.Mockito#RETURNS_SMART_NULLS} */
    static final Answer<Object> RETURNS_SMART_NULLS = Mockito.RETURNS_SMART_NULLS;


    // DELEGATE METHODS: 

    /**
     * Delegate call to public static org.mockito.verification.VerificationAfterDelay org.mockito.Mockito.after(long)
     * {@link org.mockito.Mockito#after(long)}
     */
    default VerificationAfterDelay after(long millis) {
        return Mockito.after(millis);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.atLeast(int)
     * {@link org.mockito.Mockito#atLeast(int)}
     */
    default VerificationMode atLeast(int minNumberOfInvocations) {
        return Mockito.atLeast(minNumberOfInvocations);
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
    default VerificationMode atMost(int maxNumberOfInvocations) {
        return Mockito.atMost(maxNumberOfInvocations);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.calls(int)
     * {@link org.mockito.Mockito#calls(int)}
     */
    default VerificationMode calls(int wantedNumberOfInvocations) {
        return Mockito.calls(wantedNumberOfInvocations);
    }



    /**
     * Delegate call to public static <T> void org.mockito.Mockito.clearInvocations(T...)
     * {@link org.mockito.Mockito#clearInvocations(java.lang.Object[])}
     */
    default <T> void clearInvocations(T... mocks) {
        Mockito.clearInvocations(mocks);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.description(java.lang.String)
     * {@link org.mockito.Mockito#description(java.lang.String)}
     */
    default VerificationMode description(String description) {
        return Mockito.description(description);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doAnswer(org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#doAnswer(org.mockito.stubbing.Answer)}
     */
    default Stubber doAnswer(Answer answer) {
        return Mockito.doAnswer(answer);
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
    default Stubber doReturn(Object toBeReturned) {
        return Mockito.doReturn(toBeReturned);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doReturn(java.lang.Object,java.lang.Object...)
     * {@link org.mockito.Mockito#doReturn(java.lang.Object,java.lang.Object[])}
     */
    default Stubber doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return Mockito.doReturn(toBeReturned, toBeReturnedNext);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Class<? extends java.lang.Throwable>)
     * {@link org.mockito.Mockito#doThrow(java.lang.Class)}
     */
    default Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return Mockito.doThrow(toBeThrown);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Throwable...)
     * {@link org.mockito.Mockito#doThrow(java.lang.Throwable[])}
     */
    default Stubber doThrow(Throwable... toBeThrown) {
        return Mockito.doThrow(toBeThrown);
    }



    /**
     * Delegate call to public static org.mockito.stubbing.Stubber org.mockito.Mockito.doThrow(java.lang.Class<? extends java.lang.Throwable>,java.lang.Class<? extends java.lang.Throwable>...)
     * {@link org.mockito.Mockito#doThrow(java.lang.Class,java.lang.Class[])}
     */
    default Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... toBeThrownNext) {
        return Mockito.doThrow(toBeThrown, toBeThrownNext);
    }



    /**
     * Delegate call to public static java.lang.Object[] org.mockito.Mockito.ignoreStubs(java.lang.Object...)
     * {@link org.mockito.Mockito#ignoreStubs(java.lang.Object[])}
     */
    default Object[] ignoreStubs(Object... mocks) {
        return Mockito.ignoreStubs(mocks);
    }



    /**
     * Delegate call to public static org.mockito.InOrder org.mockito.Mockito.inOrder(java.lang.Object...)
     * {@link org.mockito.Mockito#inOrder(java.lang.Object[])}
     */
    default InOrder inOrder(Object... mocks) {
        return Mockito.inOrder(mocks);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>)
     * {@link org.mockito.Mockito#mock(java.lang.Class)}
     */
    default <T> T mock(Class<T> classToMock) {
        return Mockito.mock(classToMock);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.stubbing.Answer)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.stubbing.Answer)}
     */
    default <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return Mockito.mock(classToMock, defaultAnswer);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,org.mockito.MockSettings)
     * {@link org.mockito.Mockito#mock(java.lang.Class,org.mockito.MockSettings)}
     */
    default <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return Mockito.mock(classToMock, mockSettings);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.mock(java.lang.Class<T>,java.lang.String)
     * {@link org.mockito.Mockito#mock(java.lang.Class,java.lang.String)}
     */
    default <T> T mock(Class<T> classToMock, String name) {
        return Mockito.mock(classToMock, name);
    }



    /**
     * Delegate call to public static org.mockito.MockingDetails org.mockito.Mockito.mockingDetails(java.lang.Object)
     * {@link org.mockito.Mockito#mockingDetails(java.lang.Object)}
     */
    default MockingDetails mockingDetails(Object toInspect) {
        return Mockito.mockingDetails(toInspect);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.never()
     * {@link org.mockito.Mockito#never()}
     */
    default VerificationMode never() {
        return Mockito.never();
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.only()
     * {@link org.mockito.Mockito#only()}
     */
    default VerificationMode only() {
        return Mockito.only();
    }



    /**
     * Delegate call to public static <T> void org.mockito.Mockito.reset(T...)
     * {@link org.mockito.Mockito#reset(java.lang.Object[])}
     */
    default <T> void reset(T... mocks) {
        Mockito.reset(mocks);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(java.lang.Class<T>)
     * {@link org.mockito.Mockito#spy(java.lang.Class)}
     */
    default <T> T spy(Class<T> classToSpy) {
        return Mockito.spy(classToSpy);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.spy(T)
     * {@link org.mockito.Mockito#spy(java.lang.Object)}
     */
    default <T> T spy(T object) {
        return Mockito.spy(object);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.DeprecatedOngoingStubbing<T> org.mockito.Mockito.stub(T)
     * {@link org.mockito.Mockito#stub(java.lang.Object)}
     */
    default <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
        return Mockito.stub(methodCall);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.VoidMethodStubbable<T> org.mockito.Mockito.stubVoid(T)
     * {@link org.mockito.Mockito#stubVoid(java.lang.Object)}
     */
    default <T> VoidMethodStubbable<T> stubVoid(T mock) {
        return Mockito.stubVoid(mock);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationWithTimeout org.mockito.Mockito.timeout(long)
     * {@link org.mockito.Mockito#timeout(long)}
     */
    default VerificationWithTimeout timeout(long millis) {
        return Mockito.timeout(millis);
    }



    /**
     * Delegate call to public static org.mockito.verification.VerificationMode org.mockito.Mockito.times(int)
     * {@link org.mockito.Mockito#times(int)}
     */
    default VerificationMode times(int wantedNumberOfInvocations) {
        return Mockito.times(wantedNumberOfInvocations);
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
    default <T> T verify(T mock) {
        return Mockito.verify(mock);
    }



    /**
     * Delegate call to public static <T> T org.mockito.Mockito.verify(T,org.mockito.verification.VerificationMode)
     * {@link org.mockito.Mockito#verify(java.lang.Object,org.mockito.verification.VerificationMode)}
     */
    default <T> T verify(T mock, VerificationMode mode) {
        return Mockito.verify(mock, mode);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyNoMoreInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyNoMoreInteractions(java.lang.Object[])}
     */
    default void verifyNoMoreInteractions(Object... mocks) {
        Mockito.verifyNoMoreInteractions(mocks);
    }



    /**
     * Delegate call to public static void org.mockito.Mockito.verifyZeroInteractions(java.lang.Object...)
     * {@link org.mockito.Mockito#verifyZeroInteractions(java.lang.Object[])}
     */
    default void verifyZeroInteractions(Object... mocks) {
        Mockito.verifyZeroInteractions(mocks);
    }



    /**
     * Delegate call to public static <T> org.mockito.stubbing.OngoingStubbing<T> org.mockito.Mockito.when(T)
     * {@link org.mockito.Mockito#when(java.lang.Object)}
     */
    default <T> OngoingStubbing<T> when(T methodCall) {
        return Mockito.when(methodCall);
    }



    /**
     * Delegate call to public static org.mockito.MockSettings org.mockito.Mockito.withSettings()
     * {@link org.mockito.Mockito#withSettings()}
     */
    default MockSettings withSettings() {
        return Mockito.withSettings();
    }



}