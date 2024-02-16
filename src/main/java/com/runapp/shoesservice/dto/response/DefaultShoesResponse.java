package com.runapp.shoesservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultShoesResponse {
    private Long id;
    private String brand;
    private String model;
    private String shoesImageUrl;
}
