package com.example.VahakSocketService.dto;

import com.example.vahakentityservice.models.BookingStatus;
import com.example.vahakentityservice.models.Driver;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingResponseDto {

    private Long bookingId;

    private BookingStatus bookingStatus;

    private Optional<Long> driverId;
}
