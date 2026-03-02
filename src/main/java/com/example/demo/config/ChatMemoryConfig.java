package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat Memory 配置类
 *
 * 演示 Spring AI 的对话记忆功能
 * 使用内存存储，默认保留最近 20 条消息
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/chat-memory.html
 */
@Configuration
public class ChatMemoryConfig {

    /**
     * 创建 ChatMemory 实例
     *
     * MessageWindowChatMemory: 保留最近 N 条消息，自动清理旧消息
     * InMemoryChatMemoryRepository: 内存存储，重启后丢失
     */
    @Bean
    public MessageWindowChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)  // 保留最近10条消息
                .build();
    }

    /**
     * 创建支持 Chat Memory 的 ChatClient
     *
     * 使用 defaultAdvisors 添加内存 Advisor
     */
    @Bean
    public ChatClient chatClientWithMemory(ChatModel chatModel, MessageWindowChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}