package io.osc.bikas.product.data.kafka.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.Map;

public class TreeSetSerde<T> implements Serde<TreeSet<T>> {

    private final Serde<TreeSet<T>> inner;

    public TreeSetSerde( final Serde<T> avroSerde, final Comparator<T> comparator) {
        inner = Serdes.serdeFrom(new TreeSetSerializer<>(avroSerde.serializer()),
                new TreeSetDeserializer<>(comparator, avroSerde.deserializer()));
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
    public Serializer<TreeSet<T>> serializer() {
        return inner.serializer();
    }

    @Override
    public Deserializer<TreeSet<T>> deserializer() {
        return inner.deserializer();
    }
}
