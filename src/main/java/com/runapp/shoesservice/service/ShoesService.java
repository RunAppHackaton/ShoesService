package com.runapp.shoesservice.service;

import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.repository.ShoesRepository;
import com.runapp.shoesservice.utill.UserExistHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoesService {

    private final ShoesRepository shoesRepository;
    private final UserExistHandler userExistHandler;

    @Autowired
    public ShoesService(ShoesRepository shoesRepository, UserExistHandler userExistHandler) {
        this.shoesRepository = shoesRepository;
        this.userExistHandler = userExistHandler;
    }

    public List<ShoesModel> getAllShoes() {
        return shoesRepository.findAll();
    }

    public Optional<ShoesModel> getShoesById(Long id) {
        return shoesRepository.findById(id);
    }

    public ShoesModel createShoes(ShoesModel shoesModel) {
        // this method check user exists
        userExistHandler.handleRequest(shoesModel.getUserId());

        return shoesRepository.save(shoesModel);
    }

    public ShoesModel updateShoes(Long id, ShoesModel updatedShoes) {
        // this method check user exists
        userExistHandler.handleRequest(updatedShoes.getUserId());

        if (shoesRepository.existsById(id)) {
            updatedShoes.setId(id);
            return shoesRepository.save(updatedShoes);
        } else {
            throw new RuntimeException("Shoes with id " + id + " not found.");
        }
    }

    public void deleteShoes(Long id) {
        if (shoesRepository.existsById(id)) {
            shoesRepository.deleteById(id);
        } else {
            throw new RuntimeException("Shoes with id " + id + " not found.");
        }
    }
}
