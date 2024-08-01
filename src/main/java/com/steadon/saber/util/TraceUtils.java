package com.steadon.saber.util;

public class TraceUtils {
    // 序列号，每当同一毫秒生成多个ID时递增
    private long sequence = 0L;

    // 上次生成ID的时间戳，用于检测时钟回拨
    private long lastTimestamp = -1L;

    /**
     * 获取基于雪花算法生成的唯一ID
     *
     * @return 唯一的traceId
     */
    public static long getTraceId() {
        return new TraceUtils().nextId();
    }

    /**
     * 生成下一个唯一的ID
     *
     * @return 生成的唯一ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 检测时钟是否回拨，如果是，则抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果当前时间戳与上一次生成ID的时间戳相同，则递增序列号
        // 序列号占用的位数
        long sequenceBits = 12L;
        if (lastTimestamp == timestamp) {
            // 序列号的最大值，用于在同一毫秒内循环序列号
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            // 如果序列号溢出，则等待下一个毫秒时间戳
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，重置序列号
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 组合时间戳和序列号生成唯一ID
        // 基准时间戳（2020-01-01）
        long twepoch = 1577836800000L;
        return ((timestamp - twepoch) << sequenceBits) | sequence;
    }

    /**
     * 等待直到获取下一个毫秒时间戳
     *
     * @param lastTimestamp 上一次的时间戳
     * @return 下一个毫秒的时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前的时间戳
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
