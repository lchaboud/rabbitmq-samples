package org.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Publisher {

    public static void main(String[] args) throws Exception {
        Publisher p = new Publisher();
        p.send("Hello World !", "test2");
    }

    public void send(String messageToSend, String queueName) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection conn = null;
        Channel channel = null;
        try {
            conn = factory.newConnection();
            channel = conn.createChannel();

            // Send message
            channel.basicPublish("", queueName, null, messageToSend.getBytes());
            System.out.println("Message sent : " + messageToSend+" on the queue : "+queueName);

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
