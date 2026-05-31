package com.example.VahakSocketService.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "sample-topic", groupId = "sample-group")
    public void listen(String message){
        System.out.println("Consumer is listening the message: "+message);
    }
}
