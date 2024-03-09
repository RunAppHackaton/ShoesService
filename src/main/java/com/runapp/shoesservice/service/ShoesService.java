package com.runapp.shoesservice.service;

import com.runapp.shoesservice.exception.NoEntityFoundException;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.repository.ShoesRepository;
import com.runapp.shoesservice.utill.ConditionShoesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoesService {

    private final ShoesRepository shoesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoesService.class);

    @Autowired
    public ShoesService(ShoesRepository shoesRepository) {
        this.shoesRepository = shoesRepository;
    }

    @Cacheable("shoes")
    public List<ShoesModel> getAllShoes() {
        LOGGER.info("Shoes get all");
        return shoesRepository.findAll();
    }

    @Cacheable(value = "shoes", key = "#id")
    public Optional<ShoesModel> getShoesById(Long id) {
        LOGGER.info("Shoes get by id: {}", id);
        return shoesRepository.findById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "shoes", allEntries = true)},
            put = {
                    @CachePut(value = "shoes", key = "#shoesModel.id")
            })
    public ShoesModel createShoes(ShoesModel shoesModel) {
        LOGGER.info("Shoes add: {}", shoesModel);
        // this method check user exists

        return shoesRepository.save(shoesModel);
    }

    @Caching(evict = {
            @CacheEvict(value = "shoes", allEntries = true)},
            put = {
                    @CachePut(value = "shoes", key = "#updatedShoes.id")
            })
    public ShoesModel updateShoes(Long id, ShoesModel updatedShoes) {
        LOGGER.info("Shoes update by id: id={}, updatedShoes={}", id, updatedShoes);
        // this method check user exists

        if (shoesRepository.existsById(id)) {
            updatedShoes.setId(id);
            return shoesRepository.save(updatedShoes);
        } else {
            throw new NoEntityFoundException("Shoes with id " + id + " not found.");
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "shoes", allEntries = true),
            @CacheEvict(value = "shoes", key = "#id")})
    public void deleteShoes(Long id) {
        LOGGER.info("Shoes delete by id: {}", id);
        if (shoesRepository.existsById(id)) {
            shoesRepository.deleteById(id);
        } else {
            throw new NoEntityFoundException("Shoes with id " + id + " not found.");
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "shoes", allEntries = true)},
            put = {
                    @CachePut(value = "shoes", key = "#updatedShoes.id")
            })
    public ShoesModel updateShoesModelMileage(ShoesModel shoesModel, int additionalKilometers) {
        int updatedMileage = shoesModel.getMileage() + additionalKilometers;
        shoesModel.setMileage(updatedMileage);

        if (updatedMileage < 150) {
            shoesModel.setCondition(ConditionShoesEnum.USED);
        } else if (updatedMileage < 400) {
            shoesModel.setCondition(ConditionShoesEnum.WORN_OUT);
        } else if (updatedMileage < 800) {
            shoesModel.setCondition(ConditionShoesEnum.DAMAGED);
        }
        return shoesModel;
    }
}
