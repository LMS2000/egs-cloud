package com.lms.sqlfather.service.impl;


import com.lms.lmscommon.constant.SqlConstant;
import com.lms.sqlfather.service.AiService;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.model.DevChatRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AiServiceImpl implements AiService {


    private  final YuCongMingClient yuCongMingClient;
    @Override
    public String getAiCreateSql(String message) {

        DevChatRequest request=new DevChatRequest();
        request.setMessage(message+ SqlConstant.AI_BUILD_SUFFIX);
        request.setModelId(1654785040361893889L);
        StringBuilder stringBuilder=new StringBuilder();
        String content = yuCongMingClient.doChat(request).getData().getContent();

        String regex = "CREATE TABLE [^;]+;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String createTableStatement = matcher.group();
            stringBuilder.append(createTableStatement).append("\n");
        }
        return stringBuilder.toString();
    }
}
