package org.demo.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Assert;
import org.junit.Test;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

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
            channel.basicConsume(QUEUE_NAME, true, consumer);

            String messageToSend = "Hello World !";

            // Send message
            channel.basicPublish("", QUEUE_NAME, null, messageToSend.getBytes());
            System.out.println("Message sent : "+messageToSend);

            // Receive message
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String messageReceived = new String(delivery.getBody());

            System.out.println("Message received : "+messageReceived);
            Assert.assertEquals(messageToSend, messageReceived);

            // Delete queue
            channel.queueDelete(QUEUE_NAME);

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
