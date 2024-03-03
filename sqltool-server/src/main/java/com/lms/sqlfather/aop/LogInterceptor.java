package com.lms.sqlfather.aop;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.lmscommon.common.AppLogLevel;
import com.lms.lmscommon.utils.ClassUtil;
import com.lms.lmscommon.utils.JsonUtil;
import com.lms.sqlfather.config.LogProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Aspect
@Component
public class LogInterceptor {

    @Resource
    private LogProperties logProperties;

    @Around("((@within(org.springframework.stereotype.Controller) ||" +
            " @within(org.springframework.web.bind.annotation.RestController)) && !@annotation(com.lms.sqlfather.annotation.IgnoreLog))")
     public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {

        AppLogLevel level = logProperties.getLevel();
        // 如果为NONE就不去打印
        if (AppLogLevel.NONE == level) {
            return joinPoint.proceed();
        } else {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request =((ServletRequestAttributes) requestAttributes).getRequest();
            String requestUrl = ((HttpServletRequest)Objects.requireNonNull(request)).getRequestURI();
            String requestMethod = request.getMethod();
            JSONObject logItem = new JSONObject();
            logItem.putIfAbsent("method", requestMethod);
            logItem.putIfAbsent("url", requestUrl);
            this.logIngArgs(joinPoint, logItem);
            this.logIngHeaders(request, level, logItem);
            long startNs = System.nanoTime();
            boolean flag = false;
            Object result;
            try {
                flag = true;
                 result = joinPoint.proceed();
                if (AppLogLevel.BODY.lte(level)) {
                    logItem.putIfAbsent("response", JSONUtil.toJsonStr(result));
                }
                flag = false;
            } finally {
                if (flag) {
                    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
                    logItem.putIfAbsent("costTime", tookMs);
                    log.info("{}", logItem.toStringPretty());
                }
            }
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            logItem.putIfAbsent("costTime", tookMs);
            log.info("{}", logItem.toStringPretty());
            return result;
        }
     }

     public void logIngArgs(ProceedingJoinPoint point,JSONObject logItem){
         MethodSignature ms = (MethodSignature)point.getSignature();
         Method method = ms.getMethod();
         Object[] args = point.getArgs();
         Map<String, Object> paraMap = new HashMap(16);
         Object requestBodyValue = null;

         for(int i = 0; i < args.length; ++i) {
             MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
             PathVariable pathVariable = (PathVariable)methodParam.getParameterAnnotation(PathVariable.class);
             if (pathVariable == null) {
                 RequestBody requestBody = (RequestBody)methodParam.getParameterAnnotation(RequestBody.class);
                 String parameterName = methodParam.getParameterName();
                 Object value = args[i];
                 if (requestBody != null) {
                     requestBodyValue = value;
                 } else if (value instanceof HttpServletRequest) {
                     paraMap.putAll(((HttpServletRequest)value).getParameterMap());
                 } else if (value instanceof WebRequest) {
                     paraMap.putAll(((WebRequest)value).getParameterMap());
                 } else if (!(value instanceof HttpServletResponse)) {
                     String paraName;
                     if (value instanceof MultipartFile) {
                         MultipartFile multipartFile = (MultipartFile)value;
                         paraName = multipartFile.getName();
                         String fileName = multipartFile.getOriginalFilename();
                         paraMap.put(paraName, fileName);
                     } else if (value instanceof MultipartFile[]) {
                         MultipartFile[] arr = (MultipartFile[])((MultipartFile[])value);
                         if (arr.length != 0) {
                             paraName = arr[0].getName();
                             StringBuilder sb = new StringBuilder(arr.length);
                             MultipartFile[] var27 = arr;
                             int var18 = arr.length;

                             for(int var19 = 0; var19 < var18; ++var19) {
                                 MultipartFile multipartFile = var27[var19];
                                 sb.append(multipartFile.getOriginalFilename());
                                 sb.append(",");
                             }

                             paraMap.put(paraName, StrUtil.removeSuffix(sb.toString(), ","));
                         }
                     } else {
                         if (value instanceof List) {
                             List<?> list = (List)value;
                             AtomicBoolean isSkip = new AtomicBoolean(false);
                             Iterator var16 = list.iterator();

                             while(var16.hasNext()) {
                                 Object o = var16.next();
                                 if ("StandardMultipartFile".equalsIgnoreCase(o.getClass().getSimpleName())) {
                                     isSkip.set(true);
                                     break;
                                 }
                             }

                             if (isSkip.get()) {
                                 paraMap.put(parameterName, "此参数不能序列化为json");
                                 continue;
                             }
                         }

                         RequestParam requestParam = (RequestParam)methodParam.getParameterAnnotation(RequestParam.class);
                         paraName = parameterName;
                         if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
                             paraName = requestParam.value();
                         }

                         if (value == null) {
                             paraMap.put(paraName, (Object)null);
                         } else if (ClassUtil.isPrimitiveOrWrapper(value.getClass())) {
                             paraMap.put(paraName, value);
                         } else if (value instanceof InputStream) {
                             paraMap.put(paraName, "InputStream");
                         } else if (value instanceof InputStreamSource) {
                             paraMap.put(paraName, "InputStreamSource");
                         } else if (JsonUtil.canSerialize(value)) {
                             paraMap.put(paraName, value);
                         } else {
                             paraMap.put(paraName, "此参数不能序列化为json");
                         }
                     }
                 }
             }
         }

         if (!paraMap.isEmpty()) {
             logItem.putIfAbsent("parameters", JsonUtil.toJson(paraMap));
         }

         if (requestBodyValue != null) {
             if (requestBodyValue instanceof String) {
                 logItem.putIfAbsent("body", requestBodyValue);
             } else {
                 logItem.putIfAbsent("body", JsonUtil.toJson(requestBodyValue));
             }
         }
     }


    public void logIngHeaders(HttpServletRequest request, AppLogLevel level, JSONObject logItem) {
        if (AppLogLevel.HEADERS.lte(level)) {
            JSONObject tmp = new JSONObject();
            Enumeration<String> headers = request.getHeaderNames();

            while(headers.hasMoreElements()) {
                String headerName = (String)headers.nextElement();
                String headerValue = request.getHeader(headerName);
                tmp.putIfAbsent(headerName, headerValue);
            }
            if (!tmp.isEmpty()) {
                logItem.putIfAbsent("headers", tmp);
            }
        }

    }

}
