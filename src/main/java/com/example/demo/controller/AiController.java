package com.example.demo.controller;

import com.example.demo.pojo.ActorFilms;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 对话控制器
 *
 * 展示 Spring AI 的各种使用方式：
 * 1. 基础对话
 * 2. 结构化输出 (Structured Output)
 * 3. 流式输出 (Streaming)
 * 4. 不同配置的 ChatClient
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/chatclient.html
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient defaultChatClient;
    private final ChatClient creativeChatClient;
    private final ChatClient preciseChatClient;
    private final ChatClient pirateChatClient;

    public AiController(
            @Qualifier("defaultChatClient") ChatClient defaultChatClient,
            @Qualifier("creativeChatClient") ChatClient creativeChatClient,
            @Qualifier("preciseChatClient") ChatClient preciseChatClient,
            @Qualifier("pirateChatClient") ChatClient pirateChatClient) {
        this.defaultChatClient = defaultChatClient;
        this.creativeChatClient = creativeChatClient;
        this.preciseChatClient = preciseChatClient;
        this.pirateChatClient = pirateChatClient;
    }

    /**
     * 基础对话接口
     *
     * 使用 SimpleLoggerAdvisor 打印请求/响应日志，用于调试
     *
     * @param userInput 用户输入
     * @return AI 响应内容
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String userInput) {
        return defaultChatClient.prompt()
                // 开启日志 advisor，记录请求和响应
                .advisors(new SimpleLoggerAdvisor())
                .user(userInput)
                .call()
                .content();
    }

    /**
     * 结构化输出 - 单个实体
     *
     * Spring AI 的 .entity() 方法可以将 AI 响应直接映射为 Java 对象
     * 使用 enable_native_structured_output advisor 启用原生结构化输出
     *
     *
     * @param userInput 用户输入（可选，会被忽略，使用固定的演示 prompt）
     * @return ActorFilms 实体对象
     */
    @GetMapping("/chat/entity")
    public ActorFilms chatEntity(@RequestParam(required = false) String userInput) {
        return defaultChatClient.prompt()
                // 启用原生结构化输出，让模型直接输出 JSON
                .advisors(a -> a.param("enable_native_structured_output", true))
                // 在提示词中明确要求 JSON 格式输出
                .user("Generate the filmography for a random actor. **Output the result strictly in JSON format.**")
                .call()
                .entity(ActorFilms.class);
    }

    /**
     * 结构化输出 - 实体列表
     *
     * 使用 ParameterizedTypeReference 获取泛型类型列表
     *
     * @return ActorFilms 列表
     */
    @GetMapping("/chat/entityList")
    public List<ActorFilms> entityList() {
        return defaultChatClient.prompt()
                .user("Generate the filmography of 5 movies for Tom Hanks and Bill Murray.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });
    }

    /**
     * 流式输出
     *
     * 使用 .stream() 方法获取 Flux<String> 流式响应
     * BeanOutputConverter 用于将流式输出的 JSON 片段转换为对象
     *
     *
     * @return ActorFilms 列表
     */
    @GetMapping("/chat/films")
    public List<ActorFilms> chatFilms() {
        // 定义输出格式转换器，指定目标类型
        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorFilms>>() {
        });

        // 获取流式响应
        Flux<String> flux = defaultChatClient.prompt()
                .user(u -> u.text("""
                        Generate the filmography of 5 movies for Tom Hanks and Bill Murray.
                        {format}
                        """).param("format", converter.getFormat()))
                .stream()
                .content();

        // 收集流式内容并转换为对象
        String content = flux.collectList().block().stream().collect(Collectors.joining());
        return converter.convert(content);
    }

    /**
     * 角色扮演对话
     *
     * 使用配置了 Pirate system prompt 的 ChatClient
     *
     * @param message 用户消息
     * @return 包含 AI 响应的 Map
     */
    @GetMapping("/chat/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("completion", pirateChatClient.prompt().user(message).call().content());
    }

    /**
     * 创意对话
     *
     * 使用 temperature=0.9 的 ChatClient，产生更有创意的回答
     *
     * @param userInput 用户输入
     * @return AI 创意响应
     */
    @GetMapping("/chat/creative")
    public String creativeChat(@RequestParam String userInput) {
        return creativeChatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    /**
     * 精确对话
     *
     * 使用 temperature=0.2 的 ChatClient，产生更准确、可预测的回答
     * 适用于代码生成、数学计算等需要准确性的场景
     *
     * @param userInput 用户输入
     * @return AI 精确响应
     */
    @GetMapping("/chat/precise")
    public String preciseChat(@RequestParam String userInput) {
        return preciseChatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }
}