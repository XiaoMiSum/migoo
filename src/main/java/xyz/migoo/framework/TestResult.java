package xyz.migoo.framework;

import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.SkippedRun;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public class TestResult {

    private List<TestFailure> fFailures;
    private List<TestFailure> fErrors;
    private List<TestFailure> fSkips;
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
    synchronized void addFailure(AbstractTest test, AssertionFailure t) {
        fFailures.add(new TestFailure(test, t));
    }

    /**
     * Adds a skipped to the list of skips. The passed in exception
     * caused the failure.
     */
    synchronized void addSkip(AbstractTest test, SkippedRun skip) {
        fSkips.add(new TestFailure(test, skip));
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
    public synchronized List<TestFailure> failures() {
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
    public synchronized List<TestFailure> skips() {
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
}
