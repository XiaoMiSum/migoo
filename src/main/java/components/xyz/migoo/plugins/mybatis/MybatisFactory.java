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

package components.xyz.migoo.plugins.mybatis;

import core.xyz.migoo.plugin.PluginFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xiaomi
 * @date 2020/1/4 10:55
 */
public final class MybatisFactory {

    public static <T> T mapper(Class<? extends Mapper> clazz, String environment) {
        try {
            SqlSessionFactory sqlSessionFactory = ((Mybatis) PluginFactory.get("Mybatis")).getSqlSession(environment);
            SqlSession sqlSession = sqlSessionFactory.openSession();
            Mapper mapper = sqlSession.getMapper(clazz);
            return (T) MapperProxy.bind(mapper);
        } catch (Exception e) {
            throw new RuntimeException("init mapper exception. ", e);
        }
    }

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
