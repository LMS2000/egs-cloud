package com.lms.lmscommon.model.dto.email;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lms2000
 */
@Data
public class EmailMessage implements Serializable {


    private String email;
    private Integer msgType;
    /**
     * 注册发送邮件标题
     */
    private String registerEmailTitle = "邮箱验证码";

    /**
     * 注册发送邮件内容
     */
    private String registerEmailContent = "你好，您的邮箱验证码是：%s，15分钟有效";

    /**
     * 用户初始化空间大小 5M
     */
    private Integer userInitUseSpace = 5;
}
