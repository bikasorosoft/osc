package io.osc.bikas.product.data.kafka.utils;


import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeSet;

public class TreeSetDeserializer<T> implements Deserializer<TreeSet<T>> {

    private final Deserializer<T> valueDeserializer;

    public TreeSetDeserializer(Deserializer<T> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    @Override
    public TreeSet<T> deserialize(String s, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final TreeSet<T> TreeSet = new TreeSet<>();
        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            final int records = dataInputStream.readInt();
            for (int i = 0; i < records; i++) {
                final byte[] valueBytes = new byte[dataInputStream.readInt()];
                if (dataInputStream.read(valueBytes) != valueBytes.length) {
                    throw new BufferUnderflowException();
                };
                TreeSet.add(valueDeserializer.deserialize(s, valueBytes));
            }
        } catch (final IOException e) {
            throw new RuntimeException("Unable to deserialize TreeSet", e);
        }
        return TreeSet;
    }

    @Override
    public void close() {
        //do nothing
    }
}
