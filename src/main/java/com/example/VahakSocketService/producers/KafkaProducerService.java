package com.example.VahakSocketService.producers;

import com.example.VahakSocketService.dto.UpdateBookingDto;
import com.example.VahakSocketService.dto.UpdateBookingResponseDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, UpdateBookingDto> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UpdateBookingDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMessage(String topic, UpdateBookingDto message){
        kafkaTemplate.send(topic, message);
//        kafkaTemplate.send("sample-topic-1", 0, null, "P0");
//        kafkaTemplate.send("sample-topic-1", 1, null, "P1");
    }

}
