package com.example.demo.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

@Service
public class MultiModelService {

    private static final Logger logger = LoggerFactory.getLogger(MultiModelService.class);

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private OpenAiApi openAiApi;

    public void multiClientFlow() {
        try {
            // Derive a new OpenAiApi for Qwen
            OpenAiApi qwenApi = openAiApi.mutate()
                    .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
                    .apiKey(System.getenv("sk-8932ad48263c4b89950145445e068d3c"))
                    .build();

            // Derive a new OpenAiChatModel for Qwen
            OpenAiChatModel groqModel = openAiChatModel.mutate()
                    .openAiApi(qwenApi)
                    .defaultOptions(OpenAiChatOptions.builder().model("qwen3.5-plus").temperature(0.5).build())
                    .build();


            // Simple prompt for both models
            String prompt = "What is the capital of France?";
            String qwenResponse = ChatClient.builder(groqModel).build().prompt(prompt).call().content();
            logger.info("Qwen (qwen3.5-plus) response: {}", qwenResponse);

//
//            // Derive a new OpenAiApi for OpenAI GPT-4
//            OpenAiApi gpt4Api = openAiApi.mutate()
//                    .baseUrl("https://api.openai.com")
//                    .apiKey(System.getenv("OPENAI_API_KEY"))
//                    .build();
//
//
//            // Derive a new OpenAiChatModel for GPT-4
//            OpenAiChatModel gpt4Model = openAiChatModel.mutate()
//                    .openAiApi(gpt4Api)
//                    .defaultOptions(OpenAiChatOptions.builder().model("gpt-4").temperature(0.7).build())
//                    .build();
//
//            String gpt4Response = ChatClient.builder(gpt4Model).build().prompt(prompt).call().content();
//            logger.info("OpenAI GPT-4 response: {}", gpt4Response);
        } catch (Exception e) {
            logger.error("Error in multi-client flow", e);
        }
    }
}