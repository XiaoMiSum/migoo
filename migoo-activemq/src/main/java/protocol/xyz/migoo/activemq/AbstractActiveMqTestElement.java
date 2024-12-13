/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package protocol.xyz.migoo.activemq;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import protocol.xyz.migoo.activemq.config.ActiveMqDefaults;
import protocol.xyz.migoo.activemq.util.ActiveMqConstantsInterface;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 20:35
 */
public abstract class AbstractActiveMqTestElement extends AbstractTestElement implements ActiveMqConstantsInterface {

    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public void testStarted() {
        var other = (ActiveMqDefaults) getVariables().get(ACTIVEMQ_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(ACTIVEMQ_USERNAME, other.get(ACTIVEMQ_USERNAME));
            setProperty(ACTIVEMQ_PASSWORD, other.get(ACTIVEMQ_PASSWORD));
            setProperty(BROKER_URL, other.get(BROKER_URL));
            setProperty(ACTIVEMQ_TOPIC, other.get(ACTIVEMQ_TOPIC));
            setProperty(ACTIVEMQ_QUEUE, other.get(ACTIVEMQ_QUEUE));
            setProperty(ACTIVEMQ_MESSAGE, other.get(ACTIVEMQ_MESSAGE));
        }
        this.buildConnectionFactory();
    }

    protected SampleResult execute(SampleResult result) {
        result.setTestClass(this.getClass());
        result.sampleStart();
        result.setSamplerData(getRequestString());
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            var destination = get(ACTIVEMQ_QUEUE) != null ? session.createQueue(getPropertyAsString(ACTIVEMQ_QUEUE))
                    : session.createTopic(getPropertyAsString(ACTIVEMQ_TOPIC));
            producer = session.createProducer(destination);
            var textMessage = session.createTextMessage(getPropertyAsString(ACTIVEMQ_MESSAGE));
            producer.send(textMessage);
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private void buildConnectionFactory() {
        var username = (String) get(ACTIVEMQ_USERNAME, ActiveMQConnection.DEFAULT_USER);
        var password = (String) get(ACTIVEMQ_PASSWORD, ActiveMQConnection.DEFAULT_PASSWORD);
        var url = (String) get(BROKER_URL, ActiveMQConnection.DEFAULT_BROKER_URL);
        factory = new ActiveMQConnectionFactory(username, password, url);
    }

    public void testEnded() {
        if (producer != null) {
            try {
                producer.close();
            } catch (Exception ignored) {
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (Exception ignored) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignored) {
            }
        }
    }

    private String getRequestString() {
        var data = new JSONObject();
        data.put(ACTIVEMQ_USERNAME, get(ACTIVEMQ_USERNAME));
        data.put(ACTIVEMQ_PASSWORD, get(ACTIVEMQ_PASSWORD));
        data.put(BROKER_URL, get(BROKER_URL));
        data.put(ACTIVEMQ_TOPIC, get(ACTIVEMQ_TOPIC));
        data.put(ACTIVEMQ_QUEUE, get(ACTIVEMQ_QUEUE));
        data.put(ACTIVEMQ_MESSAGE, get(ACTIVEMQ_MESSAGE));
        return data.toJSONString();
    }
}
