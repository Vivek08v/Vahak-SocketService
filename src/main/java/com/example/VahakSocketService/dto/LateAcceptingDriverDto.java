package com.example.VahakSocketService.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LateAcceptingDriverDto {
    private Long bookingId;

    private Long driverId;

    private Long message;
}
