package rabbitmq;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Consumer {

    private static final String host = "localhost";


    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        var factory = new ConnectionFactory();
        factory.setHost(host); // RabbitMQ服务器地址
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        // 创建连接
        try (var connection = factory.newConnection()) {
            // 创建通道
            var channel = connection.createChannel();
            // 声明队列
            channel.queueDeclare("migoo.topic", false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 定义回调函数
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("接收到消息: " + message);
            };
            while (true) {
// 开始消费消息
                channel.basicConsume("migoo.topic", true, deliverCallback, consumerTag -> {
                });
            }


        }


    }
}
