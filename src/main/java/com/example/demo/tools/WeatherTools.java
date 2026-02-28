package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Random;

/**
 * 天气查询工具
 *
 * 演示 Tool Calling - 获取天气信息
 * 注意：这是一个模拟实现，实际项目中可以调用真实天气API
 *
 */
public class WeatherTools {

    private static final String[] WEATHER_CONDITIONS = {"晴", "多云", "阴", "小雨", "中雨", "大雨"};
    private static final Random random = new Random();

    /**
     * 获取指定城市的当前天气
     *
     * @param city 城市名称，如 "北京"、"上海"
     * @return 天气信息字符串
     */
    @Tool(description = "获取指定城市的当前天气状况")
    public String getCurrentWeather(@ToolParam(description = "城市名称，如：北京、上海") String city) {
        // 模拟天气数据，实际项目中可调用真实API
        int temp = random.nextInt(15) + 10; // 10-25度
        String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];
        int humidity = random.nextInt(40) + 40; // 40-80%

        return String.format("%s 当前天气：%s，温度：%d°C，湿度：%d%%", city, condition, temp, humidity);
    }

    /**
     * 获取指定城市未来几天的天气预报
     *
     * @param city 城市名称
     * @param days 预报天数 (1-7)
     * @return 天气预报字符串
     */
    @Tool(description = "获取指定城市未来几天的天气预报")
    public String getWeatherForecast(
            @ToolParam(description = "城市名称") String city,
            @ToolParam(description = "预报天数，1-7天") int days) {
        if (days < 1 || days > 7) {
            return "预报天数请控制在1-7天范围内";
        }

        StringBuilder forecast = new StringBuilder(city).append(" 未来").append(days).append("天天气预报：\n");

        for (int i = 1; i <= days; i++) {
            int temp = random.nextInt(15) + 10;
            String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];
            forecast.append(String.format("第%d天: %s, %d°C\n", i, condition, temp));
        }

        return forecast.toString();
    }
}