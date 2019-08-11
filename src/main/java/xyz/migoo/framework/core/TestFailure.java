package xyz.migoo.framework.core;


import xyz.migoo.exception.AssertionFailure;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author xiaomi
 * @date 2019-08-10 14:53
 */
public class TestFailure {

    protected AbstractTest fFailedTest;
    protected Throwable fThrownException;


    /**
     * Constructs a TestFailure with the given test and exception.
     */
    public TestFailure(AbstractTest failedTest, Throwable thrownException) {
        fFailedTest= failedTest;
        fThrownException= thrownException;
    }
    /**
     * Gets the failed test.
     */
    public AbstractTest failedTest() {
        return fFailedTest;
    }
    /**
     * Gets the thrown exception.
     */
    public Throwable thrownException() {
        return fThrownException;
    }
    /**
     * Returns a short description of the failure.
     */
    @Override
    public String toString() {
        return fFailedTest + ": " + fThrownException.getMessage();
    }

    public String trace() {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        fThrownException.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
    public String exceptionMessage() {
        return fThrownException.getMessage();
    }
    public boolean isFailure() {
        return thrownException() instanceof AssertionFailure;
    }
}
