package io.osc.bikas.kafka.utils;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.LinkedList;
import java.util.Map;

public class LinkedListSerde<T> implements Serde<LinkedList<T>> {

    private final Serde<LinkedList<T>> inner;

    public LinkedListSerde( final Serde<T> avroSerde) {
        inner = Serdes.serdeFrom(new LinkedListSerializer<>(avroSerde.serializer()),
                new LinkedListDeserializer<>(avroSerde.deserializer()));
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        inner.serializer().configure(configs, isKey);
        inner.deserializer().configure(configs, isKey);
    }

    @Override
    public void close() {
        inner.serializer().close();
        inner.deserializer().close();
    }

    @Override
    public Serializer<LinkedList<T>> serializer() {
        return inner.serializer();
    }

    @Override
    public Deserializer<LinkedList<T>> deserializer() {
        return inner.deserializer();
    }
}
