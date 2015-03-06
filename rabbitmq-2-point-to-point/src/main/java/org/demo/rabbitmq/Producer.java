package org.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class Producer {

    public static void main(String[] args) throws Exception {
        Producer p = new Producer();
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

            for(int j=0; j<20; j++) {
                int num = new Random(100).nextInt();
                if(num <0) {num = -num; }
                for (int i=0; i < num; i++) {

                    // Send message
                    channel.basicPublish("", queueName, null, messageToSend.getBytes());
                    System.out.println("Message sent : " + messageToSend + " on the queue : " + queueName);
                }

                Thread.sleep(num);
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
