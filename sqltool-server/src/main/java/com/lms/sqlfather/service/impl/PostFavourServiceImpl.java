package com.lms.sqlfather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.model.dto.postfavour.PostFavourQueryRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.entity.PostFavour;
import com.lms.lmscommon.model.vo.post.PostVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.mapper.PostFavourMapper;
import com.lms.sqlfather.service.PostFavourService;
import com.lms.sqlfather.service.PostService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.lms.lmscommon.model.factory.PostFactory.POST_CONVERTER;

/**
 * 帖子收藏服务实现
 *
 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
        implements PostFavourService {

    @Resource
    private PostService postService;

    /**
     * 帖子收藏
     *
     * @param postId
     * @param uid
     * @return
     */
    @Override
    public Integer doPostFavour(Long postId, Long uid) {
        // 判断是否存在
        Post post = postService.getById(postId);
        BusinessException.throwIf(post==null);
        // 是否已帖子收藏
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        PostFavourService PostFavourService = (PostFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(uid).intern()) {
            return PostFavourService.doPostFavourInner(uid, postId);
        }
    }

    @Override
    public Page<PostVO> listFavourPostByPage(PostFavourQueryRequest postFavourQueryRequest, Long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        Long current = postFavourQueryRequest.getCurrent();
        Long size = postFavourQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size>20);
        Page<Post> postPage = baseMapper.listFavourPostByPage(new Page<>(current, size),
                postService.getQueryWrapper(postFavourQueryRequest.getPostQueryRequest()),favourUserId);
        Page<PostVO> postVOPage=new Page<>(current,size);
        postVOPage.setRecords(POST_CONVERTER.toListPostVO(postPage.getRecords()));
        postVOPage.setTotal(postPage.getTotal());
        return postVOPage;
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer doPostFavourInner(Long userId, Long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setUserId(userId);
        postFavour.setPostId(postId);
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(postFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            result = this.remove(postFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = postService.update()
                        .eq("id", postId)
                        .gt("favour_num", 0)
                        .setSql("favour_num = favour_num - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(HttpCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(postFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = postService.update()
                        .eq("id", postId)
                        .setSql("favour_num = favour_num + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(HttpCode.SYSTEM_ERROR);
            }
        }
    }

}




