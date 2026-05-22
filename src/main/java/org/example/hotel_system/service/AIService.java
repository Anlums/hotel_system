package org.example.hotel_system.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class AIService {

    @Value("${ai.deepseek.api-key}")
    private String apiKey;

    @Value("${ai.deepseek.model}")
    private String modelName;

    @Value("${ai.deepseek.base-url}")
    private String apiUrl;

    // 配置 OkHttpClient，建议增加超时时间以应对 V4-Pro 的长推理过程
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS) // 推理模式响应较慢，设置 60s 超时
            .build();

    /**
     * 调用 DeepSeek V4 API
     * @param systemPrompt 系统角色定义
     * @param userPrompt   用户具体需求
     * @return AI 的回答内容
     */
    public String getChatResponse(String systemPrompt, String userPrompt) {
        // 1. 构造请求体 (根据 DeepSeek V4 最新文档)
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", modelName);

        // 2. 构造消息队列
        JSONArray messages = new JSONArray();
        messages.add(createMessage("system", systemPrompt));
        messages.add(createMessage("user", userPrompt));
        requestBody.put("messages", messages);

        // 3. 配置 V4-Pro 特有的思维模式
        // 仅当模型为 v4-pro 或具有推理能力时开启
        if (modelName.contains("pro") || modelName.contains("reasoner")) {
            JSONObject thinking = new JSONObject();
            thinking.put("type", "enabled");
            requestBody.put("thinking", thinking);

            // 设置推理强度 (high/medium/low)
            requestBody.put("reasoning_effort", "high");
        }

        requestBody.put("stream", false);

        // 4. 构建 OkHttp 请求
        RequestBody body = RequestBody.create(
                requestBody.toJSONString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // 5. 执行请求并解析结果
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String resultJson = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(resultJson);

                // 解析返回路径：choices -> message -> content
                return jsonObject.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            } else {
                String errorMsg = response.body() != null ? response.body().string() : "未知错误";
                return "AI 服务调用失败，代码: " + response.code() + "，详情: " + errorMsg;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "网络连接异常，请检查 API 地址或密钥配置";
        }
    }

    /**
     * 辅助方法：创建消息对象
     */
    private JSONObject createMessage(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }
}