package com.lms.sqlfather.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.StatusConstant;
import com.lms.lmscommon.model.dto.userInterfaceInfo.PageUserInterfaceInfoRequest;
import com.lms.lmscommon.model.entity.UserInterfaceInfo;
import com.lms.lmscommon.model.vo.userInterfaceInfo.UserInterfaceInfoVO;
import com.lms.lmscommon.utils.MybatisUtils;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.annotation.AuthCheck;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.sqlfather.service.UserInterfaceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

/**
 * 用户接口控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@EnableResponseAdvice
@Api(value ="用户接口管理")
public class UserInterfaceController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    /**
     * 创建
     * @param interfaceId
     * @return
     */
    @PostMapping("/add/{interfaceId}")
    @ApiOperation("创建")
    public Boolean addUserInterface( @PathVariable("interfaceId") Long  interfaceId){
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return userInterfaceInfoService.saveUserInterface(loginId,interfaceId);
    }


    /**
     * 分页查询用户使用接口信息
     * @param pageInterfaceInfoDto
     * @return
     */
    @PostMapping("/page")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    @ApiOperation("分页查询用户使用接口信息")
    public Page<UserInterfaceInfoVO> pageUserInterfaceInfo(@RequestBody PageUserInterfaceInfoRequest pageInterfaceInfoDto){
        return userInterfaceInfoService.pageUseInterfaceInfo(pageInterfaceInfoDto);
    }

    /**
     * 用户获取自己的使用接口信息
     * @param request
     * @param pageUserInterfaceInfoDto
     * @return
     */
    @PostMapping("/page/user")
    @ApiOperation("用户获取自己的使用接口信息")
    public Page<UserInterfaceInfoVO> pageCurentUserInterfaceInfo(HttpServletRequest request,@RequestBody PageUserInterfaceInfoRequest pageUserInterfaceInfoDto){
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return userInterfaceInfoService.pageCurrentInterfaceInfo(loginId,pageUserInterfaceInfoDto);
    }


    /**
     * 删除用户使用接口信息
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    @ApiOperation("删除用户使用接口信息")
    public Boolean deleteUserInterface(@RequestParam("ids")List<Long> ids){
               return userInterfaceInfoService.update(new UpdateWrapper<UserInterfaceInfo>()
                       .in("id",ids).set("is_delete", StatusConstant.DELETED));
    }

    /**
     * 续期使用接口
     * @param id
     * @return
     */
    @PostMapping("/renewal/{id}")
    @ApiOperation("续期使用接口")
    public Boolean renewalInterface(@Positive(message = "id不合法") @PathVariable("id") Long id){
        BusinessException.throwIfNot(MybatisUtils.existCheck(
                userInterfaceInfoService, Map.of("id",id,"delete_flag", StatusConstant.NOT_DELETED)));
        return userInterfaceInfoService.update(new UpdateWrapper<UserInterfaceInfo>()
                .eq("id",id).set("left_num",100));
    }


    /**
     * 修改用户使用接口处状态
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/update/{id}/{status}")
    @AuthCheck(anyRole = UserConstant.ADMIN_ROLE)
    @ApiOperation("修改用户使用接口处状态")
    public Boolean  updateStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status){
       BusinessException.throwIfNot(!ObjectUtils.isEmpty(status)&&(status.equals(0)||status.equals(1)));
       return userInterfaceInfoService.update(new UpdateWrapper<UserInterfaceInfo>().set("status",status).eq("id",id));
    }



}
