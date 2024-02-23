package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorEditRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorQueryRequest;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;


public interface GeneratorServiceFacade {


    /**
     * 删除生成器
     * @param deleteRequest
     * @param uid
     * @return
     */
    Boolean deleteGenerator(DeleteRequest deleteRequest,Long uid);
    /**
     * 获取帖子封装
     *
     * @param generator
     * @return
     */
    GeneratorVO getGeneratorVO(Generator generator, Long userId);

    /**
     * 分页获取帖子封装
     *
     * @param generatorPage
     * @return
     */
    Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage);


    /**
     *
     * @param generatorQueryRequest
     * @return
     */
    Page<GeneratorVO> listGeneratorVOByPage(GeneratorQueryRequest generatorQueryRequest);

    /**
     * 加入缓存后的分页接口
     * @param generatorQueryRequest
     * @return
     */
    Page<GeneratorVO> listGeneratorVOByPageFast(GeneratorQueryRequest generatorQueryRequest);

    /**
     * 分页获取当前用户创建的资源列表
     * @param generatorQueryRequest
     * @return
     */
    Page<GeneratorVO> listMyGeneratorVOByPage(GeneratorQueryRequest generatorQueryRequest);


    Boolean editGenerator( GeneratorEditRequest generatorEditRequest);
}
