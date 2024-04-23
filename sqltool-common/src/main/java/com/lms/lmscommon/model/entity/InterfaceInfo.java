package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.common.DeleteFlagEntity;
import lombok.*;

/**
 * 接口信息
 * @TableName interface_info
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="interface_info")
@Data
@NoArgsConstructor
public class InterfaceInfo extends DeleteFlagEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     * [
     *   {"name": "username", "type": "string"}
     * ]
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private Integer method;

    /**
     * 创建人
     */
    private Long userId;

}