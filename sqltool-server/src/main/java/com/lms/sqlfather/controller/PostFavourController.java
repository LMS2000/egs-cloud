package com.lms.sqlfather.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.lmscommon.model.dto.postfavour.PostFavourAddRequest;
import com.lms.lmscommon.model.dto.postfavour.PostFavourQueryRequest;
import com.lms.lmscommon.common.BusinessException;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.sqlfather.service.PostFavourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子收藏控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/post/favour")
@Slf4j
@AllArgsConstructor
@Api(value = "帖子收藏")
public class PostFavourController {


    private final PostFavourService postFavourService;





    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @return resultNum 收藏变化数
     */
    @PostMapping()
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "收藏/取消 收藏")
    public Integer doPostFavour(@Validated  @RequestBody PostFavourAddRequest postFavourAddRequest) {
        // 登录才能操作
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        Long postId = postFavourAddRequest.getGeneratorId();
        return postFavourService.doPostFavour(postId, loginId);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param postFavourQueryRequest
     */
    @PostMapping("/my/list/page")
    @ApiOperationSupport(order =2)
    @ApiOperation(value = "获取我收藏的帖子列表")
    public Page<GeneratorVO> listMyFavourPostByPage(@RequestBody PostFavourQueryRequest postFavourQueryRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postFavourService.listFavourPostByPage(postFavourQueryRequest,loginId);
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "获取用户收藏的帖子列表")
    public Page<GeneratorVO> listFavourPostByPage(@RequestBody PostFavourQueryRequest postFavourQueryRequest,
            HttpServletRequest request) {
        BusinessException.throwIf(postFavourQueryRequest == null);
        Long current = postFavourQueryRequest.getCurrent();
        Long size = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        // 限制爬虫
        BusinessException.throwIf(size > 20 || userId == null);
        return postFavourService.listFavourPostByPage(postFavourQueryRequest, userId);
    }
}
