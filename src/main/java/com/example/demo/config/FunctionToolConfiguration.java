package com.example.demo.config;

import com.example.demo.tools.function.WeatherFunctionService;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FunctionToolCallback 配置类
 *
 * 展示如何使用 Function 接口方式定义工具
 * 与 @Tool 注解方式不同，这种方式需要实现 Function 接口
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/tools.html
 *
 * 注意: Spring AI 中两种方式：
 * 1. @Tool 注解方式 - 使用 MethodToolCallbackProvider (当前项目使用)
 * 2. Function 接口方式 - 实现 Function 接口，使用函数式编程
 */
@Configuration
public class FunctionToolConfiguration {

    /**
     * 注册天气查询服务 (Function 方式)
     *
     * 这个服务实现了 Function 接口
     * AI 模型会自动识别并调用 apply 方法
     */
    @Bean
    public WeatherFunctionService weatherFunctionService() {
        return new WeatherFunctionService();
    }
}