package com.lms.sqlfather.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.model.dto.post.PostAddRequest;
import com.lms.lmscommon.model.dto.post.PostEditRequest;
import com.lms.lmscommon.model.dto.post.PostQueryRequest;
import com.lms.lmscommon.model.dto.post.PostUpdateRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.vo.post.PostVO;
import com.lms.sqlfather.annotation.AuthCheck;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.PostService;
import com.lms.sqlfather.service.PostServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * 帖子控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/post")
@Slf4j
@AllArgsConstructor
@Api(value = "帖子管理")
public class PostController {


    private final PostService postService;


    private final PostServiceFacade postServiceFacade;



    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @return
     */
    @PostMapping("/add")
    @ApiOperationSupport(order =1)
    @ApiOperation(value = " 创建")
    public Long addPost(@RequestBody @Valid PostAddRequest postAddRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postService.addPost(postAddRequest,loginId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @ApiOperationSupport(order =2)
    @ApiOperation(value = " 删除")
    public Boolean deletePost(@RequestBody DeleteRequest deleteRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postServiceFacade.deletePost(deleteRequest,loginId);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @ApiOperationSupport(order =3)
    @ApiOperation(value = " 修改")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Boolean updatePost(@RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.updatePost(postUpdateRequest);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @ApiOperationSupport(order =4)
    @ApiOperation(value = " 根据 id 获取")
    public PostVO getPostVOById(@Positive(message = "id不合法") Long id) {
        BusinessException.throwIf(id <= 0);
        Post post = postService.getById(id);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        BusinessException.throwIf(post==null,HttpCode.NOT_FOUND_ERROR);
        return postService.getPostVO(post, loginId);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @ApiOperationSupport(order =5)
    @ApiOperation(value = "分页获取列表（封装类）")
    public Page<PostVO> listPostVOByPage(@RequestBody PostQueryRequest postQueryRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postService.listPostVOByPage(postQueryRequest, loginId);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/my/list/page/vo")
    @ApiOperationSupport(order =6)
    @ApiOperation(value = "分页获取当前用户创建的资源列表")
    public Page<PostVO> listMyPostVOByPage(@RequestBody PostQueryRequest postQueryRequest) {

        BusinessException.throwIf(postQueryRequest==null);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postService.pagePost(postQueryRequest,loginId);
    }

    // endregion

//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param postQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public Page<PostVO> searchPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
//            HttpServletRequest request) {
//        long size = postQueryRequest.getPageSize();
//        // 限制爬虫
//        BusinessException.throwIf(size>20);
//        return ResultUtils.success(postService.getPostVOPage(postPage, request));
//    }

    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @return
     */
    @PostMapping("/edit")
    @ApiOperationSupport(order =7)
    @ApiOperation(value = "编辑（用户）")
    public Boolean editPost(@RequestBody @Valid PostEditRequest postEditRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        return postServiceFacade.editPost(postEditRequest,loginId);
    }

}
