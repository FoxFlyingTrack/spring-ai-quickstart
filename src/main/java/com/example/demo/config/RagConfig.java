package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RAG 配置类
 *
 * 演示检索增强生成 (Retrieval Augmented Generation)
 * 使用内存向量存储，无需外部数据库
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
 */
@Configuration
public class RagConfig {

    /**
     * 创建 Embedding 模型
     *
     * 用于将文本转换为向量（嵌入）
     * Spring AI 会自动配置 OpenAiEmbeddingModel
     */
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        // 使用内存向量存储（重启后丢失）
        return SimpleVectorStore.builder(embeddingModel)
                .build();
    }

    /**
     * 创建支持 RAG 的 ChatClient
     *
     * QuestionAnswerAdvisor 会自动：
     * 1. 将用户问题转换为向量
     * 2. 从向量数据库检索相似文档
     * 3. 将检索到的文档作为上下文提供给 AI
     */
    @Bean
    public ChatClient ragChatClient(ChatModel chatModel, VectorStore vectorStore) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .topK(3)  // 检索最相似的3个文档
                                        .build())
                                .build()
                )
                .build();
    }

    /**
     * 初始化示例文档
     *
     * 实际应用中可以从文件、数据库等加载文档
     */
    @Bean
    public List<Document> sampleDocuments() {
        return List.of(
                new Document("Spring AI 是 Spring 生态的 AI 框架，支持多种 AI 模型提供商。"),
                new Document("Spring AI 的核心组件包括：ChatClient、Memory、Tool Calling、RAG。"),
                new Document("ChatClient 是 Spring AI 的主要 API，用于与 AI 模型交互。"),
                new Document("RAG (检索增强生成) 可以让 AI 基于私有文档进行问答。"),
                new Document("Tool Calling 让 AI 可以调用外部函数和 API。"),
                new Document("Chat Memory 支持多轮对话，让 AI 记住之前的对话内容。"),
                new Document("Embedding 用于将文本转换为向量，用于相似度搜索。"),
                new Document("Vector Store 向量数据库用于存储和检索文档向量。")
        );
    }
}