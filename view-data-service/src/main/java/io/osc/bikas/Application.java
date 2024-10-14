package io.osc.bikas;

import com.osc.bikas.avro.UserProductViewTopicValue;
import io.osc.bikas.kafka.KafkaConst;
import io.osc.bikas.kafka.producer.UserProductViewPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedList;

@SpringBootApplication
@RequiredArgsConstructor
@RestController
@RequestMapping
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public final UserProductViewPublisher publisher;
    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;

    @GetMapping("/populate")
    public void populate() throws Exception {
        for (int i = 0; i < 100; i++) {
            publisher.publish();
            Thread.sleep(1_00);
        }
        System.out.println(LocalDateTime.now());
    }

    @GetMapping("/view")
    public ResponseEntity<?> getViewHistory() {
        ReadOnlyKeyValueStore<String, LinkedList<UserProductViewTopicValue>> store =
                kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
                        KafkaConst.PRODUCT_VIEW_HISTORY,
                        QueryableStoreTypes.keyValueStore()
                );
        LinkedList<UserProductViewTopicValue> response = store.get("userId");
        return ResponseEntity.ok(response.toString());
    }

}