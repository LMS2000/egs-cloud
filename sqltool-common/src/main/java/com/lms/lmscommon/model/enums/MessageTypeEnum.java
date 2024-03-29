package com.lms.lmscommon.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
@Getter
public enum MessageTypeEnum implements Serializable {
    EMAIL_REGISTER_TYPE("EMAIL_REGISTER",0),
    EMAIL_FINDBACK_TYPE("EMAIL_FINDBACK",1);


    private String code;

    private Integer type;

    MessageTypeEnum(String code, Integer type) {
        this.code = code;
        this.type = type;
    }
    public static MessageTypeEnum getEnumByValue(Integer type) {
        if (ObjectUtils.isEmpty(type)) {
            return null;
        }
        for (MessageTypeEnum anEnum : MessageTypeEnum.values()) {
            if (anEnum.type.equals(type)) {
                return anEnum;
            }
        }
        return null;
    }
}
