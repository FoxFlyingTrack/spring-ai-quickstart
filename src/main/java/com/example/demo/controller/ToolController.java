package com.example.demo.controller;

import com.example.demo.tools.DateTimeTools;
import com.example.demo.tools.function.WeatherFunctionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tool Calling 控制器
 *
 * 展示两种工具定义方式：
 * 1. @Tool 注解方式 (DateTimeTools)
 * 2. Function 接口方式 (WeatherFunctionService)
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/tools.html
 */
@RestController
@RequestMapping("/tool")
public class ToolController {

    private final ChatClient defaultChatClient;

    public ToolController(@Qualifier("defaultChatClient") ChatClient defaultChatClient) {
        this.defaultChatClient = defaultChatClient;
    }

    /**
     * @Tool 注解方式 - 日期时间工具
     *
     * 使用 @Tool 注解声明工具方法
     */
    @GetMapping("/datetime")
    public String datetime(@RequestParam String question) {
        return defaultChatClient.prompt()
                .user(question)
                .tools(new DateTimeTools())
                .call()
                .content();
    }

    /**
     * Function 接口方式 - 天气查询
     *
     * 使用实现了 Function 接口的服务类
     * Spring AI 会自动识别 Function 接口并调用 apply 方法
     */
    @GetMapping("/weather")
    public String weather(@RequestParam String question) {
        return defaultChatClient.prompt()
                .user(question)
                .tools(new WeatherFunctionService())
                .call()
                .content();
    }

    /**
     * 综合示例 - 同时使用两种方式
     */
    @GetMapping("/all")
    public String all(@RequestParam String question) {
        return defaultChatClient.prompt()
                .user(question)
                .tools(new DateTimeTools(), new WeatherFunctionService())
                .call()
                .content();
    }

    /**
     * 使用 toolCallbacks 方法传入工具回调
     */
    @GetMapping("/callback")
    public String callback(@RequestParam String question) {
        return defaultChatClient.prompt()
                .user(question)
                .tools(new DateTimeTools())
                .call()
                .content();
    }
}