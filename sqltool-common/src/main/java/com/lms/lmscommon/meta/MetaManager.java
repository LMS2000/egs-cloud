package com.lms.lmscommon.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {

    private static volatile Meta meta;

    public MetaManager() {
    }

    public static Meta getMetaObject(){
        if(meta==null){
            synchronized (MetaManager.class){
                if(meta==null){
                  meta=initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
        String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");

        Meta newMeta= JSONUtil.toBean(metaJson,Meta.class);
        //校验参数
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }
}
