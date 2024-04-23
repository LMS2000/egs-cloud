package com.lms.sqlfather.config;

import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.AmqpHeaders;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class MQErrorHandler implements RabbitListenerErrorHandler {
    private final RabbitTemplate rabbitTemplate;

    public MQErrorHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
        // 错误处理逻辑
//            // 可以在这里记录日志或者进行其他处理
//            // 然后手动确认消息，防止消息被重新入队
            long deliveryTag = (long) message1.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            acknowledgeMessage(deliveryTag);
            throw new BusinessException(HttpCode.OPERATION_ERROR,e.getMessage());
    }

    private void acknowledgeMessage(long deliveryTag) {
        try(Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(true);){
            log.info("处理错误deliveryTag:"+deliveryTag+"  消息确认");
            channel.basicAck(deliveryTag, false);
        }catch (IOException | TimeoutException e){
            throw new BusinessException(HttpCode.OPERATION_ERROR,"rabbitmq ACK消息失败 deliveryTag:"+deliveryTag);
        }
    }

}
