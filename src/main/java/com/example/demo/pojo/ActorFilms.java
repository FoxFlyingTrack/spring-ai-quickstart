package com.example.demo.pojo;

import java.util.List;

/**
 * 演员电影作品 POJO
 *
 * 用于演示 Spring AI 的结构化输出功能
 * AI 响应的 JSON 会被自动映射为这个 Java Record
 *
 * 官方文档: https://docs.spring.io/spring-ai/reference/api/structured-output.html
 *
 * @param actor 演员姓名
 * @param movies 电影作品列表
 */
public record ActorFilms(String actor, List<String> movies) {
}