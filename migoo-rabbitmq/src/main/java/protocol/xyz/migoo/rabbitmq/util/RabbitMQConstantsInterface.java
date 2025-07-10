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

package protocol.xyz.migoo.rabbitmq.util;


/**
 * @author xiaomi
 * Created in 2021/11/11 11:08
 */
public interface RabbitMQConstantsInterface {

    String RABBIT_MQ_DEFAULT = "migoo_protocol_rabbitmq_element_defaults";

    String VIRTUAL_HOST = "virtual_host";

    String HOST = "host";

    String PORT = "port";

    String USERNAME = "username";

    String PASSWORD = "password";

    String TIMEOUT = "timeout";

    String QUEUE_CONFIG = "queue";

    String QUEUE_NAME = "name";

    String QUEUE_DURABLE = "durable";

    String QUEUE_EXCLUSIVE = "exclusive";

    String QUEUE_AUTO_DELETE = "auto_delete";

    String QUEUE_ARGUMENTS = "arguments";

    String EXCHANGE = "exchange";
    String EXCHANGE_NAME = "name";

    String EXCHANGE_TYPE = "type";
    String EXCHANGE_TYPE_FANOUT = "fanout";
    String EXCHANGE_TYPE_DIRECT = "direct";
    String EXCHANGE_TYPE_TOPIC = "topic";
    String EXCHANGE_TYPE_HEADERS = "headers";

    String EXCHANGE_ROUTING_KEY = "routing_key";

    String PROPS = "props";

    String PROPS_CONTENT_TYPE = "content_type";
    String PROPS_CONTENT_ENCODING = "content_encoding";
    String PROPS_HEADERS = "headers";
    String PROPS_DELIVERY_MODE = "delivery_mode";
    String PROPS_PRIORITY = "priority";
    String PROPS_CORRELATION_ID = "correlation_id";
    String PROPS_REPLY_TO = "reply_to";
    String PROPS_EXPIRATION = "expiration";
    String PROPS_MESSAGE_ID = "message_id";
    String PROPS_TIMESTAMP = "timestamp";
    String PROPS_TYPE = "type";
    String PROPS_USER_ID = "user_id";
    String PROPS_APP_ID = "app_id";
    String PROPS_CLUSTER_ID = "cluster_id";

    String MESSAGE = "message";


}
