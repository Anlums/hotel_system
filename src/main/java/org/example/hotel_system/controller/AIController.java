package org.example.hotel_system.controller;

import org.example.hotel_system.common.Result;
import org.example.hotel_system.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userPrompt = request.get("prompt");

        // 设置系统提示词，让 AI 更有管家范儿
        String systemPrompt = "你现在是天玺尊邸酒店的 AI 智能管家，请用专业、尊贵的语气回答问题。";

        String result = aiService.getChatResponse(systemPrompt, userPrompt);

        Map<String, String> response = new HashMap<>();
        response.put("content", result); // 这里的 key 要和前端 res.data.content 对应
        return response;
    }
}

