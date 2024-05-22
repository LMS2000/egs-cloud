package com.lms.lmscommon.model.enums;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ReportTypeEnum implements Serializable {

    /**
     * 词库
     */
    DICT("词库", 0),
    /**
     * 表
     */
    TABLE("表", 1),
    /**
     * 字段
     */
    FIELD("字段", 2),
    /**
     * 生成器
     */
    GENERATOR("生成器", 3);

    @Getter
    private final String text;

    @Getter
    private final Integer value;

    ReportTypeEnum(String text, Integer value) {
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

}
