package com.lms.lmsclient.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lms.lmsclient.model.dto.LmsRequest;
import com.lms.lmsclient.model.dto.LmsResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.lms.lmsclient.utils.SignUtils.genSign;


/**
 * 调用第三方接口的客户端
 *
 * @author LMS
 */
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class SqlToolApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private static final String DATA="data";
    private String accessKey;
    private String secretKey;




    public LmsResponse randomData(LmsRequest request)  {



        String requestBodyJson = JSONUtil.toJsonStr(request);
        log.info("转换的请求json:"+requestBodyJson);

        HttpResponse response = HttpUtil.createPost(GATEWAY_HOST + "/api/sql/generate/sdk")
                .addHeaders(getHeaderMap(requestBodyJson))
                .body(requestBodyJson)
                .execute();
        String responseBody = response.body();
        log.info("响应体："+responseBody);
        return getResponseData(responseBody);
    }


    private LmsResponse getResponseData(String body){
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject data =(JSONObject) jsonObject.get(DATA);
        return data.toJavaObject(LmsResponse.class);
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
//        hashMap.put("secretKey", secretKey);
        hashMap.put("Content-Type", "application/json; charset=UTF-8");
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        log.info("生成的签字为:"+genSign(body,secretKey));
        return hashMap;
    }


}
