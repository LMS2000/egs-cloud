package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.*;
import cn.hutool.json.JSONUtil;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.util.concurrent.RateLimiter;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.FileConstant;
import com.lms.lmscommon.constant.UserConstant;

import com.lms.lmscommon.model.dto.generator.*;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.maker.generator.main.GenerateTemplate;
import com.lms.maker.generator.main.ZipGenerator;
import com.lms.maker.meta.Meta;
import com.lms.maker.meta.MetaValidator;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.annotation.IgnoreLog;
import com.lms.sqlfather.client.OssClient;
import com.lms.sqlfather.service.GeneratorService;
import com.lms.sqlfather.service.GeneratorServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 *  代码生成器
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/generator")
@Slf4j
@AllArgsConstructor
@EnableResponseAdvice
@Api(value = "生成器管理")
public class GeneratorController {


    private final GeneratorService generatorService;

    private final GeneratorServiceFacade generatorServiceFacade;


    private final OssClient client;

    private static final RateLimiter MAKE_LIMITER = RateLimiter.create(10);

    /**
     * 创建
     *
     * @param generatorAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckLogin
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "创建")
    public Long addGenerator(@RequestBody GeneratorAddRequest generatorAddRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return generatorService.addGenerator(generatorAddRequest,loginId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @SaCheckLogin
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "删除")
    public Boolean deleteGenerator(@RequestBody DeleteRequest deleteRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return generatorServiceFacade.deleteGenerator(deleteRequest,loginId);
    }

    /**
     * 更新（仅管理员）
     *
     * @param generatorUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "修改")
    public Boolean updateGenerator(@RequestBody GeneratorUpdateRequest generatorUpdateRequest) {
       return generatorService.updateGenerator(generatorUpdateRequest);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据id获取")
    public GeneratorVO getGeneratorVOById(@Positive(message = "id不合法") Long id) {
        Generator generator = generatorService.getById(id);
        if (ObjectUtil.isEmpty(generator)) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR);
        }
        return generatorServiceFacade.getGeneratorVO(generator);
    }

    /**
     * 根据id查询携带点赞收藏的生成器信息
     * @param id
     * @return
     */
    @GetMapping("/get/vo/with")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "根据id获取")
    public GeneratorVO getGernertorByIdWithStarAndFavour(@Positive(message = "id不合法") Long id){
        Long userId = null;
        if(StpUtil.isLogin()){
            userId= Long.parseLong((String) StpUtil.getLoginId());
        }
        return generatorServiceFacade.getGeneratorWithStarAndFavour(id,userId);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param generatorQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "分页获取列表（仅管理员）")
    public Page<Generator> listGeneratorByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        return generatorPage;
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param generatorQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "分页获取列表（封装类）")
    public Page<GeneratorVO> listGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest) {
     return generatorServiceFacade.listGeneratorVOByPage(generatorQueryRequest);
    }

    /**
     * 快速分页获取列表（接入redis缓存）
     *
     * @param generatorQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo/fast")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "快速分页获取列表（封装类）")
    public Page<GeneratorVO> listGeneratorVOByPageFast(@RequestBody GeneratorQueryRequest generatorQueryRequest) {
         return generatorServiceFacade.listGeneratorVOByPageFast(generatorQueryRequest);
    }


    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param generatorQueryRequest
     * @return
     */
    @PostMapping("/my/list/page/vo")
    @SaCheckLogin
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "分页获取当前用户创建的资源列表")
    public Page<GeneratorVO> listMyGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest) {
        return generatorServiceFacade.listMyGeneratorVOByPage(generatorQueryRequest);
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param generatorEditRequest
     * @return
     */
    @PostMapping("/edit")
    @SaCheckLogin
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "分页获取当前用户创建的资源列表")
    public Boolean editGenerator(@RequestBody @Valid GeneratorEditRequest generatorEditRequest) {
      return generatorServiceFacade.editGenerator(generatorEditRequest);
    }

    /**
     * 根据 id 下载
     *
     * @param id
     * @return
     */
    @GetMapping("/download")
    @SaCheckLogin
    public void downloadGeneratorById(@Positive(message = "id不合法") Long id, HttpServletResponse response) throws IOException {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR);
        }
        String filepath = generator.getDistPath();
        if (StrUtil.isBlank(filepath)) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR, "产物包不存在");
        }

        // 追踪事件
        log.info("用户 {} 下载了 {}", loginId, filepath);

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filepath);

//        // 从缓存中尝试获取
        String zipFilePath = getCacheFilePath(id, filepath);
        if (FileUtil.exist(zipFilePath)) {
            // 写入响应
            Files.copy(Paths.get(zipFilePath), response.getOutputStream());
            return;
        }

        try {
            GetObjectRequest getObjectRequest=new GetObjectRequest(FileConstant.BUCKET_NAME, filepath);
            S3Object s3Object = client.getS3Client().getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            // 使用 IOUtils.copy() 方法将输入流内容复制到输出流（本地文件）
            IOUtils.copy(objectInputStream, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "下载失败");
        }
    }

    /**
     * 使用代码生成器
     *
     * @param generatorUseRequest
     * @param response
     * @return
     */
    @PostMapping("/use")
    @SaCheckLogin
    public void useGenerator(@RequestBody GeneratorUseRequest generatorUseRequest, HttpServletResponse response) throws IOException {
        // 获取用户输入的请求参数
        Long id = generatorUseRequest.getId();
        Map<String, Object> dataModel = generatorUseRequest.getDataModel();
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        // 需要用户登录
        log.info("userId = {} 使用了生成器 id = {}", loginId, id);
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR);
        }

        // 生成器的存储路径
        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR, "产物包不存在");
        }

        // 从对象存储下载生成器的压缩包
        // 定义独立的工作空间
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/use/%s", projectPath, id);
        String zipFilePath = tempDirPath + "/dist.zip";

        // 使用文件缓存
        String cacheFilePath = getCacheFilePath(id, distPath);
        Path cacheFilePathObj = Paths.get(cacheFilePath);
        Path zipFilePathObj = Paths.get(zipFilePath);

        if (!FileUtil.exist(zipFilePath)) {
            // 有缓存，复制文件
            if (FileUtil.exist(cacheFilePath)) {
                Files.copy(cacheFilePathObj, zipFilePathObj);
            } else {
                // 没有缓存，从对象存储下载文件
                FileUtil.touch(zipFilePath);
                try {
                    GetObjectRequest getObjectRequest=new GetObjectRequest(FileConstant.BUCKET_NAME, distPath);
                    S3Object s3Object = client.getS3Client().getObject(getObjectRequest);
                    S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
                    // 使用 IOUtils.copy() 方法将输入流内容复制到输出流（本地文件）
                    try (FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath)) {
                        IOUtils.copy(objectInputStream, fileOutputStream);
                    }
                } catch (Exception e) {
                    throw new BusinessException(HttpCode.SYSTEM_ERROR, "生成器下载失败");
                }
                // 写文件缓存
                File parentFile = cacheFilePathObj.toFile().getParentFile();
                if (!FileUtil.exist(parentFile)) {
                    FileUtil.mkdir(parentFile);
                }
                Files.copy(zipFilePathObj, cacheFilePathObj);
            }
        }
        // 解压压缩包，得到脚本文件

        File unzipDistDir = ZipUtil.unzip(zipFilePath);


        // 将用户输入的参数写到 json 文件中
        String dataModelFilePath = tempDirPath + "/dataModel.json";
        String jsonStr = JSONUtil.toJsonStr(dataModel);
        FileUtil.writeUtf8String(jsonStr, dataModelFilePath);

        // 执行脚本
        // 找到脚本文件所在路径
        // 要注意，如果不是 windows 系统，找 generator 文件而不是 bat
        File scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
                .stream()
                .filter(file -> file.isFile()
                        && "generator.bat".equals(file.getName()))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        // 添加可执行权限
        try {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(scriptFile.toPath(), permissions);
        } catch (Exception e) {

        }

        // 构造命令
        File scriptDir = scriptFile.getParentFile();
        // 注意，如果是 mac / linux 系统，要用 "./generator"
        String scriptAbsolutePath = scriptFile.getAbsolutePath().replace("\\", "/");
        String[] commands = new String[]{scriptAbsolutePath, "json-generate", "--file=" + dataModelFilePath};

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(scriptDir);

        try {
            Process process = processBuilder.start();

            // 读取命令的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("命令执行结束，退出码：" + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "执行生成器脚本错误");
        }

        // 压缩得到的生成结果，返回给前端
        String generatedPath = scriptDir.getAbsolutePath() + "/generated";
        String resultPath = tempDirPath + "/result.zip";
        File resultFile = ZipUtil.zip(generatedPath, resultPath);

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
        Files.copy(resultFile.toPath(), response.getOutputStream());

        // 清理文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }
    /**
     * 制作代码生成器
     *
     * @param generatorMakeRequest
     * @param response
     */
    @PostMapping("/make")
    @SaCheckLogin
    @IgnoreLog
    public void makeGenerator(@RequestBody GeneratorMakeRequest generatorMakeRequest, HttpServletResponse response) throws IOException {
        if (!MAKE_LIMITER.tryAcquire()) {
            throw new BusinessException(HttpCode.OPERATION_ERROR);
        }


        // 1) 输入参数
        Meta meta = generatorMakeRequest.getMeta();
        String zipFilePath = generatorMakeRequest.getZipFilePath();

        // 需要用户登录
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        log.info("userId = {} 在线制作生成器", loginId);

        // 2) 创建独立的工作空间，下载压缩包到本地
        String projectPath = System.getProperty("user.dir");
        String id = IdUtil.getSnowflakeNextId() + RandomUtil.randomString(6);
        String tempDirPath = String.format("%s/.temp/make/%s", projectPath, id);
        String localZipFilePath = tempDirPath + "/project.zip";

        if (!FileUtil.exist(localZipFilePath)) {
            FileUtil.touch(localZipFilePath);
        }

        // 下载文件
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
//            String objectURL = client.getObjectURL(FileConstant.BUCKET_NAME, zipFilePath);
            GetObjectRequest getObjectRequest = new GetObjectRequest(FileConstant.BUCKET_NAME, zipFilePath);
            S3Object s3Object = client.getS3Client().getObject(getObjectRequest);
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            // 使用 IOUtils.copy() 方法将输入流内容复制到输出流（本地文件）
            try (FileOutputStream fileOutputStream = new FileOutputStream(localZipFilePath)) {
                IOUtils.copy(objectInputStream, fileOutputStream);
            }

//            FileUtil.writeBytes(objectURL.getBytes(),localZipFilePath);
            stopWatch.stop();
            System.out.println("下载文件：" + stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "压缩包下载失败");
        }

        // 3）解压，得到项目模板文件
        File unzipDistDir = ZipUtil.unzip(localZipFilePath);

        // 4）构造 meta 对象和生成器的输出路径
        String sourceRootPath = unzipDistDir.getAbsolutePath();
        meta.getFileConfig().setSourceRootPath(sourceRootPath);
        // 扫描其他不属于原有fileConfig的文件路径

        // 都添加为静态的路径追加到meta 类中

        // 校验和处理默认值
        MetaValidator.doValidAndFill(meta);
        String outputPath = tempDirPath + "/generated/" + meta.getName();

        // 5）调用 maker 方法制作生成器
        GenerateTemplate generateTemplate = new ZipGenerator();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            generateTemplate.doGenerate(meta, outputPath);
            stopWatch.stop();
            System.out.println("制作耗时：" + stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "制作失败");
        }

        // 6）下载制作好的生成器压缩包
        String suffix = "-dist.zip";
        String zipFileName = meta.getName() + suffix;
        // 生成器压缩包的绝对路径
        String distZipFilePath = outputPath + suffix;

        // 设置响应头
        response.setContentType("application/octet-stream;charSet=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
        Files.copy(Paths.get(distZipFilePath), response.getOutputStream());
        // 7）清理工作空间的文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }

    /**
     * 缓存代码生成器
     *
     * @param generatorCacheRequest
     * @return
     */
    @PostMapping("/cache")
    @SaCheckLogin
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public void cacheGenerator(@RequestBody @Valid GeneratorCacheRequest generatorCacheRequest, HttpServletResponse response) throws IOException {
        if (generatorCacheRequest == null || generatorCacheRequest.getId() <= 0) {
            throw new BusinessException(HttpCode.PARAMS_ERROR);
        }

        long id = generatorCacheRequest.getId();
        Generator generator = generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR);
        }

        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(HttpCode.NOT_FOUND_ERROR, "产物包不存在");
        }

        String zipFilePath = getCacheFilePath(id, distPath);

        try {
            String objectURL = client.getObjectURL(FileConstant.BUCKET_NAME, distPath);
            // 处理下载到的流
            byte[] bytes = objectURL.getBytes(StandardCharsets.UTF_8);
            FileUtil.writeBytes(bytes,zipFilePath);
        } catch (Exception e) {
            throw new BusinessException(HttpCode.SYSTEM_ERROR, "生成器下载失败");
        }
    }

    /**
     * 获取缓存文件路径
     *
     * @param id
     * @param distPath
     * @return
     */
    public String getCacheFilePath(long id, String distPath) {
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/cache/%s", projectPath, id);
        String zipFilePath = tempDirPath + "/" + distPath;
        return zipFilePath;
    }
//
//    /**
//     * 获取分页缓存 keu
//     *
//     * @param generatorQueryRequest
//     * @return
//     */
//    public static String getPageCacheKey(GeneratorQueryRequest generatorQueryRequest) {
//        String jsonStr = JSONUtil.toJsonStr(generatorQueryRequest);
//        // 请求参数编码
//        String base64 = Base64Encoder.encode(jsonStr);
//        String key = "generator:page:" + base64;
//        return key;
//    }

}










