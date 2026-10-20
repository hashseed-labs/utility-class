package com.utility;

import java.util.UUID;

/**
 * ID 生成工具类（JDK 8）
 * <p>
 * 提供 UUID、简易雪花算法 ID。
 */
public final class IdUtil {

    private static final Snowflake DEFAULT_SNOWFLAKE = new Snowflake(1L, 1L);

    private IdUtil() {
    }

    /**
     * 标准 UUID（含横线）
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 无横线 UUID
     */
    public static String simpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 简易雪花算法 long ID（单机默认 workerId=1, datacenterId=1）
     */
    public static long snowflakeId() {
        return DEFAULT_SNOWFLAKE.nextId();
    }

    /**
     * 简易雪花算法字符串 ID
     */
    public static String snowflakeIdStr() {
        return String.valueOf(snowflakeId());
    }

    /**
     * 自定义机器位的雪花生成器
     */
    public static Snowflake createSnowflake(long workerId, long datacenterId) {
        return new Snowflake(workerId, datacenterId);
    }

    /**
     * 简易雪花算法实现（64 位）
     */
    public static final class Snowflake {

        private static final long START_TIMESTAMP = 1704067200000L; // 2024-01-01

        private static final long WORKER_ID_BITS = 5L;
        private static final long DATACENTER_ID_BITS = 5L;
        private static final long SEQUENCE_BITS = 12L;

        private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
        private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

        private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
        private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
        private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
        private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

        private final long workerId;
        private final long datacenterId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        public Snowflake(long workerId, long datacenterId) {
            if (workerId < 0 || workerId > MAX_WORKER_ID) {
                throw new IllegalArgumentException("workerId 超出范围: 0~" + MAX_WORKER_ID);
            }
            if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
                throw new IllegalArgumentException("datacenterId 超出范围: 0~" + MAX_DATACENTER_ID);
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        public synchronized long nextId() {
            long timestamp = currentTime();
            if (timestamp < lastTimestamp) {
                throw new IllegalArgumentException("系统时钟回拨，拒绝生成 ID");
            }
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0L) {
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                    | (datacenterId << DATACENTER_ID_SHIFT)
                    | (workerId << WORKER_ID_SHIFT)
                    | sequence;
        }

        private long waitNextMillis(long last) {
            long timestamp = currentTime();
            while (timestamp <= last) {
                timestamp = currentTime();
            }
            return timestamp;
        }

        private long currentTime() {
            return System.currentTimeMillis();
        }
    }
}
