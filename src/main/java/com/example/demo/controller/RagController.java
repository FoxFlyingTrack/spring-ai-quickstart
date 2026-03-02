package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * RAG 控制器
 *
 * 演示检索增强生成功能：基于文档知识库进行问答
 *
 * 核心流程：
 * 1. 将文档加载到向量数据库
 * 2. 用户提问时，将问题转换为向量
 * 3. 检索相似文档
 * 4. AI 基于检索到的文档生成回答
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
 */
@RestController
@RequestMapping("/ai/rag")
public class RagController {

    private final ChatClient ragChatClient;
    private final VectorStore vectorStore;
    private final List<Document> sampleDocuments;

    public RagController(
            ChatClient ragChatClient,
            VectorStore vectorStore,
            List<Document> sampleDocuments) {
        this.ragChatClient = ragChatClient;
        this.vectorStore = vectorStore;
        this.sampleDocuments = sampleDocuments;
    }

    /**
     * 初始化向量数据库
     *
     * 将示例文档加载到向量存储中
     * 实际应用中可以从文件、数据库等加载
     */
    @PostMapping("/init")
    public Map<String, Object> initDocuments() {
        // 将文档添加到向量数据库
        vectorStore.add(sampleDocuments);

        return Map.of(
                "status", "success",
                "message", "已加载 " + sampleDocuments.size() + " 个文档到向量数据库",
                "documents", sampleDocuments.stream()
                        .map(doc -> doc.getText())
                        .toList()
        );
    }

    /**
     * RAG 问答
     *
     * 基于已加载的文档进行问答
     * AI 会从向量数据库检索相关文档，然后生成回答
     *
     * @param question 用户问题
     * @return AI 回答
     */
    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam String question) {
        String response = ragChatClient.prompt()
                .user(question)
                .call()
                .content();

        return Map.of(
                "question", question,
                "answer", response
        );
    }

    /**
     * 添加文档到知识库
     *
     * @return 添加结果
     */
    @PostMapping("/add")
    public Map<String, Object> addDocument(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        Document document = new Document(content);
        vectorStore.add(List.of(document));

        return Map.of(
                "status", "success",
                "message", "文档已添加",
                "content", content
        );
    }

    /**
     * 查询向量数据库中的文档数量
     */
    @GetMapping("/count")
    public Map<String, Object> countDocuments() {
        return Map.of(
                "count", sampleDocuments.size()
        );
    }
}