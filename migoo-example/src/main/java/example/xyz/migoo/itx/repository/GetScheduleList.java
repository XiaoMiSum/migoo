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

package example.xyz.migoo.itx.repository;

import itestx.xyz.migoo.element.Extractors;
import itestx.xyz.migoo.element.Samplers;
import itestx.xyz.migoo.logic.TestSets;
import itestx.xyz.migoo.logic.Testcase;

import static example.xyz.migoo.itx.repository.ValidatorRepository.DATA_NOT_EMPTY;
import static example.xyz.migoo.itx.repository.ValidatorRepository.VERIFY_HTTP_STATUS;

/**
 * @author xiaomi
 * Created at 2022/9/25 16:14
 */
public class GetScheduleList {

    public final static Samplers SAMPLER = Samplers.builder().http("request api: get task list")
            .api("/api/task/schedule")
            .addExtractor(Extractors.builder().json("scheduleId").jsonPath("$.data[0].id").matchNo(0))
            .addValidator(VERIFY_HTTP_STATUS, DATA_NOT_EMPTY);

    public static final Testcase TESTCASE_GET_TASK_LIST_1 = Testcase.builder("测试用例1：获取任务列表接口1")
            .addVariable("username2", "migoo")
            .addSampler(SAMPLER)
            .build();

    public static final Testcase TESTCASE_GET_TASK_LIST_2 = Testcase.builder("测试用例2：获取任务列表接口2")
            .addVariable("username2", "migoo")
            .addSampler(SAMPLER)
            .build();

    public static final TestSets TESTSETS = TestSets.builder("获取任务列表接口测试用例集合")
            .addVariable("username1", "migoo")
            .addConfig(ElementRepository.HTTP_DEFAULT_HEADER)
            .addTestcase(TESTCASE_GET_TASK_LIST_1, TESTCASE_GET_TASK_LIST_2)
            .build();
}
