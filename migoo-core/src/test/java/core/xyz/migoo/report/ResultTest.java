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

import core.xyz.migoo.variable.MiGooVariables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author xiaomi
 * Created in 2021/10/18 14:26
 */
public class ResultTest {

    private Result result;

    @BeforeEach
    public void beforeEach() {
        result = new Result("test");
    }

    @Test
    public void testResultTitle() {
        assert Objects.equals(result.getTitle(), "test");
    }

    @Test
    public void testResultSuccess() {
        // 默认值 true
        assert result.isSuccessful();
        result.setSuccessful(false);
        assert !result.isSuccessful();
    }

    @Test
    public void testResultThrowable() {
        result.setThrowable(null);
        assert result.isSuccessful();
        assert !result.isException();
        result.setThrowable(new Exception("test"));
        assert !result.isSuccessful();
        assert result.isException();
    }

    @Test
    public void testResultTime() {
        assert result.getStartTime() == null;
        assert result.getEndTime() == null;
        result.sampleStart();
        result.sampleEnd();
        assert !(result.getStartTime() == null);
        assert !(result.getEndTime() == null);
        LocalDateTime startTime = LocalDateTime.now(ZoneId.systemDefault());
        result.setStartTime(startTime);
        LocalDateTime endTime = LocalDateTime.now(ZoneId.systemDefault());
        result.setEndTime(endTime);
        assert result.getStartTime().equals(startTime);
        assert result.getEndTime().equals(endTime);
        result.sampleStart();
        result.sampleEnd();
        assert result.getStartTime().equals(startTime);
        assert result.getEndTime().equals(endTime);
    }

    @Test
    public void testProcessorResults() {
        assert result.getPreprocessorResults() == null;
        assert result.getPostprocessorResults() == null;
        result.setPreprocessorResults(new ArrayList<>());
        result.setPostprocessorResults(new ArrayList<>());
        assert result.getPreprocessorResults() != null;
        assert result.getPostprocessorResults() != null;
    }

    @Test
    public void testSubResults() {
        assert result.getSubResults() == null;
        result.setSubResults(new ArrayList<>());
        assert result.getSubResults() != null;
    }

    @Test
    public void testMigooVariables() {
        assert result.getVariables() == null;
        result.setVariables(new MiGooVariables());
        assert result.getVariables() != null;
    }
}
