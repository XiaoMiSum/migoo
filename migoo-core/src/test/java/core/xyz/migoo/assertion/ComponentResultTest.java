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

package core.xyz.migoo.assertion;

import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author xiaomi
 * Created in 2021/10/18 11:12
 */
public class ComponentResultTest {

    @Test
    public void testAssertionResult1() {
        VerifyResult result = new VerifyResult("test");
        result.setContext("test context");
        result.setSuccessful(true);
        assert result.getName().equals("test");
        assert result.getContext().equals("test context");
        assert result.isSuccessful();
    }

    @Test
    public void testAssertionResult2() {
        VerifyResult result = new VerifyResult("test");
        result.setSuccessful(false);
        assert result.getName().equals("test");
        assert !result.isSuccessful();
    }

    @Test
    public void testAssertionResult3() {
        VerifyResult result = new VerifyResult("test");
        result.setFailureMessage("test false");
        assert result.getName().equals("test");
        assert Objects.equals(result.getContext(), "test false");
        assert !result.isSuccessful();
    }

    @Test
    public void testAssertionResult4() {
        VerifyResult result = new VerifyResult("test");
        result.setFailureMessage(new Exception("test Exception"));
        assert result.getName().equals("test");
        assert result.getContext().contains("java.lang.Exception: test Exception");
        assert !result.isSuccessful();
    }

    @Test
    public void testAssertionResult5() {
        VerifyResult result = new VerifyResult("test");
        assert !result.isSuccessful();
        assert result.getContext() == null;
    }

}
