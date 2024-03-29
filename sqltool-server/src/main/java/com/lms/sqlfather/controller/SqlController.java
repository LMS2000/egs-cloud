package com.lms.sqlfather.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.generate.LmsCodeGenerator;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.sql.AiCreateSqlRequest;
import com.lms.lmscommon.model.dto.sql.GenerateByAutoRequest;
import com.lms.lmscommon.model.dto.sql.GenerateBySqlRequest;
import com.lms.lmscommon.model.entity.User;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.core.GeneratorFacade;
import com.lms.sqlfather.core.model.vo.GenerateVO;
import com.lms.sqlfather.core.schema.TableSchema;
import com.lms.sqlfather.core.schema.TableSchemaBuilder;
import com.lms.sqlfather.service.AiService;
import com.lms.sqlfather.service.UserService;
import com.yupi.yucongming.dev.utils.SignUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

/**
 * sql生成控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/sql")
@EnableResponseAdvice
@Api(value = "sql管理")
public class SqlController {


    @Resource
    private AiService aiService;


    @Resource(name = "userServiceImpl")
    private UserService userService;

    /**
     * ai生成sql
     * @param aiCreateSqlRequest
     * @return
     */
    @PostMapping("/generate/ai")
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "ai生成sql")
    public String getAiCreateSql(@RequestBody AiCreateSqlRequest aiCreateSqlRequest){
        return aiService.getAiCreateSql(aiCreateSqlRequest.getMessage());
    }


    /**
     * 对sdk使用的接口
     * @param sqlRequest
     * @return
     */
    @PostMapping(value = "/generate/sdk")
    @ApiOperationSupport(order =2)
    @ApiOperation(value = "对sdk使用的接口")
    public GenerateVO generateDataForSdk(@RequestBody  GenerateBySqlRequest sqlRequest, HttpServletRequest request){
        //校验签字，时间戳，公钥
        BusinessException.throwIf(sqlRequest == null);

        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body =request.getHeader("body");
        String newBody  = new String(body.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        User invokeUser = userService.getOne(new QueryWrapper<User>().eq("access_key", accessKey));
        BusinessException.throwIf(invokeUser==null);
        BusinessException.throwIf(Long.parseLong(nonce)>10000L, HttpCode.NO_AUTH_ERROR);
        // 时间和当前时间不能超过 5 分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;

        BusinessException.throwIf((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES,HttpCode.NO_AUTH_ERROR);
        // 实际情况中是从数据库中查出 secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(newBody, secretKey);
        BusinessException.throwIf(sign == null || !sign.equals(serverSign),HttpCode.NO_AUTH_ERROR);
        TableSchema schema = TableSchemaBuilder.buildFromSql(sqlRequest.getSql());
        // todo 根据sql的类型设置模拟策略

        return GeneratorFacade.generateAll(schema);
    }

    @PostMapping("/generate/code")
    public void makeCode(@RequestBody GenerateBySqlRequest sqlRequest, HttpServletResponse response) throws IOException {
        // 生成在项目路径下的generated

        String id = IdUtil.getSnowflakeNextId() + RandomUtil.randomString(6);
        // 将生成的文件打压缩包让前端下载
        String path=System.getProperty("user.dir")+"/.temp/code/"+id+"/generated";
        String sql = sqlRequest.getSql().replace("\\n", "\n").replace("\\'", "'");
        LmsCodeGenerator.doGenerate(SqlConstant.DEFAULT_GENERATED_PACEKAGE,sql,path);

        // 6）下载制作好的生成器压缩包
        // 压缩得到的生成结果，返回给前端
        String resultPath = System.getProperty("user.dir")+"/.temp/code/"+id + "/resultCode.zip";
        File resultFile = ZipUtil.zip(path, resultPath);

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
        Files.copy(resultFile.toPath(), response.getOutputStream());

        // 删除临时文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(path);
            FileUtil.del(resultPath);
        });
    }
    /**
     * 获取生成的模拟数据
     *
     * @param tableSchema
     * @return
     */
    @PostMapping("/generate/schema")
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "获取生成的模拟数据")
    public GenerateVO generateBySchema(@RequestBody TableSchema tableSchema) {
        return GeneratorFacade.generateAll(tableSchema);
    }



    /**
     * 根据 文本 获取 schema
     *
     * @param autoRequest
     * @return
     */
    @PostMapping("/get/schema/auto")
    @ApiOperationSupport(order =4)
    @ApiOperation(value = "根据 文本 获取 schema")
    public TableSchema getSchemaByAuto(@RequestBody GenerateByAutoRequest autoRequest) {
        BusinessException.throwIf(autoRequest == null);
        return TableSchemaBuilder.buildFromAuto(autoRequest.getContent());
    }
    /**
     * 根据 SQL 获取 schema
     *
     * @param sqlRequest
     * @return
     */
    @PostMapping("/get/schema/sql")
    @ApiOperationSupport(order =5)
    @ApiOperation(value = "根据 SQL 获取 schema")
    public TableSchema getSchemaBySql(@RequestBody GenerateBySqlRequest sqlRequest) {
        BusinessException.throwIf(sqlRequest == null);
        // 获取 tableSchema
        return TableSchemaBuilder.buildFromSql(sqlRequest.getSql());
    }
    /**
     * 根据 excel获取schema
     *
     * @param file
     * @return
     */
    @PostMapping("/get/schema/excel")
    @ApiOperationSupport(order =6)
    @ApiOperation(value = "根据 excel获取schema")
    public TableSchema getSchemaByExcel(MultipartFile file) {
        return TableSchemaBuilder.buildFromExcel(file);
    }


}
