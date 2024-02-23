package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.generator.GeneratorAddRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorEditRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorQueryRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorUpdateRequest;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.service.GeneratorService;
import com.lms.sqlfather.service.GeneratorServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

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
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return generatorServiceFacade.getGeneratorVO(generator,loginId);
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

//    /**
//     * 根据 id 下载
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("/download")
//    public void downloadGeneratorById(@Positive(message = "id不合法") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        User loginUser = userService.getLoginUser(request);
//        Generator generator = generatorService.getById(id);
//        if (generator == null) {
//            throw new BusinessException(HttpCode.NOT_FOUND_ERROR);
//        }
//
//        String filepath = generator.getDistPath();
//        if (StrUtil.isBlank(filepath)) {
//            throw new BusinessException(HttpCode.NOT_FOUND_ERROR, "产物包不存在");
//        }
//
//        // 追踪事件
//        log.info("用户 {} 下载了 {}", loginUser, filepath);
//
//        // 设置响应头
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
//
//        String zipFilePath = getCacheFilePath(id, filepath);
//        if (FileUtil.exist(zipFilePath)) {
//            // 写入响应
//            Files.copy(Paths.get(zipFilePath), response.getOutputStream());
//            return;
//        }
//
//        COSObjectInputStream cosObjectInput = null;
//        try {
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            COSObject cosObject = cosManager.getObject(filepath);
//            cosObjectInput = cosObject.getObjectContent();
//
//            // 处理下载到的流
//            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
//
//            stopWatch.stop();
//            System.out.println("下载耗时：" + stopWatch.getTotalTimeMillis());
//
//            // 写入响应
//            response.getOutputStream().write(bytes);
//            response.getOutputStream().flush();
//        } catch (Exception e) {
//            log.error("file download error, filepath = " + filepath, e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
//        } finally {
//            if (cosObjectInput != null) {
//                cosObjectInput.close();
//            }
//        }
//    }

//    /**
//     * 使用代码生成器
//     *
//     * @param generatorUseRequest
//     * @param request
//     * @param response
//     * @return
//     */
//    @PostMapping("/use")
//    public void useGenerator(@RequestBody GeneratorUseRequest generatorUseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // 获取用户输入的请求参数
//        Long id = generatorUseRequest.getId();
//        Map<String, Object> dataModel = generatorUseRequest.getDataModel();
//
//        // 需要用户登录
//        User loginUser = userService.getLoginUser(request);
//        log.info("userId = {} 使用了生成器 id = {}", loginUser.getId(), id);
//
//        Generator generator = generatorService.getById(id);
//        if (generator == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//
//        // 生成器的存储路径
//        String distPath = generator.getDistPath();
//        if (StrUtil.isBlank(distPath)) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产物包不存在");
//        }
//
//        // 从对象存储下载生成器的压缩包
//        // 定义独立的工作空间
//        String projectPath = System.getProperty("user.dir");
//        String tempDirPath = String.format("%s/.temp/use/%s", projectPath, id);
//        String zipFilePath = tempDirPath + "/dist.zip";
//
//        if (!FileUtil.exist(zipFilePath)) {
//            FileUtil.touch(zipFilePath);
//        }
//
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        try {
//            cosManager.download(distPath, zipFilePath);
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成器下载失败");
//        }
//        stopWatch.stop();
//        System.out.println("下载耗时：" + stopWatch.getTotalTimeMillis());
//
//        // 解压压缩包，得到脚本文件
//        stopWatch = new StopWatch();
//        stopWatch.start();
//        File unzipDistDir = ZipUtil.unzip(zipFilePath);
//        stopWatch.stop();
//        System.out.println("解压耗时：" + stopWatch.getTotalTimeMillis());
//
//        // 将用户输入的参数写到 json 文件中
//        stopWatch = new StopWatch();
//        stopWatch.start();
//        String dataModelFilePath = tempDirPath + "/dataModel.json";
//        String jsonStr = JSONUtil.toJsonStr(dataModel);
//        FileUtil.writeUtf8String(jsonStr, dataModelFilePath);
//        stopWatch.stop();
//        System.out.println("写数据文件：" + stopWatch.getTotalTimeMillis());
//
//        // 执行脚本
//        // 找到脚本文件所在路径
//        // 要注意，如果不是 windows 系统，找 generator 文件而不是 bat
//        File scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
//                .stream()
//                .filter(file -> file.isFile()
//                        && "generator.bat".equals(file.getName()))
//                .findFirst()
//                .orElseThrow(RuntimeException::new);
//
//        // 添加可执行权限
//        try {
//            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
//            Files.setPosixFilePermissions(scriptFile.toPath(), permissions);
//        } catch (Exception e) {
//
//        }
//
//        // 构造命令
//        File scriptDir = scriptFile.getParentFile();
//        // 注意，如果是 mac / linux 系统，要用 "./generator"
//        String scriptAbsolutePath = scriptFile.getAbsolutePath().replace("\\", "/");
//        String[] commands = new String[]{scriptAbsolutePath, "json-generate", "--file=" + dataModelFilePath};
//
//        // 这里一定要拆分！
//        ProcessBuilder processBuilder = new ProcessBuilder(commands);
//        processBuilder.directory(scriptDir);
//
//        try {
//            stopWatch = new StopWatch();
//            stopWatch.start();
//            Process process = processBuilder.start();
//
//            // 读取命令的输出
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 等待命令执行完成
//            int exitCode = process.waitFor();
//            System.out.println("命令执行结束，退出码：" + exitCode);
//            stopWatch.stop();
//            System.out.println("执行脚本：" + stopWatch.getTotalTimeMillis());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "执行生成器脚本错误");
//        }
//
//        // 压缩得到的生成结果，返回给前端
//        stopWatch = new StopWatch();
//        stopWatch.start();
//        String generatedPath = scriptDir.getAbsolutePath() + "/generated";
//        String resultPath = tempDirPath + "/result.zip";
//        File resultFile = ZipUtil.zip(generatedPath, resultPath);
//        stopWatch.stop();
//        System.out.println("压缩结果：" + stopWatch.getTotalTimeMillis());
//
//        // 设置响应头
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
//        Files.copy(resultFile.toPath(), response.getOutputStream());
//
//        // 清理文件
//        CompletableFuture.runAsync(() -> {
//            FileUtil.del(tempDirPath);
//        });
//    }

//    /**
//     * 制作代码生成器
//     *
//     * @param generatorMakeRequest
//     * @param request
//     * @param response
//     * @return
//     */
//    @PostMapping("/make")
//    public void makeGenerator(@RequestBody GeneratorMakeRequest generatorMakeRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // 1) 输入参数
//        Meta meta = generatorMakeRequest.getMeta();
//        String zipFilePath = generatorMakeRequest.getZipFilePath();
//
//        // 需要用户登录
//        User loginUser = userService.getLoginUser(request);
//        log.info("userId = {} 在线制作生成器", loginUser.getId());
//
//        // 2) 创建独立的工作空间，下载压缩包到本地
//        String projectPath = System.getProperty("user.dir");
//        String id = IdUtil.getSnowflakeNextId() + RandomUtil.randomString(6);
//        String tempDirPath = String.format("%s/.temp/make/%s", projectPath, id);
//        String localZipFilePath = tempDirPath + "/project.zip";
//
//        if (!FileUtil.exist(localZipFilePath)) {
//            FileUtil.touch(localZipFilePath);
//        }
//
//        // 下载文件
//        try {
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            cosManager.download(zipFilePath, localZipFilePath);
//            stopWatch.stop();
//            System.out.println("下载文件：" + stopWatch.getTotalTimeMillis());
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩包下载失败");
//        }
//
//        // 3）解压，得到项目模板文件
//        File unzipDistDir = ZipUtil.unzip(localZipFilePath);
//
//        // 4）构造 meta 对象和生成器的输出路径
//        String sourceRootPath = unzipDistDir.getAbsolutePath();
//        meta.getFileConfig().setSourceRootPath(sourceRootPath);
//        // 校验和处理默认值
//        MetaValidator.doValidAndFill(meta);
//        String outputPath = tempDirPath + "/generated/" + meta.getName();
//
//        // 5）调用 maker 方法制作生成器
//        GenerateTemplate generateTemplate = new ZipGenerator();
//        try {
//            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            generateTemplate.doGenerate(meta, outputPath);
//            stopWatch.stop();
//            System.out.println("制作耗时：" + stopWatch.getTotalTimeMillis());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "制作失败");
//        }
//
//        // 6）下载制作好的生成器压缩包
//        String suffix = "-dist.zip";
//        String zipFileName = meta.getName() + suffix;
//        // 生成器压缩包的绝对路径
//        String distZipFilePath = outputPath + suffix;
//
//        // 设置响应头
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
//        Files.copy(Paths.get(distZipFilePath), response.getOutputStream());
//
//        // 7）清理工作空间的文件
//        CompletableFuture.runAsync(() -> {
//            FileUtil.del(tempDirPath);
//        });
//    }

//    /**
//     * 缓存代码生成器
//     *
//     * @param generatorCacheRequest
//     * @return
//     */
//    @PostMapping("/cache")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public void cacheGenerator(@RequestBody GeneratorCacheRequest generatorCacheRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        if (generatorCacheRequest == null || generatorCacheRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//
//        long id = generatorCacheRequest.getId();
//        Generator generator = generatorService.getById(id);
//        if (generator == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//
//        String distPath = generator.getDistPath();
//        if (StrUtil.isBlank(distPath)) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "产物包不存在");
//        }
//
//        String zipFilePath = getCacheFilePath(id, distPath);
//
//        try {
//            cosManager.download(distPath, zipFilePath);
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成器下载失败");
//        }
//    }

//    /**
//     * 获取缓存文件路径
//     *
//     * @param id
//     * @param distPath
//     * @return
//     */
//    public String getCacheFilePath(long id, String distPath) {
//        String projectPath = System.getProperty("user.dir");
//        String tempDirPath = String.format("%s/.temp/cache/%s", projectPath, id);
//        String zipFilePath = tempDirPath + "/" + distPath;
//        return zipFilePath;
//    }
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










