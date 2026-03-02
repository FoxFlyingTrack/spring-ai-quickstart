package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Chat Memory 控制器
 *
 * 演示多轮对话功能：AI 会记住之前的对话内容
 *
 * 关键概念：
 * - conversationId: 会话ID，用于区分不同对话
 * - Advisor: 自动将历史消息添加到请求中
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/chat-memory.html
 */
@RestController
@RequestMapping("/ai/memory")
public class MemoryController {

    private final ChatClient chatClientWithMemory;

    public MemoryController(@Qualifier("chatClientWithMemory") ChatClient chatClientWithMemory) {
        this.chatClientWithMemory = chatClientWithMemory;
    }

    /**
     * 多轮对话 - 通过 URL 参数指定会话ID
     *
     * 同一个 conversationId 会共享对话历史
     * 不同 conversationId 是独立的对话
     *
     * @param message 用户消息
     * @param conversationId 会话ID（必填），用于标识不同对话
     * @return AI 响应
     */
    @GetMapping("/chat")
    public Map<String, String> chat(
            @RequestParam String message,
            @RequestParam String conversationId) {

        String response = chatClientWithMemory.prompt()
                .user(message)
                // 指定会话ID，AI 会自动加载该会话的历史消息
                .advisors(a -> a.param("conversationId", conversationId))
                .call()
                .content();

        return Map.of(
                "conversationId", conversationId,
                "message", message,
                "response", response
        );
    }

    /**
     * 多轮对话 - 通过 Header 指定会话ID
     *
     * @param message 用户消息
     * @param xConversationId 会话ID (Header)
     * @return AI 响应
     */
    @GetMapping("/chat/header")
    public Map<String, String> chatWithHeader(
            @RequestParam String message,
            @RequestHeader("X-Conversation-Id") String xConversationId) {

        String response = chatClientWithMemory.prompt()
                .user(message)
                .advisors(a -> a.param("conversationId", xConversationId))
                .call()
                .content();

        return Map.of(
                "conversationId", xConversationId,
                "message", message,
                "response", response
        );
    }
}