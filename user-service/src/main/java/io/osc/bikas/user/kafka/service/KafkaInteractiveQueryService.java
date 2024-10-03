package io.osc.bikas.user.kafka.service;

import com.osc.bikas.avro.OtpDetails;
import com.osc.bikas.avro.UserRegistrationDetail;
import io.osc.bikas.user.kafka.config.KafkaConstants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    private ReadOnlyKeyValueStore<String, OtpDetails> otpReadOnlyKeyValueStore;
    private ReadOnlyKeyValueStore<String, UserRegistrationDetail> registrationUserReadOnlyKeyValueStore;

    public ReadOnlyKeyValueStore<String, OtpDetails> getOtpReadOnlyKeyValueStore() {
        if(otpReadOnlyKeyValueStore == null) {
            this.otpReadOnlyKeyValueStore =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConstants.OTP_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        }
        return otpReadOnlyKeyValueStore;
    }

    public ReadOnlyKeyValueStore<String, UserRegistrationDetail> getRegistrationUserReadOnlyKeyValueStore() {

        if(registrationUserReadOnlyKeyValueStore == null) {
            this.registrationUserReadOnlyKeyValueStore =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConstants.REGISTRATION_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        }

        return registrationUserReadOnlyKeyValueStore;
    }
}
