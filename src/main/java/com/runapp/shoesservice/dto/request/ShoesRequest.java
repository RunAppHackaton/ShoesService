package com.runapp.shoesservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesRequest {

    private String brand;
    private String model;
    private int size;
    private int mileage;
    private String userId;
}
