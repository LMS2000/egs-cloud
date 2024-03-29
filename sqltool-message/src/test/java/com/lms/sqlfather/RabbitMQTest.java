package com.lms.sqlfather;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQTest {

    @Test
    public void connectTest(){
        ConnectionFactory factory=new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("47.120.1.239");
        factory.setPort(5672);
        Channel channel= null;
        try {
            channel = factory.newConnection().createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        System.out.println(channel);
    }
}
