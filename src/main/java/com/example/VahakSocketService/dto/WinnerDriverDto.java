package com.example.VahakSocketService.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinnerDriverDto {

    private String winnerDriverId;

    private String bookingId;

    private List<String> otherDriverIds; // (Not Early Rejecting Drivers)

    private String message;
}
