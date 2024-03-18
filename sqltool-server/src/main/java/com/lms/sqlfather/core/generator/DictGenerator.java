package com.lms.sqlfather.core.generator;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.utils.SpringContextUtils;
import com.lms.sqlfather.core.schema.TableSchema.Field;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.DictService;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 词库数据生成器
 */
public class DictGenerator implements DataGenerator {

    private DictService dictService;
    private static final Gson GSON =new Gson();
    public DictGenerator(){
        this.dictService = SpringUtil.getBean(DictService.class);
    }


    @Override
    public List<String> doGenerate(Field field, int rowNum) {
        String mockParams = field.getMockParams();
        long dictId = Long.parseLong(mockParams);
        List<String> result=new ArrayList<>();
        Dict byId = dictService.getById(dictId);
        BusinessException.throwIf(byId==null, HttpCode.NOT_FOUND_ERROR,
                "找不到词库");
        //TypeToken :这种写法能将返回一个json数据集合
        List<String> typeList = GSON.fromJson(byId.getContent(),
                new TypeToken<List<String>>() {
        }.getType());
        for (int i = 0; i < rowNum; i++) {
             result.add(typeList.get(RandomUtils.nextInt(0,typeList.size())));
        }
        return result;
    }
}
