package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    // 默认 ChatClient (通义千问)
    @Bean
    public ChatClient defaultChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    // 创意写作 ChatClient (更高 temperature)
    @Bean
    public ChatClient creativeChatClient(OpenAiChatModel chatModel) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.9)
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }

    // 精确回答 ChatClient (更低 temperature)
    @Bean
    public ChatClient preciseChatClient(OpenAiChatModel chatModel) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.2)
                .build();

        return ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }
}