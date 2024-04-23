package com.lms.sqlfather.message.consumer;

import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.CommonConstant;
import com.lms.lmscommon.constant.EmailConstant;
import com.lms.lmscommon.model.dto.email.EmailMessage;
import com.lms.lmscommon.utils.StringTools;
import com.lms.redis.RedisCache;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lms2000
 */
@Component
@Slf4j
public class EmailMessageConsumer {
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RedisCache redisCache;

    /**
     * 发送人
     */
    @Value("${spring.mail.username}")
    private String sendUserName;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "${app.amqp.queues.mq-lms-router-message-email.name}"), // 同理这里又声明创建了一个direct.queue2为名的队列
            exchange = @Exchange(name = "${app.amqp.exchanges.ex-delay-topic-email.name}", type = "${app.amqp.exchanges.ex-delay-topic-email.type}",delayed = "${app.amqp.exchanges.ex-delay-topic-email.delayed}"), // 绑定声明的交换机
            key = {"lms.router.message.email.*"}  // 并且为这个topic.queue为名的队列设置的BindingKey为
      ))
    public void processToSendMessage(EmailMessage message,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,Channel channel){
        String email = message.getEmail();
        Integer type = message.getMsgType();
        //随机的邮箱验证码
        String code = StringTools.getRandomNumber(CommonConstant.LENGTH_5);
        sendEmailCode(code,message);
        //设置15分钟的失效时间
        redisCache.setCacheObject(EmailConstant.EMAIIL_HEADER + type + "_" + email, code, 15, TimeUnit.MINUTES);
        //手动ack
        acknowledgeMessage(channel,deliveryTag);
    }
    private void sendEmailCode(String code, EmailMessage msg) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //邮件发件人
            helper.setFrom(sendUserName);
            //邮件收件人 1或多个
            helper.setTo(msg.getEmail());
            //邮件主题
            helper.setSubject(msg.getRegisterEmailTitle());
            //邮件内容
            helper.setText(String.format(msg.getRegisterEmailContent(), code));
            //邮件发送时间
            helper.setSentDate(new Date());
            // 报错重试
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            throw new BusinessException(HttpCode.OPERATION_ERROR, "邮件发送失败");
        }
    }  private void acknowledgeMessage(Channel channel, long deliveryTag) {
        try{
            log.info("deliveryTag:"+deliveryTag+"  消息确认");
            channel.basicAck(deliveryTag, false);
        }catch (IOException e){
            throw new BusinessException(HttpCode.OPERATION_ERROR,"rabbitmq ACK消息失败 deliveryTag:"+deliveryTag);
        }
    }
}
