/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package server.activemq;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.IOException;

public class JmsConsumerTopic {
    // 定义 MQ 连接地址
    private static final String ACTIVE_MQ_URL = "tcp://localhost:61616";
    // 定义主题名称
    private static final String TOPIC_NAME = "migoo.queue";

    public static void main(String[] args) throws JMSException, IOException {
        while (true) {
            // 1、创建连接工厂
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            // 2、通过连接工厂，获得连接 Connection
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            ///3、创建会话
            // 两个参数，第一个叫事务 / 第二个叫签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 4、创建目的地（具体是队列还是主题 Topic）
            Destination topic = session.createQueue(TOPIC_NAME);
            // 5、创建消费者
            MessageConsumer messageConsumer = session.createConsumer(topic);
            // 通过监听的方式来消费消息 MessageConsumer messageConsumer = session.createConsumer(queue)
            messageConsumer.setMessageListener((message) -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        System.out.println("****** 消费者接收到Topic消息 ****** ：" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.in.read();
            // 关闭资源
            messageConsumer.close();
            session.close();
            connection.close();
        }

    }
}