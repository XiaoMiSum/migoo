/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.report;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author mi.xiao
 * @date 2021/6/15 20:44
 */
public abstract class Result implements Serializable {

    private final String id;

    private String title;

    private boolean success = true;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<Result> subResults;

    private Throwable throwable;


    public Result(String title) {
        this(null, title);
    }

    public Result(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public void testStart() {
        if (startTime == null) {
            setStartTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public void testEnd() {
        if (endTime == null) {
            setEndTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Result> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<Result> subResults) {
        this.subResults = subResults;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccessful(boolean success) {
        this.success = success;
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

    public boolean isException() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.testStart();
        if (throwable != null) {
            this.success = false;
            this.throwable = throwable;
        }
        this.testEnd();
    }

    public String getId() {
        return id;
    }
}
