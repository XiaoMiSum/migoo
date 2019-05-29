package xyz.migoo.test;


import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.AbstractAssertion;
import xyz.migoo.assertions.AssertionFactory;

import javax.xml.bind.ValidationException;

/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class Test extends Base{

    @org.junit.Test
    public void testFunc() throws ValidationException {
        JSONObject data = new JSONObject(1);
        data.put("expect", 1);
        AbstractAssertion assertion = AssertionFactory.getAssertion("package.xyz.migoo.test.extend.Test");
        assertion.setActual("package.xyz.migoo.test.extend.Test");
        Object result = assertion.assertThat(data);
        System.out.println(result);
    }

    @org.junit.Test
    public void testApiYaml(){
        //runner.execute("../TestCase/test_case.yml");
    }

    @org.junit.Test
    public void testApiJson1(){
        //runner.execute("../TestCase/test_case.json");
    }

    @org.junit.Test
    public void testApiCaseSet(){
        String json = "{" +
                "  \"name\": \"the-test-suite-name\"," +
                "  \"config\": {" +
                "    \"variables\":{" +
                "      \"user\": \"test\"" +
                "    }," +
                "    \"beforeClass\": [\"__hook1(beforeClass)\"]," +
                "    \"request\": {" +
                "      \"url\": \"http://migoo.xyz/api/login\"," +
                "      \"method\": \"post\"," +
                "      \"headers\": {" +
                "        \"Content-Type\": \"application/json\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"case\": [" +
                "    {" +
                "      \"title\": \"忽略执行\"," +
                "      \"ignore\": \"1\"," +
                "      \"variables\": {" +
                "        \"pwd\": \"123456\"" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after,before)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"migoo1\"," +
                "        \"password\": \"${pwd}\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": \"411\", \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": \"\", \"types\": \"isEmpty\"}" +
                "      ]" +
                "    }," +
                "    {" +
                "      \"title\": \"未注册的用户\"," +
                "      \"variables\": {" +
                "        \"pwd\": \"123456\"" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after,before)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"migoo1\"," +
                "        \"password\": \"${pwd}\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": \"411\", \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": \"\", \"types\": \"isEmpty\"}" +
                "      ]" +
                "    }," +
                "    {" +
                "      \"title\": \"密码错误\"," +
                "      \"variables\": {" +
                "        \"pwd\": \"__getPwd(${user})\"" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after,before)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"${user}\"," +
                "        \"password\": \"12345\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": 411, \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": null, \"types\": \"isEmpty\"}" +
                "      ]" +
                "    }," +
                "    {" +
                "      \"title\": \"正确的用户名密码\"," +
                "      \"variables\": {" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after,hei)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"${user}\"," +
                "        \"password\": \"123456\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": \"200\", \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": \"\", \"types\": \"isNotNull\"}" +
                "      ]" +
                "    }" +
                "  ]" +
                "}";
        runner.run(json, "");
    }
}
