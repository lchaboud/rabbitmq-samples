package org.demo.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class RabbitMQTest {

    @Test
    public void test() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();

            String QUEUE_NAME = "test";

            // Create queue
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Declare message consumer
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException
                        {
                            String messageReceived = new String(body);
                            System.out.println("Message received : " + messageReceived);
                        }
                    });

            for(int i=0; i<100; i++) {
                // Send message
                String messageToSend = "Message num = "+ (i+1);
                channel.basicPublish("", QUEUE_NAME, null, messageToSend.getBytes());
                System.out.println("Message sent : " + messageToSend);
            }

            Thread.sleep(100);

        } finally {
            if(channel != null && channel.isOpen()) {
                channel.close();
            }
            if(conn != null && conn.isOpen()) {
                conn.close();
            }
        }
    }

}
