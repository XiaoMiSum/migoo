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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.plugin.Plugin;
import core.xyz.migoo.vars.Vars;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2020/8/30 13:27
 */
public class Mybatis implements Plugin {

    private String resource;

    private String defaultEnv;

    @Override
    public void initialize(JSONObject config) throws Exception {
        if (config != null && !config.isEmpty()) {
            StringBuilder envStr = new StringBuilder();
            for (Map.Entry<String, Object> entry : config.getJSONObject("envs").entrySet()) {
                JSONObject env = (JSONObject) entry.getValue();
                envStr.append("<environment id=\"").append(entry.getKey()).append("\">")
                        .append("<transactionManager type=\"JDBC\"/>")
                        .append("<dataSource type=\"POOLED\">")
                        .append("<property name=\"driver\" value=\"com.mysql.cj.jdbc.Driver\"/>")
                        .append("<property name=\"url\" value=\"").append(env.get("url")).append("\"/>")
                        .append("<property name=\"username\" value=\"").append(env.get("username")).append("\"/>")
                        .append("<property name=\"password\" value=\"").append(env.get("password")).append("\"/>")
                        .append("</dataSource>")
                        .append("</environment>");
            }
            JSONArray mappers = config.getJSONArray("mappers");
            StringBuilder mapperStr = new StringBuilder();
            for (Object mapper : mappers) {
                mapperStr.append("<mapper url=\"").append(mapper).append("\" /> \n");
            }
            this.defaultEnv = config.getString("default");
            resource = String.format(TEMPLATE, this.defaultEnv, envStr, mapperStr);
        }
    }

    @Override
    public void close() {
        for (Map.Entry<String, SqlSessionFactory> entry : SQL_SESSION_FACTORY.entrySet()) {
            entry.getValue().openSession().close();
        }
        SQL_SESSION_FACTORY.clear();
    }

    private final Map<String, SqlSessionFactory> SQL_SESSION_FACTORY = new HashMap<>();

    private SqlSessionFactory getSqlSession(Object environment) {
        String env =  environment == null || "".equals(environment) ? this.defaultEnv : String.valueOf(environment);
        SqlSessionFactory sqlSessionFactory = SQL_SESSION_FACTORY.get(env);
        if (sqlSessionFactory == null) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(new ByteArrayInputStream(resource.getBytes()), env);
            SQL_SESSION_FACTORY.put(env, sqlSessionFactory);
        }
        return sqlSessionFactory;
    }

    public <T> T mapper(Class<? extends Mapper> clazz) {
        return mapper(clazz, null);
    }

    public <T> T mapper(Class<? extends Mapper> clazz, Object environment) {
        SqlSessionFactory sqlSessionFactory = this.getSqlSession(environment);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Mapper mapper = sqlSession.getMapper(clazz);
            return (T) MapperProxy.bind(mapper);
        } catch (BindingException e) {
            if (e.getMessage().contains("is not known to the MapperRegistry")) {
                sqlSession.getConfiguration().addMapper(clazz);
                Mapper mapper = sqlSession.getMapper(clazz);
                return (T) MapperProxy.bind(mapper);
            }
            throw new RuntimeException("init mapper exception. ", e);
        } catch (Exception e) {
            throw new RuntimeException("init mapper exception. ", e);
        }
    }

    private static final String TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE configuration\n" +
            "        PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\"\n" +
            "        \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n" +
            "<configuration>\n" +
            "    <environments default=\"%s\">\n" +
            "        %s\n" +
            "    </environments>\n" +
            "    <mappers> \n" +
            "        %s" +
            "    </mappers> " +
            "</configuration>";


    private static class MapperProxy implements InvocationHandler {
        private final Mapper mapper;

        private MapperProxy(Mapper mapper) {
            this.mapper = mapper;
        }

        private static Mapper bind(Mapper mapper) {
            return (Mapper) Proxy.newProxyInstance(mapper.getClass().getClassLoader(),
                    mapper.getClass().getInterfaces(), new MapperProxy(mapper));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(mapper, args);
        }
    }
}
