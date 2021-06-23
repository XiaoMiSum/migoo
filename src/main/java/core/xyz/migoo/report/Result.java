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

package core.xyz.migoo.report;

import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.variables.MiGooVariables;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author mi.xiao
 * @date 2021/6/15 20:44
 */
public class Result implements Serializable {


    private boolean success = true;

    private final String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<SampleResult> preprocessorResults;

    private List<SampleResult> postprocessorResults;

    private List<Result> subResults;

    private Throwable throwable;

    private MiGooVariables variables;

    public Result(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<Result> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<Result> subResults) {
        this.subResults = subResults;
    }

    public void setSuccessful(boolean success) {
        this.success = success;
    }

    public boolean isSuccessful() {
        return success;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void sampleStart() {
        if (startTime == null) {
            setStartTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public void sampleEnd() {
        if (endTime == null) {
            setEndTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public boolean isException() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.sampleStart();
        if (throwable != null) {
            this.success = false;
            this.throwable = throwable;
        }
        this.sampleEnd();
    }

    public List<SampleResult> getPreprocessorResults() {
        return preprocessorResults;
    }

    public void setPreprocessorResults(List<SampleResult> preprocessorResults) {
        this.preprocessorResults = preprocessorResults;
    }

    public List<SampleResult> getPostprocessorResults() {
        return postprocessorResults;
    }

    public void setPostprocessorResults(List<SampleResult> postprocessorResults) {
        this.postprocessorResults = postprocessorResults;
    }

    public MiGooVariables getVariables() {
        return variables;
    }

    public void setVariables(MiGooVariables variables) {
        this.variables = variables;
    }
}
