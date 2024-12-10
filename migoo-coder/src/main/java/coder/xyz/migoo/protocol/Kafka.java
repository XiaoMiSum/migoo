package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

public class Kafka extends El {

    public Kafka bootstrapServers(String bootstrapServers) {
        p("bootstrapServers", bootstrapServers);
        return this;
    }

    public Kafka key(String key) {
        p("key", key);
        return this;
    }

    public Kafka keySerializer(String keySerializer) {
        p("key.serializer", keySerializer);
        return this;
    }

    public Kafka valueSerializer(String valueSerializer) {
        p("value.serializer", valueSerializer);
        return this;
    }

    public Kafka acks(int acks) {
        p("acks", acks);
        return this;
    }

    public Kafka retries(int retries) {
        p("retries", retries);
        return this;
    }

    public Kafka lingerMs(int lingerMs) {
        p("linger.ms", lingerMs);
        return this;
    }

    public Kafka topic(String topic) {
        p("topic", topic);
        return this;
    }

    public Kafka message(Object message) {
        p("message", message);
        return this;
    }

}
