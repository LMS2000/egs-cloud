package com.lms.sqlfather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.post.PostAddRequest;
import com.lms.lmscommon.model.dto.post.PostQueryRequest;
import com.lms.lmscommon.model.dto.post.PostUpdateRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.entity.PostFavour;
import com.lms.lmscommon.model.entity.PostThumb;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.post.PostVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.utils.SqlUtils;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.mapper.PostFavourMapper;
import com.lms.sqlfather.mapper.PostMapper;
import com.lms.sqlfather.mapper.PostThumbMapper;
import com.lms.sqlfather.service.PostService;
import com.lms.sqlfather.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lms.lmscommon.model.factory.PostFactory.POST_CONVERTER;
import static com.lms.lmscommon.model.factory.UserFactory.USER_CONVERTER;


/**
 * 帖子服务实现
 *
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final static Gson GSON = new Gson();

    @Resource(name = "userServiceImpl")
    private UserService userService;


    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;



    @Override
    public void validPost(Post post, boolean add) {
        BusinessException.throwIf(post==null);
        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        // 创建时，参数不能为空
        if (add) {
            BusinessException.throwIf(StringUtils.isAnyBlank(title, content, tags), HttpCode.PARAMS_ERROR);
        }
        // 有参数则校验
        BusinessException.throwIf(StringUtils.isNotBlank(title) && title.length() > 80,"标题过长");

        BusinessException.throwIf(StringUtils.isNotBlank(content) && content.length() > 8192,"内容过长");
    }

    /**
     * 获取查询包装类
     *
     * @param postQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (postQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = postQueryRequest.getSearchText();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        Long id = postQueryRequest.getId();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTags();
        Long userId = postQueryRequest.getUserId();
        Long notId = postQueryRequest.getNotId();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

//    @Override
//    public Page<Post> searchFromEs(PostQueryRequest postQueryRequest) {
//        Long id = postQueryRequest.getId();
//        Long notId = postQueryRequest.getNotId();
//        String searchText = postQueryRequest.getSearchText();
//        String title = postQueryRequest.getTitle();
//        String content = postQueryRequest.getContent();
//        List<String> tagList = postQueryRequest.getTags();
//        List<String> orTagList = postQueryRequest.getOrTags();
//        Long userId = postQueryRequest.getUserId();
//        // es 起始页为 0
//        long current = postQueryRequest.getCurrent() - 1;
//        long pageSize = postQueryRequest.getPageSize();
//        String sortField = postQueryRequest.getSortField();
//        String sortOrder = postQueryRequest.getSortOrder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        // 过滤
//        boolQueryBuilder.filter(QueryBuilders.termQuery("deleteFlag", 0));
//        if (id != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
//        }
//        if (notId != null) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
//        }
//        if (userId != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
//        }
//        // 必须包含所有标签
//        if (CollectionUtils.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
//            }
//        }
//        // 包含任何一个标签即可
//        if (CollectionUtils.isNotEmpty(orTagList)) {
//            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
//            for (String tag : orTagList) {
//                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
//            }
//            orTagBoolQueryBuilder.minimumShouldMatch(1);
//            boolQueryBuilder.filter(orTagBoolQueryBuilder);
//        }
//        // 按关键词检索
//        if (StringUtils.isNotBlank(searchText)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按标题检索
//        if (StringUtils.isNotBlank(title)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按内容检索
//        if (StringUtils.isNotBlank(content)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 排序
//        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
//        if (StringUtils.isNotBlank(sortField)) {
//            sortBuilder = SortBuilders.fieldSort(sortField);
//            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
//        }
//        // 分页
//        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
//        // 构造查询
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
//                .withPageable(pageRequest).withSorts(sortBuilder).build();
//        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
//        Page<Post> page = new Page<>();
//        page.setTotal(searchHits.getTotalHits());
//        List<Post> resourceList = new ArrayList<>();
//        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
//        if (searchHits.hasSearchHits()) {
//            List<SearchHit<PostEsDTO>> searchHitList = searchHits.getSearchHits();
//            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
//                    .collect(Collectors.toList());
//            // 从数据库中取出更完整的数据
//            List<Post> postList = baseMapper.selectBatchIds(postIdList);
//            if (postList != null) {
//                Map<Long, List<Post>> idPostMap = postList.stream().collect(Collectors.groupingBy(Post::getId));
//                postIdList.forEach(postId -> {
//                    if (idPostMap.containsKey(postId)) {
//                        resourceList.add(idPostMap.get(postId).get(0));
//                    } else {
//                        // 从 es 清空 db 已物理删除的数据
//                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDTO.class);
//                        log.info("delete post {}", delete);
//                    }
//                });
//            }
//        }
//        page.setRecords(resourceList);
//        return page;
//    }

    @Override
    public PostVO getPostVO(Post post, Long loginId) {
        PostVO postVO = POST_CONVERTER.toPostVO(post);
        long postId = post.getId();
        // 1. 关联查询用户信息
        Long userId = post.getUserId();
        UserVO user = null;
        if (userId != null && userId > 0) {
            user = USER_CONVERTER.toUserVo( userService.getById(userId));
        }
        postVO.setUser(user);
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getById(loginId);

        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
            postThumbQueryWrapper.in("post_id", postId);
            postThumbQueryWrapper.eq("user_id", loginUser.getId());
            PostThumb postThumb = postThumbMapper.selectOne(postThumbQueryWrapper);
            postVO.setHasThumb(postThumb != null);
            // 获取收藏
            QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
            postFavourQueryWrapper.in("post_id", postId);
            postFavourQueryWrapper.eq("user_id", loginUser.getId());
            PostFavour postFavour = postFavourMapper.selectOne(postFavourQueryWrapper);
            postVO.setHasFavour(postFavour != null);
        }
        return postVO;
    }

    @Override
    public Page<PostVO> getPostVOPage(Page<Post> postPage, Long uid) {
        List<Post> postList = postPage.getRecords();
        Page<PostVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (CollectionUtils.isEmpty(postList)) {
            return postVOPage;
        }
        // 1. 关联查询用户信息
        List<Long> userIdSet = postList.stream().map(Post::getUserId).collect(Collectors.toList());
        Map<Long, List<UserVO>> userIdUserListMap = userService.list(new QueryWrapper<User>().in("id",userIdSet)).stream()
                .map(USER_CONVERTER::toUserVo)
                .collect(Collectors.groupingBy(UserVO::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> postIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> postIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getById(uid);
        if (loginUser != null) {
            Set<Long> postIdSet = postList.stream().map(Post::getId).collect(Collectors.toSet());
            // 获取点赞
            QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
            postThumbQueryWrapper.in("post_id", postIdSet);
            postThumbQueryWrapper.eq("user_id", loginUser.getId());
            List<PostThumb> postPostThumbList = postThumbMapper.selectList(postThumbQueryWrapper);
            postPostThumbList.forEach(postPostThumb -> postIdHasThumbMap.put(postPostThumb.getPostId(), true));
            // 获取收藏
            QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
            postFavourQueryWrapper.in("post_id", postIdSet);
            postFavourQueryWrapper.eq("user_id", loginUser.getId());
            List<PostFavour> postFavourList = postFavourMapper.selectList(postFavourQueryWrapper);
            postFavourList.forEach(postFavour -> postIdHasFavourMap.put(postFavour.getPostId(), true));
        }
        // 填充信息
        List<PostVO> postVOList = postList.stream().map(post -> {
            PostVO postVO =POST_CONVERTER.toPostVO(post);
            Long userId = post.getUserId();
            UserVO user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }

            postVO.setUser(user);
            postVO.setHasThumb(postIdHasThumbMap.getOrDefault(post.getId(), false));
            postVO.setHasFavour(postIdHasFavourMap.getOrDefault(post.getId(), false));
            return postVO;
        }).collect(Collectors.toList());
        postVOPage.setRecords(postVOList);
        return new Page<>();
    }

    @Override
    public Page<PostVO> listPostVOByPage(PostQueryRequest postQueryRequest, Long uid) {
        long current = postQueryRequest.getCurrent();
        long pageSize = postQueryRequest.getPageSize();
        Page<Post> postPage = this.page(new Page<>(current, pageSize),
                this.getQueryWrapper(postQueryRequest));
        return this.getPostVOPage(postPage, uid);
    }

    @Override
    public Long addPost(PostAddRequest postAddRequest,Long uid) {
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        validPost(post, true);
        post.setUserId(uid);
        post.setFavourNum(0);
        post.setThumbNum(0);
        save(post);
        return post.getId();
    }

    @Override
    public Boolean updatePost(PostUpdateRequest postUpdateRequest) {
        Post post = new Post();
        BeanUtils.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        // 参数校验
        validPost(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        Post oldPost = getById(id);
        BusinessException.throwIf(oldPost==null,HttpCode.NOT_FOUND_ERROR);
        return updateById(post);
    }

    @Override
    public Page<PostVO> pagePost(PostQueryRequest postQueryRequest, Long uid) {

        postQueryRequest.setUserId(uid);
        Long current = postQueryRequest.getCurrent();
        Long size = postQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size>20);
        Page<Post> postPage = page(new Page<>(current, size),
                getQueryWrapper(postQueryRequest));
        return getPostVOPage(postPage, uid);
    }

}




