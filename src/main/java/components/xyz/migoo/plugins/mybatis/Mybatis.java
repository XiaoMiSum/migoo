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

package components.xyz.migoo.plugins.mybatis;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.plugin.IPlugin;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2020/8/30 13:27
 */
public class Mybatis implements IPlugin {

    private InputStream resource;

    @Override
    public void init(JSONObject config) throws Exception {
        if (resource == null) {
            JSONObject envs = config.getJSONObject("envs");
            StringBuilder envStr = new StringBuilder();
            for (Map.Entry<String, Object> entry : envs.entrySet()) {
                JSONObject env = (JSONObject) entry.getValue();
                envStr.append("<environment id=\"").append(entry.getKey()).append("\">")
                        .append("<transactionManager type=\"JDBC\"/>")
                        .append("<dataSource type=\"POOLED\">")
                        .append("<property name=\"driver\" value=\"com.mysql.cj.jdbc.Driver\"/>")
                        .append("<property name=\"url\" value=\"").append(env.getString("url")).append("\"/>")
                        .append("<property name=\"username\" value=\"").append(env.getString("username")).append("\"/>")
                        .append("<property name=\"password\" value=\"").append(env.getString("password")).append("\"/>")
                        .append("</dataSource>")
                        .append("</environment>");
            }
            String xml = String.format(template, config.getString("default"), envStr);
            resource = new ByteArrayInputStream(xml.getBytes());
        }
    }

    private final Map<String, SqlSessionFactory> SQL_SESSION_FACTORY = new HashMap<>();

    public SqlSessionFactory getSqlSession(Object environment) {
        String env = String.valueOf(environment);
        SqlSessionFactory sqlSessionFactory = SQL_SESSION_FACTORY.get(env);
        if (sqlSessionFactory == null) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource, env);
            SQL_SESSION_FACTORY.put(env, sqlSessionFactory);
        }
        return sqlSessionFactory;
    }

    private static final String template = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE configuration\n" +
            "        PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\n" +
            "        \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n" +
            "<configuration>\n" +
            "    <environments default=\"%s\">\n" +
            "        %s\n" +
            "    </environments>\n" +
            "</configuration>";
}
