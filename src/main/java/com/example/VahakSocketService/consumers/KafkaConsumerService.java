package com.example.VahakSocketService.consumers;

import com.example.VahakSocketService.dto.LateAcceptingDriverDto;
import com.example.VahakSocketService.dto.WinnerDriverDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public KafkaConsumerService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @KafkaListener(topics = "winner-notif-topic", groupId = "sample-group-21", containerFactory = "kafkaListenerContainerFactory1")
    public void listen(WinnerDriverDto winnerDriverDto){
        String payload = "You got the booking-id: "+winnerDriverDto.getBookingId();
        System.out.println("winner drivers: "+winnerDriverDto.getWinnerDriverId());
        simpMessagingTemplate.convertAndSend("/topic/winner/driver/"+winnerDriverDto.getWinnerDriverId(), payload);

        payload = "No notif required, just vanish the pop-up";
        for(String driverId: winnerDriverDto.getOtherDriverIds()){
            System.out.println("other drivers: "+driverId);
            simpMessagingTemplate.convertAndSend("/topic/looser/driver/"+driverId, payload);
        }
    }


    @KafkaListener(topics = "late-accepting-driver-topic", groupId = "sample-group-22", containerFactory = "kafkaListenerContainerFactory2")
    public void listen(LateAcceptingDriverDto lateAcceptingDriverDto) {
        String payload = "Sorry, you were a bit late";
        System.out.println("Late driver: "+lateAcceptingDriverDto.getDriverId());
        simpMessagingTemplate.convertAndSend("/topic/looser/driver/"+lateAcceptingDriverDto.getDriverId(), payload);
    }
}
