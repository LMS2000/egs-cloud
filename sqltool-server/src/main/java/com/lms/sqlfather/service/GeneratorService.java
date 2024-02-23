package com.lms.sqlfather.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.generator.GeneratorAddRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorQueryRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorUpdateRequest;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface GeneratorService extends IService<Generator> {

    /**
     * 校验
     *
     * @param generator
     * @param add
     */
    void validGenerator(Generator generator, boolean add);

    /**
     * 获取查询条件
     *
     * @param generatorQueryRequest
     * @return
     */
    QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest);




    /**
     * 创建
     * @param generatorAddRequest
     * @return
     */
    Long addGenerator(GeneratorAddRequest generatorAddRequest,Long uid);

    /**
     *
     * @param generatorUpdateRequest
     * @return
     */
    Boolean updateGenerator(GeneratorUpdateRequest generatorUpdateRequest);
}
