package com.lms.sqlfather.core.builder;

import cn.hutool.core.util.StrUtil;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.sqlfather.core.model.dto.JavaEntityGenerateDTO;
import com.lms.sqlfather.core.model.dto.JavaEntityGenerateDTO.FieldDTO;
import com.lms.sqlfather.core.model.dto.JavaObjectGenerateDTO;
import com.lms.sqlfather.core.model.enums.FieldTypeEnum;
import com.lms.sqlfather.core.model.enums.MockTypeEnum;
import com.lms.sqlfather.core.schema.TableSchema;
import com.lms.sqlfather.core.schema.TableSchema.Field;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java 代码生成器
 *
 */
@Component
@Slf4j
public class JavaCodeBuilder {

    private static Configuration configuration;

    @Resource
    public void setConfiguration(Configuration configuration) {
      JavaCodeBuilder.configuration = configuration;
    }

    /**
     * 构造 Java 实体代码
     *
     * @param tableSchema 表概要
     * @return 生成的 java 代码
     */
    @SneakyThrows
    public static String buildJavaEntityCode(TableSchema tableSchema) {
        // 传递参数
        List<JavaEntityGenerateDTO> javaEntityGenerateDTOs = new ArrayList<>();

        tableSchema.getTableList().forEach(table -> {
            JavaEntityGenerateDTO javaEntityGenerateDTO=new JavaEntityGenerateDTO();
            String tableName = table.getTableName();
            String tableComment = table.getTableComment();
            String upperCamelTableName = StringUtils.capitalize(StrUtil.toCamelCase(tableName));
            // 类名为大写的表名
            javaEntityGenerateDTO.setClassName(upperCamelTableName);
            // 类注释为表注释 > 表名
            javaEntityGenerateDTO.setClassComment(Optional.ofNullable(tableComment).orElse(upperCamelTableName));
            // 依次填充每一列
            List<FieldDTO> fieldDTOList = new ArrayList<>();
            for (Field field : table.getFieldList()) {
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setComment(field.getComment());
                FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT);
                fieldDTO.setJavaType(fieldTypeEnum.getJavaType());
                fieldDTO.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                fieldDTOList.add(fieldDTO);
            }
            javaEntityGenerateDTO.setFieldList(fieldDTOList);
            javaEntityGenerateDTOs.add(javaEntityGenerateDTO);
        });
        StringBuilder stringBuilder=new StringBuilder();
        javaEntityGenerateDTOs.forEach(javaEntityGenerateDTO -> {
            StringWriter stringWriter = new StringWriter();
            Template temp = null;
            try {
                temp = configuration.getTemplate("java_entity.ftl");
                temp.process(javaEntityGenerateDTO, stringWriter);
                stringBuilder.append(stringWriter.toString()).append("\n");
            } catch (TemplateException e) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"java代码填入模板失败");
            }catch (Exception e){
                throw new BusinessException(HttpCode.OPERATION_ERROR,"异常错误");
            }

        });

        return stringBuilder.toString();
    }

    /**
     * 构造 Java 对象代码
     *
     * @param tableSchema 表概要
     * @param insertDataMap    数据列表
     * @return 生成的 java 代码
     */
    @SneakyThrows
    public static String buildJavaObjectCode(TableSchema tableSchema, Map<String,List<Map<String,Object>>> insertDataMap) {

         BusinessException.throwIf(MapUtils.isEmpty(insertDataMap),
                 HttpCode.PARAMS_ERROR,"缺少示例数据");


        // 传递参数

        StringBuilder resultBuilder=new StringBuilder();
        tableSchema.getTableList().forEach(table -> {
            List<Map<String, Object>> dataList = insertDataMap.get(table.getTableName());
            JavaObjectGenerateDTO javaObjectGenerateDTO=new JavaObjectGenerateDTO();
            String tableName = table.getTableName();
            String camelTableName = StrUtil.toCamelCase(tableName);
            // 类名为大写的表名
            javaObjectGenerateDTO.setClassName(StringUtils.capitalize(camelTableName));
            // 变量名为表名
            javaObjectGenerateDTO.setObjectName(camelTableName);
            // 依次填充每一列
            Map<String, Object> fillData = dataList.get(0);
            List<JavaObjectGenerateDTO.FieldDTO> fieldDTOList = new ArrayList<>();
            List<Field> fieldList = table.getFieldList();
            // 过滤掉不模拟的字段
            fieldList = fieldList.stream()
                    .filter(field -> {
                        MockTypeEnum mockTypeEnum = Optional.ofNullable(MockTypeEnum.getEnumByValue(field.getMockType())).orElse(MockTypeEnum.NONE);
                        return !MockTypeEnum.NONE.equals(mockTypeEnum);
                    })
                    .collect(Collectors.toList());
            for (Field field : fieldList) {
                JavaObjectGenerateDTO.FieldDTO fieldDTO = new JavaObjectGenerateDTO.FieldDTO();
                // 驼峰字段名
                String fieldName = field.getFieldName();
                fieldDTO.setSetMethod(StrUtil.toCamelCase("set_" + fieldName));
                fieldDTO.setValue(getValueStr(field, fillData.get(fieldName)));
                fieldDTOList.add(fieldDTO);
            }
            javaObjectGenerateDTO.setFieldList(fieldDTOList);
            StringWriter stringWriter = new StringWriter();
            Template temp = null;
            try {
                temp = configuration.getTemplate("java_object.ftl");
                temp.process(javaObjectGenerateDTO, stringWriter);
                resultBuilder.append(stringWriter.toString()).append("\n");
            } catch (TemplateException e) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"java对象类填入模板失败");
            }catch (Exception e){
                throw new BusinessException(HttpCode.OPERATION_ERROR,"异常错误");
            }
        });


        return resultBuilder.toString();
    }

    /**
     * 根据列的属性获取值字符串
     *
     * @param field
     * @param value
     * @return
     */
    public static String getValueStr(Field field, Object value) {
        if (field == null || value == null) {
            return "''";
        }
        FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType()))
                .orElse(FieldTypeEnum.TEXT);
        switch (fieldTypeEnum) {
            case DATE:
            case TIME:
            case DATETIME:
            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BINARY:
            case VARBINARY:
                return String.format("\"%s\"", value);
            default:
                return String.valueOf(value);
        }
    }
}
