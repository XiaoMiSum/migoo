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

package example.xyz.migoo;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.report.Result;
import xyz.migoo.MiGoo;
import xyz.migoo.readers.ReaderException;
import xyz.migoo.readers.ReaderFactory;

/**
 * @author mi.xiao
 * @date 2021/2/28 13:37
 */
public class Example {

    public static void main(String[] args) throws ReaderException {
        /* 执行示例前，请先开启Mysql，创建 users 表，并插入几条测试数据
        修改 example/configelements.yaml 中 的jdbc配置
         创建表
            DROP TABLE IF EXISTS `users`;
            CREATE TABLE `users` (
                `id` int(11) NOT NULL AUTO_INCREMENT,
                `status` tinyint(1) DEFAULT '1' COMMENT '1：启用，0：禁用',
                `user_name` varchar(50) NOT NULL COMMENT '登录名',
                `real_name` varchar(10) NOT NULL COMMENT '姓名',
                `password` varchar(50) NOT NULL,
                `salt` varchar(50) NOT NULL,
                `create_time` datetime DEFAULT NULL,
                `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (`id`)
            ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';
            insert into users(user_name, real_name, password, salt) values("u_test1", "r_test1", "p_test1", "a_test1");
            insert into users(user_name, real_name, password, salt) values("u_test2", "r_test2", "p_test2", "a_test2");
            insert into users(user_name, real_name, password, salt) values("u_test3", "r_test3", "p_test3", "a_test3");

         如果想要运行 redis示例，请取消 example/configelements.yam 中 RedisDataSource相关配置的注释，并修改为实际值
         如果想要运行 dubbo示例，请取消 example/configelements.yam 中 DubboDefaults相关配置的注释，并修改为实际值，然后启动 zookeeper服务，启动 example.dubbo.DubboApplication
        */
        JSONObject yaml = (JSONObject) ReaderFactory.getReader("classpath://example/standardproject.yaml").read();
        Result result = new MiGoo(yaml).run();
    }

}
