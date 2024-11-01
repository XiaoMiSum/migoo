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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.apache.commons.lang3.StringUtils;
import util.xyz.migoo.reader.ReaderFactor;

import java.util.Locale;
import java.util.Optional;

public class TestPlanValidator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void verify(JSONObject value, String element) {
        if (StringUtils.isBlank(element)) {
            return;
        }
        var path = String.format("json-schema/%s.json", element.toLowerCase(Locale.ROOT));
        var schema = ReaderFactor.getReader(true, path).read();
        var message = Optional.of(validJson(value, schema)).orElse("");
        if (StringUtils.isNotEmpty(message)) {
            throw new RuntimeException(element + " 校验异常, " + message);
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
            var factory = JsonSchemaFactory
                    .builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012))
                    .objectMapper(mapper)
                    .build();
            var schema = factory.getSchema(validParam);
            var messages = schema.validate(mapper.readTree(object.toJSONString())).iterator();
            var message = new StringBuilder();
            while (messages.hasNext()) {
                message.append(message.isEmpty() ? "errorMessage: " : ", ").append(messages.next().getMessage());
            }
            return message.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
