package io.osc.bikas.session.data.kafka.topology;

import com.osc.bikas.avro.SessionTopicKey;
import io.osc.bikas.session.data.kafka.config.KafkaConstants;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionDataStoreTopology {
    @Bean
    public KTable<SessionTopicKey, CharSequence> registrationKTable(StreamsBuilder builder) {
        return builder.table(KafkaConstants.SESSION_TOPIC,
                Materialized.<SessionTopicKey, CharSequence, KeyValueStore<Bytes, byte[]>>as(KafkaConstants.SESSION_STORE));
    }
}
