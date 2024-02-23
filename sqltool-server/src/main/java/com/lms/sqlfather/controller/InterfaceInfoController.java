package com.lms.sqlfather.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.lmscommon.model.dto.interfaceInfo.AddInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.PageInterfaceInfoRequest;
import com.lms.lmscommon.model.dto.interfaceInfo.UpdateInterfaceInfoRequest;
import com.lms.lmscommon.model.enums.InterfaceInfoStatusEnum;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.annotation.AuthCheck;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.sqlfather.service.InterfaceInfoService;
import com.lms.sqlfather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Positive;
import java.util.List;
/**
 * 接口控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/interface/info")
@EnableResponseAdvice
@Api(description = "接口管理")
public class InterfaceInfoController {


    @Resource
    private InterfaceInfoService interfaceInfoService;


    /**
     * 添加接口信息
     *
     * @param addInterfaceInfoDto
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("添加接口信息")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    public Boolean saveInterfaceInfo(@Validated @RequestBody AddInterfaceInfoRequest addInterfaceInfoDto) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return interfaceInfoService.saveInterface(addInterfaceInfoDto, loginId);
    }

    /**
     *
     * @param updateInterfaceInfoDto
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("修改接口信息")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    public Boolean updateInterfaceInfo(@Validated @RequestBody UpdateInterfaceInfoRequest updateInterfaceInfoDto) {
        return interfaceInfoService.updateInterface(updateInterfaceInfoDto);
    }


    /**
     *
     * @param pageInterfaceInfoDto
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("分页查询接口信息列表")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    public Page<InterfaceInfoVO> page(@RequestBody PageInterfaceInfoRequest pageInterfaceInfoDto) {
        return interfaceInfoService.pageInterfaceVo(pageInterfaceInfoDto);
    }

    /**
     *
     * @param pageInterfaceInfoDto
     * @return
     */
    @PostMapping("/page/user")
    @ApiOperation("分页查询上线的接口")
    public Page<InterfaceInfoVO> pageUser(@RequestBody PageInterfaceInfoRequest pageInterfaceInfoDto){
        pageInterfaceInfoDto.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return interfaceInfoService.pageInterfaceVo(pageInterfaceInfoDto);
    }

    /**
     *
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("批量删除接口信息")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    public Boolean deleteBatchInterfaceInfo(@RequestParam("ids") List<Long> ids) {
        return interfaceInfoService.deleteInterface(ids);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根绝id获取接口信息")
    public InterfaceInfoVO getInterfaceById(@Positive(message = "id不合法") @PathVariable("id") Long id) {
        return interfaceInfoService.getInterfaceById(id);
    }


    /**
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/changeStatus/{id}/{status}")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    @ApiOperation("发布或者下线接口")
    public Boolean changeInterfaceStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        if(InterfaceInfoStatusEnum.ONLINE.getValue().equals(status)){
            return interfaceInfoService.onlineInterface(id);
        }else if(InterfaceInfoStatusEnum.OFFLINE.getValue().equals(status)){
            return interfaceInfoService.offlineInterface(id);
        }
        return false;
    }



//    @PostMapping("/invoke")
//    @ApiOperation("调用测试接口")
//    public String invokeInterface(@RequestBody InvokeInterfaceRequest invokeInterfaceDto, HttpServletRequest request){
//        User loginUser = userService.getLoginUser(request);
//        return interfaceInfoService.invokeinterface(invokeInterfaceDto,loginUser.getAccessKey(),loginUser.getSecretKey());
//    }
}
