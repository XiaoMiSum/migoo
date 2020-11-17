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


package components.xyz.migoo.assertions;

import core.xyz.migoo.assertions.AbstractAssertion;
import core.xyz.migoo.assertions.rules.Alias;
import org.apache.http.Header;
import xyz.migoo.simplehttp.Response;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-04-13 21:37
 */
@Alias(aliasList = {"response", "ResponseAssertion", "Response_Assertion"})
public class ResponseAssertion extends AbstractAssertion {

    @Override
    public void setActual(Response actual) {
        if (STATUS.contains(field)) {
            this.actual = actual.statusCode();
        } else if (field.toLowerCase().startsWith("headers.") || field.toLowerCase().startsWith("header.")) {
            String s = field.substring(field.indexOf(".") + 1).toLowerCase();
            for (Header header : actual.headers()) {
                if (header.getName().toLowerCase().equals(s)) {
                    this.actual = header.getValue();
                    break;
                }
            }
        } else {
            this.actual = actual.text();
        }
    }

    private static final List<String> STATUS = Arrays.asList("line", "status", "code", "statusCode", "statusLine", "status_code", "status_line");

}
