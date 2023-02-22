/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.engine;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class TestPlanValidator {

    public static void verify(JSONObject value, String planType) {
        if (StringUtils.isEmpty(planType)) {
            throw new RuntimeException("测试计划校验异常，数据中不包含关键字：testclass");
        }
        planType = planType.replace("_", "");
        String schema = ResourceReader.read(String.format("json-schema/%s.json", planType.toLowerCase(Locale.ROOT)));
        String message = Optional.of(validJson(value, schema)).orElse("");
        if (StringUtils.isNotEmpty(message)) {
            throw new RuntimeException(planType + " 校验异常, " + message);
        }
    }

    /**
     * 使用开源工具https://github.com/java-json-tools/json-schema-validator校验json
     *
     * @param object     被校验对象
     * @param validParam 校验规则
     * @return 返回失败消息，为空则校验成功
     */
    public static String validJson(JSONObject object, String validParam) {
        try {
            JsonNode schema = JsonLoader.fromString(validParam);
            JsonNode data = JsonLoader.fromString(object.toJSONString());
            final JsonSchema jsonSchema = JsonSchemaFactory.byDefault().getJsonSchema(schema);
            ProcessingReport report = jsonSchema.validate(data);
            StringBuilder sBuilder = new StringBuilder();
            report.forEach(pm -> {
                if (LogLevel.ERROR.equals(pm.getLogLevel())) {
                    JsonNode jsonNode = pm.asJson();
                    JsonNode key = Optional.ofNullable(jsonNode)
                            .map(o -> o.get("instance"))
                            .map(r -> r.get("pointer"))
                            .orElse(null);
                    if (Objects.nonNull(key) && StringUtils.isNotEmpty(key.asText())) {
                        sBuilder.append("errorKey: ")
                                .append(key.asText()).append("; ");
                    }
                    sBuilder.append("errorMessage: ").append(pm.getMessage());
                }
            });
            return sBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public record ResourceReader() {

        public static String read(String path) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream is = classLoader.getResourceAsStream(path);
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
