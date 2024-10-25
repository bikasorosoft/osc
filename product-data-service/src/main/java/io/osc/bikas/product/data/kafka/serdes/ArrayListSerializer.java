package io.osc.bikas.product.data.kafka.serdes;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class ArrayListSerializer<T> implements Serializer<ArrayList<T>> {

    private final Serializer<T> valueSerializer;

    public ArrayListSerializer(Serializer<T> valuerSerializer) {
        this.valueSerializer = valuerSerializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nothing
    }

    @Override
    public byte[] serialize(String topic, ArrayList<T> ts) {
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
