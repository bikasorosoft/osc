package io.osc.bikas.product.data.kafka.config;

import com.osc.bikas.avro.ProductAvro;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConsumerConfig {

   @Bean
   public ConcurrentKafkaListenerContainerFactory<String, ProductAvro> kafkaListenerContainerFactory(ConsumerFactory<String, ProductAvro> consumerFactory) {
       ConcurrentKafkaListenerContainerFactory<String, ProductAvro> factory =
               new ConcurrentKafkaListenerContainerFactory<>();
       factory.setConsumerFactory(consumerFactory);
       factory.setBatchListener(true);
       factory.getContainerProperties().setPollTimeout(300000);
       factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
       return factory;
   }

}
