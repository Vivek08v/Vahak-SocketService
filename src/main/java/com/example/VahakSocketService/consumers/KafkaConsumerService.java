package com.example.VahakSocketService.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "sample-topic", groupId = "sample-group-1")
    public void listen(String message){
        System.out.println("Consumer 'sample-group-1' is listening the message: "+message);
    }

//    @KafkaListener(topics = "sample-topic", groupId = "sample-group-2", concurrency = "2")
//    public void listen2(String message){
//        System.out.println("Consumer 'sample-group-2' is listening the message: "+message);
//    }

    @KafkaListener(
            topics = "sample-topic-1",
            groupId = "sample-group-2",
            concurrency = "2"
    )
    public void listen(
            String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        System.out.println(
                Thread.currentThread().getName()
                        + " Partition=" + partition
                        + " Message=" + message
        );
    }
}
