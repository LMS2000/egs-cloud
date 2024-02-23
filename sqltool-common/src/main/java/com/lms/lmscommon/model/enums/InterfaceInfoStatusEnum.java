package com.lms.lmscommon.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 *
 * @author LMS2000
 */
public enum InterfaceInfoStatusEnum {

    /**
     * 接口关闭状态
     */
    OFFLINE("关闭", 0),
    /**
     * 接口上线状态
     */
    ONLINE("上线", 1);

    private final String text;

    private final Integer value;

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
