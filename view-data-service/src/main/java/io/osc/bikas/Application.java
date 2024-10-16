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
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;

@SpringBootApplication
@RequiredArgsConstructor
//@RestController
//@RequestMapping
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public final UserProductViewPublisher publisher;
    private final KafkaStreamsInteractiveQueryService kafkaStreamsInteractiveQueryService;
//
//    @GetMapping("/populate")
//    public void populate() throws Exception {
//        String userId = "test-user";
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            publisher.publish(userId, random.nextInt(5)+"");
//            Thread.sleep(1_000);
//        }
//    }
//
//    @GetMapping("/view")
//    public ResponseEntity<?> getViewHistory() {
//        ReadOnlyKeyValueStore<String, LinkedList<UserProductViewTopicValue>> store =
//                kafkaStreamsInteractiveQueryService.retrieveQueryableStore(
//                        KafkaConst.PRODUCT_VIEW_HISTORY,
//                        QueryableStoreTypes.keyValueStore()
//                );
//        LinkedList<UserProductViewTopicValue> response = store.get("userId");
//        return ResponseEntity.ok(response.toString());
//    }

}