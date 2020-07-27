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


package core.xyz.migoo.sampler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public class TestResult {

    private final List<AbstractTest> fTests;
    private final List<TestFailure> eTests;
    private final List<AbstractTest> skipTests;
    private final List<AbstractTest> sTests;
    private final String rName;
    private final int fRunTests;
    private long startTime;
    private long endTime;
    private boolean fStop;

    public TestResult(int fRunTests, String name) {
        this.fTests = new ArrayList<>();
        this.eTests = new ArrayList<>();
        this.skipTests= new ArrayList<>();
        this.sTests = new ArrayList<>();
        this.fRunTests= fRunTests;
        this.fStop= false;
        this.rName = name;
    }

    /**
     * Adds an error to the list of errors. The passed in exception
     * caused the error.
     */
    synchronized void addError(AbstractTest test, Throwable t) {
        eTests.add(new TestFailure(test, t));
    }

    /**
     * Adds a failure to the list of failures. The passed in exception
     * caused the failure.
     */
    synchronized void addFailure(AbstractTest test) {
        fTests.add(test);
    }

    /**
     * Adds a skipped to the list of skips. The passed in exception
     * caused the failure.
     */
    synchronized void addSkip(AbstractTest test) {
        skipTests.add(test);
    }

    /**
     * Adds a success to the list of skips. The passed in exception
     * caused the failure.
     */
    synchronized void addSuccess(AbstractTest test) {
        sTests.add(test);
    }

    /**
     * Gets the number of detected errors.
     */
    public synchronized int errorCount() {
        return eTests.size();
    }

    /**
     * Returns the errors
     */
    public synchronized List<TestFailure> errors() {
        return eTests;
    }

    /**
     * Gets the number of detected failures.
     */
    public synchronized int failureCount() {
        return fTests.size();
    }

    /**
     * Returns the failures
     */
    public synchronized List<AbstractTest> failures() {
        return fTests;
    }

    /**
     * Gets the number of detected skips.
     */
    public synchronized int skipCount() {
        return skipTests.size();
    }

    /**
     * Returns an Enumeration for the skips
     */
    public synchronized List<AbstractTest> skips() {
        return skipTests;
    }

    /**
     * Gets the number of detected successes.
     */
    public synchronized int successCount() {
        return sTests.size();
    }

    /**
     * Returns the success
     */
    public synchronized List<AbstractTest> success() {
        return sTests;
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
