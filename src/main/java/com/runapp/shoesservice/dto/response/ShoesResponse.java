package com.runapp.shoesservice.dto.response;

import com.runapp.shoesservice.utill.ConditionShoesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesResponse {

    private Long id;
    private String brand;
    private String model;
    private int size;
    private int mileage;
    private ConditionShoesEnum condition;
    private String userId;
    private String shoes_image_url;
}
