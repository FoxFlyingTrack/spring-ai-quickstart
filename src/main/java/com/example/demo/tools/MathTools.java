package com.example.demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数学计算工具
 * <p>
 * 演示 Tool Calling - 执行数学计算
 */
public class MathTools {

    /**
     * 计算两个数的和
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之和
     */
    @Tool(description = "计算两个数的加法", returnDirect = true)
    public double add(
            @ToolParam(description = "第一个数") double a,
            @ToolParam(description = "第二个数") double b) {
        return a + b;
    }

    /**
     * 计算两个数的差
     *
     * @param a 被减数
     * @param b 减数
     * @return 差
     */
    @Tool(description = "计算两个数的减法")
    public double subtract(
            @ToolParam(description = "被减数") double a,
            @ToolParam(description = "减数") double b) {
        return a - b;
    }

    /**
     * 计算两个数的乘积
     *
     * @param a 第一个数
     * @param b 第二个数
     * @return 乘积
     */
    @Tool(description = "计算两个数的乘法")
    public double multiply(
            @ToolParam(description = "第一个数") double a,
            @ToolParam(description = "第二个数") double b) {
        return a * b;
    }

    /**
     * 计算两个数的商
     *
     * @param a 被除数
     * @param b 除数
     * @return 商
     */
    @Tool(description = "计算两个数的除法")
    public double divide(
            @ToolParam(description = "被除数") double a,
            @ToolParam(description = "除数，不能为0") double b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a / b;
    }

    /**
     * 计算幂运算
     *
     * @param base     底数
     * @param exponent 指数
     * @return 幂运算结果
     */
    @Tool(description = "计算幂运算 (base^exponent)")
    public double power(
            @ToolParam(description = "底数") double base,
            @ToolParam(description = "指数") double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * 计算平方根
     *
     * @param number 被开方数
     * @return 平方根
     */
    @Tool(description = "计算平方根")
    public double sqrt(@ToolParam(description = "被开方数") double number) {
        if (number < 0) {
            throw new IllegalArgumentException("不能对负数进行平方根运算");
        }
        return Math.sqrt(number);
    }

    /**
     * 计算百分比
     *
     * @param value      数值
     * @param percentage 百分比 (如 80 表示 80%)
     * @return 百分比结果
     */
    @Tool(description = "计算数值的百分比")
    public double percentage(
            @ToolParam(description = "数值") double value,
            @ToolParam(description = "百分比，如80表示80%") double percentage) {
        return value * percentage / 100;
    }

    /**
     * 四舍五入计算
     *
     * @param number   要四舍五入的数
     * @param decimals 保留小数位数
     * @return 四舍五入后的结果
     */
    @Tool(description = "对数值进行四舍五入")
    public double round(
            @ToolParam(description = "要四舍五入的数") double number,
            @ToolParam(description = "保留小数位数") int decimals) {
        return BigDecimal.valueOf(number)
                .setScale(decimals, RoundingMode.HALF_UP)
                .doubleValue();
    }
}