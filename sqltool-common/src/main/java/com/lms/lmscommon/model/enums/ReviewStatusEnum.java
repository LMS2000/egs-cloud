package com.lms.lmscommon.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 词条审核状态枚举
 *
 */
public enum ReviewStatusEnum {

    /**
     * 待审核
     */
    REVIEWING("待审核", 0),
    /**
     * 通过
     */
    PASS("通过", 1),
    /**
     * 拒绝
     */
    REJECT("拒绝", 2);

    private final String text;

    private final int value;

    ReviewStatusEnum(String text, int value) {
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

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
