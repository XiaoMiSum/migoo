/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package xyz.migoo.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public class TestResult {

    private List<AbstractTest> fFailures;
    private List<TestFailure> fErrors;
    private List<AbstractTest> fSkips;
    private List<AbstractTest> fSuccess;
    private String rName;
    private int fRunTests;
    private long startTime;
    private long endTime;
    private boolean fStop;

    public TestResult(int fRunTests, String name) {
        this.fFailures= new ArrayList<>();
        this.fErrors= new ArrayList<>();
        this.fSkips= new ArrayList<>();
        this.fSuccess= new ArrayList<>();
        this.fRunTests= fRunTests;
        this.fStop= false;
        this.rName = name;
    }

    /**
     * Adds an error to the list of errors. The passed in exception
     * caused the error.
     */
    synchronized void addError(AbstractTest test, Throwable t) {
        fErrors.add(new TestFailure(test, t));
    }

    /**
     * Adds a failure to the list of failures. The passed in exception
     * caused the failure.
     */
    synchronized void addFailure(AbstractTest test) {
        fFailures.add(test);
    }

    /**
     * Adds a skipped to the list of skips. The passed in exception
     * caused the failure.
     */
    synchronized void addSkip(AbstractTest test) {
        fSkips.add(test);
    }

    /**
     * Adds a success to the list of skips. The passed in exception
     * caused the failure.
     */
    synchronized void addSuccess(AbstractTest test) {
        fSuccess.add(test);
    }

    /**
     * Gets the number of detected errors.
     */
    public synchronized int errorCount() {
        return fErrors.size();
    }

    /**
     * Returns the errors
     */
    public synchronized List<TestFailure> errors() {
        return fErrors;
    }

    /**
     * Gets the number of detected failures.
     */
    public synchronized int failureCount() {
        return fFailures.size();
    }

    /**
     * Returns the failures
     */
    public synchronized List<AbstractTest> failures() {
        return fFailures;
    }

    /**
     * Gets the number of detected skips.
     */
    public synchronized int skipCount() {
        return fSkips.size();
    }

    /**
     * Returns an Enumeration for the skips
     */
    public synchronized List<AbstractTest> skips() {
        return fSkips;
    }

    /**
     * Gets the number of detected successes.
     */
    public synchronized int successCount() {
        return fSuccess.size();
    }

    /**
     * Returns the success
     */
    public synchronized List<AbstractTest> success() {
        return fSuccess;
    }

    /**
     * Gets the number of test case.
     */
    public synchronized int caseCount() {
        return fRunTests;
    }

    /**
     * Gets the TestResult name.
     */
    public String getName() {
        return rName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean fStop() {
        return fStop;
    }

    public void fStop(boolean fStop) {
        this.fStop = fStop;
    }
}
