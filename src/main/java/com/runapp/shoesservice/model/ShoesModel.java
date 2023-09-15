package com.runapp.shoesservice.model;

import com.runapp.shoesservice.utill.ConditionShoesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Shoes")
public class ShoesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "size")
    private int size;

    @Column(name = "mileage")
    private int mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private ConditionShoesEnum condition;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "shoes_image_url")
    private String shoesImageUrl;
}
