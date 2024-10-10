package io.osc.bikas.kafka.utils;


import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class LinkedListDeserializer<T> implements Deserializer<LinkedList<T>> {

    private final Deserializer<T> valueDeserializer;

    public LinkedListDeserializer(Deserializer<T> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    @Override
    public LinkedList<T> deserialize(String s, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final LinkedList<T> linkedList = new LinkedList<>();
        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            final int records = dataInputStream.readInt();
            for (int i = 0; i < records; i++) {
                final byte[] valueBytes = new byte[dataInputStream.readInt()];
                if (dataInputStream.read(valueBytes) != valueBytes.length) {
                    throw new BufferUnderflowException();
                };
                linkedList.add(valueDeserializer.deserialize(s, valueBytes));
            }
        } catch (final IOException e) {
            throw new RuntimeException("Unable to deserialize PriorityQueue", e);
        }
        return linkedList;
    }

    @Override
    public void close() {
        //do nothing
    }
}
