package com.sss.service;

/**
 * ExpOutput class
 *
 * @author Sss
 * @date 2018/12/26
 */
public class ExpOutput {
    /**
     * 计算结果
     */
    private long value;

    /**
     * 计算耗时
     */
    private long costInNanos;

    public ExpOutput() {
    }

    public ExpOutput(long value, long costInNanos) {
        this.value = value;
        this.costInNanos = costInNanos;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getCostInNanos() {
        return costInNanos;
    }

    public void setCostInNanos(long costInNanos) {
        this.costInNanos = costInNanos;
    }
}
