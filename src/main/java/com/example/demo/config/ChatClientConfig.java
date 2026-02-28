package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClient 配置类
 *
 * Spring AI 的核心 API 是 ChatClient，它提供了 fluent API 用于与 AI 模型交互。
 * 通过 Builder 模式可以配置不同的选项，如 temperature、system prompt 等。
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/chatclient.html
 */
@Configuration
public class ChatClientConfig {

    /**
     * 默认 ChatClient
     *
     * 使用默认配置，temperature 默认为 0.7
     * 适用于大多数场景的通用对话
     */
    @Bean
    public ChatClient defaultChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    /**
     * 创意写作 ChatClient
     *
     * temperature = 0.9: 更高的随机性，产生更有创意、更意外的输出
     * 适用于：创意写作、头脑风暴、生成故事等
     */
    @Bean
    public ChatClient creativeChatClient(OpenAiChatModel chatModel) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.9)
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }

    /**
     * 精确回答 ChatClient
     *
     * temperature = 0.2: 更低的随机性，产生更确定性、可预测的输出
     * 适用于：代码生成、数学计算、事实问答等需要准确性的场景
     */
    @Bean
    public ChatClient preciseChatClient(OpenAiChatModel chatModel) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.2)
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }

    /**
     * 角色扮演 ChatClient
     *
     * 通过 defaultSystem() 设置系统提示词，定义 AI 的行为角色
     * 适用于：特定角色的对话、风格化回答等
     */
    @Bean
    public ChatClient pirateChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a friendly chat bot that answers question in the voice of a Pirate")
                .build();
    }
}