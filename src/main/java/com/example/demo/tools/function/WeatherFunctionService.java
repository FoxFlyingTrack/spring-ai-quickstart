package com.example.demo.tools.function;

import java.util.Random;
import java.util.function.Function;

/**
 * 天气查询服务 - FunctionToolCallback 方式
 *
 * 实现 Function 接口，作为函数式工具被 AI 调用
 *
 */
public class WeatherFunctionService implements Function<WeatherFunctionService.WeatherRequest, WeatherFunctionService.WeatherResponse> {

    private static final String[] WEATHER_CONDITIONS = {"晴", "多云", "阴", "小雨", "中雨", "大雨"};
    private static final Random random = new Random();

    /**
     * 请求参数 record
     */
    public record WeatherRequest(String city) {}

    /**
     * 响应参数 record
     */
    public record WeatherResponse(String city, String condition, int temperature, int humidity) {}

    /**
     * 实现 apply 方法，AI 会调用此方法
     */
    @Override
    public WeatherResponse apply(WeatherRequest request) {
        int temp = random.nextInt(20) + 5;   // 摄氏度 5-25
        String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];
        int humidity = random.nextInt(50) + 30;

        return new WeatherResponse(request.city(), condition, temp, humidity);
    }
}