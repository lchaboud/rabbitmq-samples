package org.demo.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws Exception {
        Consumer consumer = new Consumer();
        consumer.receive("test2");
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
            channel.basicConsume(queueName, false, "myConsumerTag",
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

            while(true) {
                // Infinite loop
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
