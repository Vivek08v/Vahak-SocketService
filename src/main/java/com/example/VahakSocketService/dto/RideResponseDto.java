package com.example.VahakSocketService.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDto {

    public Boolean response;

    public Long bookingId;
}
