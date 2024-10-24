package io.osc.bikas.product.data.kafka.utils;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map;

public class TreeSetSerializer<T> implements Serializer<TreeSet<T>> {

    private final Serializer<T> valueSerializer;

    public TreeSetSerializer(Serializer<T> valuerSerializer) {
        this.valueSerializer = valuerSerializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    @Override
    public byte[] serialize(String topic, TreeSet<T> ts) {
        final int size = ts.size();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(baos);
        final Iterator<T> iterator = ts.iterator();
        try {
            out.writeInt(size);
            while (iterator.hasNext()) {
                final byte[] bytes = valueSerializer.serialize(topic, iterator.next());
                out.writeInt(bytes.length);
                out.write(bytes);
            }
            out.close();
        } catch (final IOException e) {
            throw new RuntimeException("unable to serialize PriorityQueue", e);
        }
        return baos.toByteArray();
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
