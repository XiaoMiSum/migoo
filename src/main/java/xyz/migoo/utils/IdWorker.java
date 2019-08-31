package xyz.migoo.utils;

/**
 *
 * @author 192.168.1.131
 * @date 2014/12/26
 */
public class IdWorker {
    private final long workerIdBits = 5L;
    private final long dataCenterIdBits = 5L;

    private long workerId;
    private long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private IdWorker(long workerId, long dataCenterId) {
        long maxWorkerId = ~(-1L << workerIdBits);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        long maxDataCenterId = ~(-1L << dataCenterIdBits);
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public String nextStringId() {
        return String.valueOf(this.nextId());
    }

    private  synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        long sequenceBits = 12L;
        if (lastTimestamp == timestamp) {
            long sequenceMask = ~(-1L << sequenceBits);
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long dataCenterIdShift = sequenceBits + workerIdBits;
        long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
        return ((timestamp - 1288834974657L) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << sequenceBits) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    private static class Holder {

        private static IdWorker snowflakeIdWorker = new IdWorker(1, 0);
    }

    private IdWorker(){}


    public static Long getId(){
        return Holder.snowflakeIdWorker.nextId();
    }
}
