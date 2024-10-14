package io.osc.bikas.cart.data.kafka.service;

import com.osc.bikas.avro.CartItemList;
import com.osc.bikas.avro.OtpDetails;
import com.osc.bikas.avro.UserRegistrationDetail;
import io.osc.bikas.cart.data.kafka.KafkaConst;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaInteractiveQueryService {

    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    private ReadOnlyKeyValueStore<String, CartItemList> otpReadOnlyKeyValueStore;

    public ReadOnlyKeyValueStore<String, CartItemList> getCartItemReadOnlyKeyValueStore() {

        if(otpReadOnlyKeyValueStore == null) {
            this.otpReadOnlyKeyValueStore =
                    this.kafkaStreamsInteractiveQueryService
                            .retrieveQueryableStore(
                                    KafkaConst.CART_STORE,
                                    QueryableStoreTypes.keyValueStore()
                            );
        }
        return otpReadOnlyKeyValueStore;
    }

}
