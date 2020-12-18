/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package core.xyz.migoo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public class SuiteResult extends Result implements ISuiteResult {

    private List<IResult> results;

    private int passedCount;

    private int notPassedCount;

    private int errorCount;

    private int skippedCount;

    @Override
    public List<IResult> getTestResults() {
        return this.results;
    }

    @Override
    public void setTestResults(List<IResult> results) {
        this.results = results;
    }

    @Override
    public void addTestResult(IResult result) {
        if (result != null) {
            if (this.results == null) {
                this.results = new ArrayList<>();
            }
            this.results.add(result);
            if (result.isPassed()) {
                passedCount += 1;
            }else if (result.isError()) {
                errorCount += 1;
            } else if (result.isNotPassed()) {
                notPassedCount += 1;
            } else if (result.isSkipped()) {
                skippedCount += 1;
            }
        }
    }

    @Override
    public int size() {
        return results == null ? 0 : results.size();
    }

    @Override
    public int getPassedCount() {
        return passedCount;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getNotPassedCount() {
        return notPassedCount;
    }

    @Override
    public int getSkippedCount() {
        return skippedCount;
    }
}
