/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package xyz.migoo.framework.assertions;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.config.CaseKeys;

import static xyz.migoo.framework.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019-04-13 22:12
 */
public class AssertionFactory {

    private AssertionFactory(){}

    public static AbstractAssertion getAssertion(String check){
        switch (assertionType(check)){
            case CaseKeys.JSON:
                return new JSONAssertion(check);
            case CaseKeys.STATUS:
                return new ResponseCodeAssertion();
            case CaseKeys.BODY:
                return new ResponseAssertion();
            default:
                try {
                    return (AbstractAssertion)Class.forName(check).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ExecuteError("assert class not found: " + check);
                }
        }
    }

    private static String assertionType(String searchChar){
        if (CHECK_BODY.contains(searchChar)) {
            return CaseKeys.BODY;
        }
        if (CHECK_CODE.contains(searchChar)) {
            return CaseKeys.STATUS;
        }
        if (isJson(searchChar)) {
            return CaseKeys.JSON;
        }
        return searchChar;
    }

}
