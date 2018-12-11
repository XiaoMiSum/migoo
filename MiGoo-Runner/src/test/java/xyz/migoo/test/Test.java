package xyz.migoo.test;


/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class Test extends Base{

    @org.junit.Test
    public void testApiYaml(){
        runner.execute("../TestCase/test_case.yml");
    }

    @org.junit.Test
    public void testApiJson1(){
        runner.execute("../TestCase/test_case.json");
    }

    @org.junit.Test
    public void testApiJson2(){
        runner.execute("../TestCase/test_case2.json");
    }

    @org.junit.Test
    public void testApiCaseSet(){
        String json = "{" +
                "  \"name\": \"the test suite name\"," +
                "  \"config\": {" +
                "    \"variables\":{" +
                "      \"user\": \"migoo\"" +
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
                "      \"title\": \"未注册的用户\"," +
                "      \"setUp\": {" +
                "        \"pwd\": \"123456qq\"" +
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
                "      \"setUp\": {" +
                "        \"pwd\": \"__getPwd(${user})\"" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after,before)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"${user}\"," +
                "        \"password\": \"123456\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": 411, \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": null, \"types\": \"isEmpty\"}" +
                "      ]" +
                "    }," +
                "    {" +
                "      \"title\": \"未注册的用户\"," +
                "      \"setUp\": {" +
                "      }," +
                "      \"before\": [\"__hook1(before)\"]," +
                "      \"after\": [\"__hook2(after)\"]," +
                "      \"body\": {" +
                "        \"userName\": \"${user}\"," +
                "        \"password\": \"123456qq\"" +
                "      }," +
                "      \"validate\": [" +
                "        {\"check\": \"status\", \"expect\": 200, \"types\": \"==\"}," +
                "        {\"check\": \"$.status\", \"expect\": \"200\", \"types\": \"eq\"}," +
                "        {\"check\": \"body.data\", \"expect\": \"\", \"types\": \"isNotNull\"}" +
                "      ]" +
                "    }" +
                "  ]" +
                "}";

        runner.execute(json);
        runner.run(json);
    }
}
