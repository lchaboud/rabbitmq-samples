package org.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer {

    public static void main(String[] args) throws Exception {
        Consumer c = new Consumer();
        c.receive("test2");
    }

    public void receive(String queueName) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();

            // Create queue
            channel.queueDeclare(queueName, false, false, false, null);

            // Declare message consumer
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, true, consumer);

            System.out.println("Receive...");

            while(true) {

                // Receive message
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String messageReceived = new String(delivery.getBody());

                System.out.println("Message received : " + messageReceived);
            }

        } finally {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (conn != null && conn.isOpen()) {
                conn.close();
            }
        }
    }

}
