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

package example.xyz.migoo.itx;


import example.xyz.migoo.itx.repository.AddScheduleDetail;
import example.xyz.migoo.itx.repository.ElementRepository;
import example.xyz.migoo.itx.repository.GetScheduleDetail;
import example.xyz.migoo.itx.repository.GetScheduleList;
import itestx.xyz.migoo.logic.TestSuite;
import org.junit.jupiter.api.Test;
import xyz.migoo.MiGoo;

/**
 * @author xiaomi
 * Created at 2022/8/24 15:11
 */
public class TestSuiteExample {

    @Test
    public void testSuite() {
        final TestSuite suite = TestSuite.builder("project of migoo.xyz")
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addConfig(ElementRepository.HTTP_DEFAULT_HOST)
                .addPreprocessor(ElementRepository.PREPROCESS_GET_TOKEN)
                .addTestSet(GetScheduleList.TESTSETS)
                .addTestSet(GetScheduleDetail.TESTSETS)
                .addTestSet(AddScheduleDetail.TESTSETS)
                .build();
        MiGoo.start(suite);
    }
}
