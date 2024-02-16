package com.runapp.shoesservice.service;

import com.runapp.shoesservice.model.DefaultShoesModel;
import com.runapp.shoesservice.repository.DefaultShoesRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DefaultShoesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoesService.class);
    private final DefaultShoesRepository defaultShoesRepository;

    public List<DefaultShoesModel> getAllShoes() {
        LOGGER.info("Default Shoes get all");
        return defaultShoesRepository.findAll();
    }
}
