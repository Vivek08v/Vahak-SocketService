package com.example.VahakSocketService.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {
    private String data;
    private Date date;
}
