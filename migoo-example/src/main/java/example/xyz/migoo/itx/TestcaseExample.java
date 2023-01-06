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

import com.alibaba.fastjson2.JSONObject;
import example.xyz.migoo.itx.repository.ElementRepository;
import itestx.xyz.migoo.element.Extractors;
import itestx.xyz.migoo.element.Samplers;
import itestx.xyz.migoo.logic.Testcase;
import org.junit.jupiter.api.Test;
import xyz.migoo.MiGoo;

import static example.xyz.migoo.itx.repository.ValidatorRepository.DATA_NOT_EMPTY;
import static example.xyz.migoo.itx.repository.ValidatorRepository.VERIFY_HTTP_STATUS;

/**
 * @author xiaomi
 * Created at 2022/9/25 16:33
 */
public class TestcaseExample {


    @Test
    public void testGetScheduleListTestcase() {
        final Samplers sampler = Samplers.builder().http("request api: get task list")
                .addHeader("token", "${token}")
                .api("/api/task/schedule")
                .addExtractor(Extractors.builder().json("scheduleId").jsonPath("$.data[0].id").matchNo(0))
                .addValidator(VERIFY_HTTP_STATUS, DATA_NOT_EMPTY);
        Testcase testcase = Testcase.builder("测试用例1：获取任务列表接口1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HOST)
                .addPreprocessor(ElementRepository.PREPROCESS_GET_TOKEN)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addSampler(sampler)
                .build();
        MiGoo.start(testcase);
    }

    @Test
    public void testGetScheduleDetailTestcase1() {
        Samplers getTaskList = Samplers.builder().http("request api: get task list")
                .addHeader("token", "${token}")
                .api("/api/task/schedule")
                .addExtractor(Extractors.builder().json("scheduleId").jsonPath("$.data[0].id").matchNo(0))
                .addValidator(VERIFY_HTTP_STATUS, DATA_NOT_EMPTY);

        Samplers getTaskDetailSampler = Samplers.builder().http("request api: get task detail")
                .addHeader("token", "${token}")
                .api("/api/task/schedule/detail").query(JSONObject.of("scheduleId", "${scheduleId}"))
                .addExtractor(Extractors.builder().json("taskId").jsonPath("$.data.detail.id").matchNo(0))
                .addValidator(VERIFY_HTTP_STATUS, DATA_NOT_EMPTY);

        Testcase testcase = Testcase.builder("多步骤测试用例1：获取任务详情1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HOST)
                .addPreprocessor(ElementRepository.PREPROCESS_GET_TOKEN)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addSampler(getTaskList)
                .addSampler(getTaskDetailSampler)
                .build();
        MiGoo.start(testcase);
    }

    @Test
    public void testAddScheduleDetailTestcase1() {
        Samplers getTaskList = Samplers.builder().http("request api: get task list")
                .addHeader("token", "${token}")
                .api("/api/task/schedule")
                .addExtractor(Extractors.builder().json("scheduleId").jsonPath("$.data[0].id").matchNo(0))
                .addValidator(VERIFY_HTTP_STATUS, DATA_NOT_EMPTY);

        Samplers addTaskDetailSampler = Samplers.builder().http("request api: add task").method("post")
                .api("/api/task/schedule/detail/add")
                .body(JSONObject.of("scheduleId", "${scheduleId}", "count", 1, "title", "__randomString(20,,true)"))
                .addValidator(VERIFY_HTTP_STATUS);

        Testcase testcase = Testcase.builder("多步骤测试用例1：新增任务1")
                .addConfig(ElementRepository.HTTP_DEFAULT_HOST)
                .addPreprocessor(ElementRepository.PREPROCESS_GET_TOKEN)
                .addVariable("username", "migoo")
                .addVariable("password", "123456qq")
                .addSampler(getTaskList)
                .addSampler(addTaskDetailSampler)
                .build();
        MiGoo.start(testcase);
    }

}
