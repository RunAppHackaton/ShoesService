package com.runapp.shoesservice.service;

import com.runapp.shoesservice.exception.NoEntityFoundException;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.repository.ShoesRepository;
import com.runapp.shoesservice.utill.ConditionShoesEnum;
import com.runapp.shoesservice.utill.UserExistHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoesService {

    private final ShoesRepository shoesRepository;
    private final UserExistHandler userExistHandler;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoesService.class);

    @Autowired
    public ShoesService(ShoesRepository shoesRepository, UserExistHandler userExistHandler) {
        this.shoesRepository = shoesRepository;
        this.userExistHandler = userExistHandler;
    }

    public List<ShoesModel> getAllShoes() {
        LOGGER.info("Shoes get all");
        return shoesRepository.findAll();
    }

    public Optional<ShoesModel> getShoesById(Long id) {
        LOGGER.info("Shoes get by id: {}", id);
        return shoesRepository.findById(id);
    }

    public ShoesModel createShoes(ShoesModel shoesModel) {
        LOGGER.info("Shoes add: {}", shoesModel);
        // this method check user exists
        userExistHandler.handleRequest(shoesModel.getUserId());

        return shoesRepository.save(shoesModel);
    }

    public ShoesModel updateShoes(Long id, ShoesModel updatedShoes) {
        LOGGER.info("Shoes update by id: id={}, updatedShoes={}", id, updatedShoes);
        // this method check user exists
        userExistHandler.handleRequest(updatedShoes.getUserId());

        if (shoesRepository.existsById(id)) {
            updatedShoes.setId(id);
            return shoesRepository.save(updatedShoes);
        } else {
            throw new NoEntityFoundException("Shoes with id " + id + " not found.");
        }
    }

    public void deleteShoes(Long id) {
        LOGGER.info("Shoes delete by id: {}", id);
        if (shoesRepository.existsById(id)) {
            shoesRepository.deleteById(id);
        } else {
            throw new NoEntityFoundException("Shoes with id " + id + " not found.");
        }
    }

    public ShoesModel updateShoesModelMileage(ShoesModel shoesModel, int additionalKilometers){
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
