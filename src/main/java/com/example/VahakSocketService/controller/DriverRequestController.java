package com.example.VahakSocketService.controller;

import com.example.VahakSocketService.dto.RideRequestDto;
import com.example.VahakSocketService.dto.RideResponseDto;
import com.example.VahakSocketService.dto.UpdateBookingDto;
import com.example.VahakSocketService.dto.UpdateBookingResponseDto;
import com.example.VahakSocketService.producers.KafkaProducerService;
import com.example.vahakentityservice.models.BookingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/socket")
public class DriverRequestController {

    public final SimpMessagingTemplate simpMessagingTemplate;

    public final RestTemplate restTemplate;

    public KafkaProducerService kafkaProducerService;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate, RestTemplate restTemplate, KafkaProducerService kafkaProducerService){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = restTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/newRide")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto){
        System.out.println(rideRequestDto.getBookingId() +" "+ rideRequestDto.getPassengerId());
        sendDriverNewRideRequest(rideRequestDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    public void sendDriverNewRideRequest(RideRequestDto rideRequestDto){
        // TODO: request should only go to nearby drivers not everyone
        // use rideRequestDto.driverIds to get all the nearby driverIds
        if(rideRequestDto.getDriverIds()==null){
            System.out.println("No driver Available");
            return;
        }

        System.out.println("Ride request is being sent to the following drivers: ");
        for(Long driverId: rideRequestDto.getDriverIds()){
            RideRequestDto payload = rideRequestDto;
//            String payload = "Hello driver, this is a new ride request";
            System.out.println("<<< "+driverId);
            simpMessagingTemplate.convertAndSend("/topic/driver/"+driverId, payload);
        }
        // simpMessagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDto);
    }

    @MessageMapping("/rideResponse/{userId}")
    public void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){
        System.out.println("RESPONSE >>> " + rideResponseDto.getResponse() + " from driver Id: " + userId);

        UpdateBookingDto requsetDto = UpdateBookingDto.builder()
                .bookingId(rideResponseDto.bookingId)
                .driverId(Optional.of(Long.parseLong(userId)))
                .didAccept(rideResponseDto.getResponse())
                .bookingStatus(BookingStatus.SCHEDULED)
                .build();

//        System.out.println("http://localhost:8008/api/v1/booking/"+rideResponseDto.bookingId);
//        System.out.println(requsetDto.getDriverId()+" "+requsetDto.getBookingStatus());

        // ResponseEntity<UpdateBookingResponseDto> result = this.restTemplate.postForEntity("http://localhost:7008/api/v1/booking/"+rideResponseDto.bookingId, requsetDto, UpdateBookingResponseDto.class);
        kafkaProducerService.publishMessage("sample-topic-1", requsetDto);
    }

    @GetMapping
    public Boolean help(){
        kafkaProducerService.publishMessage("sample-topic-1", new UpdateBookingDto());
        return true;
    }
}
