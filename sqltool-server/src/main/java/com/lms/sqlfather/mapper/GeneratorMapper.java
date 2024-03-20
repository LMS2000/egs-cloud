package com.lms.sqlfather.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;;
import com.lms.lmscommon.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lms2000
 * @description 针对表【generator(代码生成器)】的数据库操作Mapper
 * @createDate 2023-12-27 20:49:39
 */
public interface GeneratorMapper extends BaseMapper<Generator> {
    @Select("SELECT id, distPath FROM generator WHERE is_delete = 1")
    List<Generator> listDeletedGenerator();
}




