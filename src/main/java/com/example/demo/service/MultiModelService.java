package com.example.demo.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 多模型服务示例
 *
 * 展示如何使用 Spring AI 的 mutate() 方法创建动态配置的模型实例
 *
 * 核心概念：
 * - mutate() 方法可以从现有模型派生出新的模型配置
 * - 可以修改：baseUrl、apiKey、model、temperature 等参数
 * - 适用于：同一个应用需要调用多个不同模型或配置的场景
 *
 */
@Service
public class MultiModelService {

    private static final Logger logger = LoggerFactory.getLogger(MultiModelService.class);

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private OpenAiApi openAiApi;

    /**
     * 获取当前配置的 API Key
     * 从 application.yml 读取: spring.ai.openai.api-key
     */
    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    /**
     * 演示多模型切换功能
     *
     * 此方法展示如何：
     * 1. 从现有的 OpenAiApi 派生出新的 API 配置（不同 baseUrl）
     * 2. 从现有的 OpenAiChatModel 派生出新的模型配置（不同 model）
     * 3. 创建新的 ChatClient 用于特定模型调用
     */
    public List<Map<String, String>> multiClientFlow() {
        // 提示：实际使用中应该从环境变量或配置中心获取敏感信息
        String effectiveApiKey = System.getenv("DASHSCOPE_API_KEY") != null
                ? System.getenv("DASHSCOPE_API_KEY")
                : apiKey;

        try {
            // ========== 1. 使用通义千问模型 ==========
            // 从现有 API 派生新的配置（修改 baseUrl 和 model）
            OpenAiApi qwenApi = openAiApi.mutate()
                    .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                    .apiKey(effectiveApiKey)
                    .build();

            // 从现有模型派生新的模型实例
            OpenAiChatModel qwenModel = openAiChatModel.mutate()
                    .openAiApi(qwenApi)
                    .defaultOptions(OpenAiChatOptions.builder()
                            .model("qwen3.5-plus")
                            .temperature(0.5)
                            .build())
                    .build();

            // 使用派生的模型创建 ChatClient
            String prompt = "What is the capital of France?";
            String qwenResponse = ChatClient.builder(qwenModel).build()
                    .prompt(prompt)
                    .call()
                    .content();

            logger.info("Qwen (qwen3.5-plus) response: {}", qwenResponse);

            // ========== 2. 如果需要使用其他模型（如 OpenAI GPT） ==========
            // 取消注释以下代码并设置环境变量 OPENAI_API_KEY
            // OpenAiApi gpt4Api = openAiApi.mutate()
            //         .baseUrl("https://api.openai.com")
            //         .apiKey(System.getenv("OPENAI_API_KEY"))
            //         .build();
            //
            // OpenAiChatModel gpt4Model = openAiChatModel.mutate()
            //         .openAiApi(gpt4Api)
            //         .defaultOptions(OpenAiChatOptions.builder()
            //                 .model("gpt-4")
            //                 .temperature(0.7)
            //                 .build())
            //         .build();
            //
            // String gpt4Response = ChatClient.builder(gpt4Model).build()
            //         .prompt(prompt)
            //         .call()
            //         .content();
            //
            // logger.info("OpenAI GPT-4 response: {}", gpt4Response);

            return List.of(
                    Map.of("model", "qwen3.5-plus", "response", qwenResponse)
            );

        } catch (Exception e) {
            logger.error("Error in multi-client flow", e);
            throw new RuntimeException("Failed to execute multi-model flow", e);
        }
    }
}