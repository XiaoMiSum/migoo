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
import itestx.xyz.migoo.logic.TestSets;
import itestx.xyz.migoo.logic.Testcase;
import org.junit.jupiter.api.Test;
import xyz.migoo.MiGoo;

import static example.xyz.migoo.itx.repository.ElementRepository.HTTP_DEFAULT_HOST;
import static example.xyz.migoo.itx.repository.ElementRepository.PREPROCESS_GET_TOKEN;

/**
 * @author xiaomi
 * Created at 2022/9/25 16:32
 */
public class TestSetsExample {

    @Test
    public void testGetScheduleList() {
        Testcase testcase1 = Testcase.builder("测试用例1：获取任务列表接口1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(GetScheduleList.SAMPLER)
                .build();

        Testcase testcase2 = Testcase.builder("测试用例2：获取任务列表接口2")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(GetScheduleList.SAMPLER)
                .build();

        TestSets testSets = TestSets.builder("获取任务列表接口测试用例集合")
                .addConfig(HTTP_DEFAULT_HOST)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addPreprocessor(PREPROCESS_GET_TOKEN)
                .addTestcase(testcase1, testcase2)
                .build();

        MiGoo.start(testSets);
    }

    @Test
    public void testGetScheduleDetail() {
        Testcase testcase1 = Testcase.builder("多步骤测试用例1：获取任务详情1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(GetScheduleDetail.SAMPLER)
                .build();

        Testcase testcase2 = Testcase.builder("多步骤测试用例2：获取任务详情2")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(GetScheduleDetail.SAMPLER)
                .build();

        TestSets testSets = TestSets.builder("获取任务详情测试用例集合")
                .addConfig(HTTP_DEFAULT_HOST)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addPreprocessor(PREPROCESS_GET_TOKEN)
                .addTestcase(testcase1, testcase2)
                .build();

        MiGoo.start(testSets);
    }

    @Test
    public void testAddScheduleDetail() {
        Testcase testcase1 = Testcase.builder("多步骤测试用例1：新增任务1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(AddScheduleDetail.SAMPLER)
                .build();

        Testcase testcase2 = Testcase.builder("多步骤测试用例2：新增任务2")
                .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
                .addSampler(AddScheduleDetail.SAMPLER)
                .build();

        TestSets testSets = TestSets.builder("新增任务测试用例集合")
                .addConfig(HTTP_DEFAULT_HOST)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addPreprocessor(PREPROCESS_GET_TOKEN)
                .addTestcase(testcase1, testcase2)
                .build();

        MiGoo.start(testSets);
    }
}
