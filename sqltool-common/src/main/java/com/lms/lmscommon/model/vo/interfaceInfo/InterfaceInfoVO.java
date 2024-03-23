package com.lms.lmscommon.model.vo.interfaceInfo;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "InterfaceInfoVO对象", description = "接口视图")
public class InterfaceInfoVO  extends BaseVO {



    /**
     * 接口名
     */
    @ApiModelProperty("接口名")
    private String name;

    /**
     *  接口描述
     */
    @ApiModelProperty("接口描述")
    private String description;

    /**
     *  接口地址
     */
    @ApiModelProperty("接口地址")
    private String url;
    /**
     *  接口请求参数
     */
    @ApiModelProperty("接口请求参数")
    private String requestParams;

    /**
     * 接口请求头
     */
    @ApiModelProperty("接口请求头")
    private String requestHeader;

    /**
     *  接口响应头
     */
    @ApiModelProperty("接口响应头")
    private String responseHeader;
    /**
     *  接口状态， 0为下线  1为上线
     */
    @ApiModelProperty(" 接口状态， 0为下线  1为上线")
    private Integer status;

    /**
     *  接口请求方法  GET POST PUT DELETE
     */
    @ApiModelProperty("接口请求方法  GET POST PUT DELETE")
    private String method;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long userId;


}
