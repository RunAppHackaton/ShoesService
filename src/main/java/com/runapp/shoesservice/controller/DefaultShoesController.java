package com.runapp.shoesservice.controller;

import com.runapp.shoesservice.dto.response.DefaultShoesResponse;
import com.runapp.shoesservice.dto.response.ShoesResponse;
import com.runapp.shoesservice.dto.shoesDtoMapper.DefaultShoesDtoMapper;
import com.runapp.shoesservice.model.DefaultShoesModel;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.repository.DefaultShoesRepository;
import com.runapp.shoesservice.service.DefaultShoesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/default-shoes")
@AllArgsConstructor
@Tag(name = "Shoes Management", description = "Operations related to shoes")
public class DefaultShoesController {

    private final DefaultShoesService defaultShoesService;
    private final DefaultShoesDtoMapper defaultShoesDtoMapper;

    @GetMapping
    @Operation(summary = "Get all default shoes", description = "Retrieve a list of all shoes")
    public List<DefaultShoesResponse> getAllShoes() {
        List<DefaultShoesModel> shoesModels = defaultShoesService.getAllShoes();
        return shoesModels.stream()
                .map(defaultShoesDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
