package com.lms.sqlfather.core.model.vo;

import com.lms.sqlfather.core.schema.TableSchema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 生成的返回值
 *
 */
@Data
public class GenerateVO implements Serializable {

    private TableSchema tableSchema;

    private String createSql;

    private Map<String,List<Map<String, Object>>> dataList;

    private String insertSql;

    private String dataJson;

    private String javaEntityCode;

    private String javaObjectCode;

    private String typescriptTypeCode;

    private static final long serialVersionUID = 7122637163626243606L;
}
