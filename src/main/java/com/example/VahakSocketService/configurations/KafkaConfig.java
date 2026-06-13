package com.example.VahakSocketService.configurations;

import com.example.VahakSocketService.dto.LateAcceptingDriverDto;
import com.example.VahakSocketService.dto.UpdateBookingDto;
import com.example.VahakSocketService.dto.WinnerDriverDto;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic sampleTopic(){  // works without this also
        return new NewTopic("sample-topic-1", 2, (short)1);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );
        return new KafkaAdmin(configs);
    }

    @Bean
    public ProducerFactory<String, UpdateBookingDto> producerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UpdateBookingDto> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ConsumerFactory<String, WinnerDriverDto> consumerFactoryForWinners(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");

        JacksonJsonDeserializer<WinnerDriverDto> deserializer = new JacksonJsonDeserializer<>(WinnerDriverDto.class);
        deserializer.setUseTypeHeaders(false);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WinnerDriverDto> kafkaListenerContainerFactory1(){
        ConcurrentKafkaListenerContainerFactory<String, WinnerDriverDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForWinners());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, LateAcceptingDriverDto> consumerFactoryForLateDrivers(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");

        JacksonJsonDeserializer<LateAcceptingDriverDto> deserializer = new JacksonJsonDeserializer<>(LateAcceptingDriverDto.class);
        deserializer.setUseTypeHeaders(false);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LateAcceptingDriverDto> kafkaListenerContainerFactory2(){
        ConcurrentKafkaListenerContainerFactory<String, LateAcceptingDriverDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForLateDrivers());
        return factory;
    }
}
