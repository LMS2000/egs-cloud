package com.lms.sqlfather.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.postfavour.PostFavourQueryRequest;
import com.lms.lmscommon.model.entity.PostFavour;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;

/**
 * 帖子收藏服务
 *
 * @author lms2000
 */
public interface PostFavourService extends IService<PostFavour> {

    /**
     * 帖子收藏
     *
     * @param generatorId
     * @param uid
     * @return
     */
    Integer doPostFavour(Long generatorId, Long uid);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param favourUserId
     * @return
     */
    Page<GeneratorVO> listFavourPostByPage(PostFavourQueryRequest postFavourQueryRequest,
                                           Long favourUserId);


    /**
     * 帖子收藏（内部服务）
     *
     * @param userId
     * @param generatorId
     * @return
     */
    Integer doPostFavourInner(Long userId, Long  generatorId);
}
