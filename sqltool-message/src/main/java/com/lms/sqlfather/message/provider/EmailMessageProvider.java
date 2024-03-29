package com.lms.sqlfather.message.provider;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.json.JSONUtil;
import com.lms.lmscommon.model.dto.email.EmailMessage;
import com.lms.lmscommon.model.enums.MessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lms2000
 */
@Component
@Slf4j
public class EmailMessageProvider {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.amqp.exchanges.ex-delay-topic-email.name}")
    private String exchangeName;
    private static final String COMMON_ROUTING_KEY = "lms.router.message.email.{}";


    public void sendMessage(EmailMessage message) {
        Integer msgType = message.getMsgType();
        MessageTypeEnum enumByType = MessageTypeEnum.getEnumByValue(msgType);
        String routingKey = StrFormatter.format(COMMON_ROUTING_KEY, enumByType.getCode().toLowerCase());
        log.info("{} 消息发送成功: {}", routingKey, JSONUtil.toJsonStr(message));
        rabbitTemplate.convertAndSend(exchangeName,routingKey, message);
    }
}
