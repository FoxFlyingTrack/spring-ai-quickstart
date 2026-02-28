package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具
 *
 * 演示 Tool Calling - 获取当前日期时间信息
 *
 */
public class DateTimeTools {

    /**
     * 获取当前日期和时间
     *
     * @return 当前日期时间字符串
     */
    @Tool(description = "获取当前日期和时间，返回格式为 yyyy-MM-dd HH:mm:ss")
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = LocaleContextHolder.getTimeZone().toZoneId();
        return now.atZone(zone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期字符串
     */
    @Tool(description = "获取当前日期，返回格式为 yyyy-MM-dd")
    public String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = LocaleContextHolder.getTimeZone().toZoneId();
        return now.atZone(zone).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间字符串
     */
    @Tool(description = "获取当前时间，返回格式为 HH:mm:ss")
    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = LocaleContextHolder.getTimeZone().toZoneId();
        return now.atZone(zone).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param startDate 起始日期 (yyyy-MM-dd)
     * @param endDate 结束日期 (yyyy-MM-dd)
     * @return 天数差
     */
    @Tool(description = "计算两个日期之间的天数差")
    public long daysBetween(
            @ToolParam(description = "起始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T00:00:00");
        return Duration.between(start, end).toDays();
    }
}