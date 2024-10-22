package io.osc.bikas.session.data.kafka.service;

import com.osc.bikas.avro.SessionTopicKey;
import io.osc.bikas.session.data.kafka.config.KafkaConstants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionDataInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService interactiveQueryService;


    public Optional<String> get(String userId, String deviceType) {
        ReadOnlyKeyValueStore<SessionTopicKey, CharSequence> store =
                interactiveQueryService.retrieveQueryableStore(KafkaConstants.SESSION_STORE, QueryableStoreTypes.keyValueStore());

        var key = SessionTopicKey.newBuilder().setUserId(userId).setDevice(deviceType).build();

        return Optional.of(String.valueOf(store.get(key)));

    }

}
