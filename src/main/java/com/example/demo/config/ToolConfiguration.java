package com.example.demo.config;

import com.example.demo.tools.DateTimeTools;
import com.example.demo.tools.MathTools;
import com.example.demo.tools.WeatherTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tool Calling 配置类
 *
 * 将工具类注册到 Spring AI 上下文，使其可以被 ChatClient 调用
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/tools.html
 */
@Configuration
public class ToolConfiguration {

    /**
     * 注册日期时间工具
     *
     * 使用 @Tool 注解的方法会被自动识别并注册为可调用工具
     */
    @Bean
    public DateTimeTools dateTimeTools() {
        return new DateTimeTools();
    }

    /**
     * 注册天气查询工具
     */
    @Bean
    public WeatherTools weatherTools() {
        return new WeatherTools();
    }

    /**
     * 注册数学计算工具
     */
    @Bean
    public MathTools mathTools() {
        return new MathTools();
    }

    /**
     * 创建工具回调提供者
     *
     * 将所有工具类的方法包装为 ToolCallbackProvider
     * 这些工具会被自动添加到 ChatClient 的可用工具列表中
     */
    @Bean
    public ToolCallbackProvider tools(
            DateTimeTools dateTimeTools,
            WeatherTools weatherTools,
            MathTools mathTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTimeTools, weatherTools, mathTools)
                .build();
    }
}