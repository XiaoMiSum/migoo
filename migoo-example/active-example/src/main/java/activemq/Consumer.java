package activemq;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {


    private static final String brokerURL = "tcp://127.0.0.1:61616";

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
        factory.setUserName("artemis");
        factory.setPassword("artemis");
        Connection connection = factory.createConnection();
        connection.start();
        queue(connection);
        topic(connection);
    }

    public static void queue(Connection connection) throws Exception {
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createQueue("migoo.queue");
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("queue：" + textMessage.getText());
                textMessage.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public static void topic(Connection connection) throws JMSException {

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("migoo.topic");

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("topic：" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

}
