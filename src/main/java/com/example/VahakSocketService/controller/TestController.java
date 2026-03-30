package com.example.VahakSocketService.controller;

import com.example.VahakSocketService.dto.ChatRequest;
import com.example.VahakSocketService.dto.ChatResponse;
import com.example.VahakSocketService.dto.TestRequest;
import com.example.VahakSocketService.dto.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class TestController {

    final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")  // get from client
    @SendTo("/topic/ping")   // send back to client
    // sendTo works when the method is triggered by MessageMapping
    public TestResponse pingCheck(TestRequest request){
        System.out.println("Received message from client: "+ request.getData());
        return TestResponse.builder()
                .data(request.getData())
                .date(new Date())
                .build();
    }

//    @Scheduled(fixedDelay = 1000)   // It is just a background thread execution
//    @SendTo("/topic/scheduled")
//    public void scheduleFixedDelayTask() {
//        System.out.println("Fixed delay task - "+System.currentTimeMillis()/1000);
//        simpMessagingTemplate.convertAndSend("/topic/scheduled", "Periodic message sent "+ System.currentTimeMillis()/1000);
//    }

//    @MessageMapping("/chat")
//    @SendTo("/topic/chat")
//    public ChatResponse chatMessageToAll(ChatRequest chatRequest){
//        return ChatResponse.builder()
//                .name(chatRequest.getName())
//                .message(chatRequest.getMessage())
//                .date(new Date())
//                .build();
//    }

    @MessageMapping("/privateChat/{roomId}/{userId}")
    public void privateChatMessage(@DestinationVariable String roomId, @DestinationVariable String userId, ChatRequest chatRequest){
        ChatResponse chatResponse = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .date(new Date())
                .build();
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/privateChat/"+roomId, chatResponse);
    }
}
