package com.runapp.shoesservice.service;

import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.repository.ShoesRepository;
import com.runapp.shoesservice.utill.ConditionShoesEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoesServiceTest {

    @Mock
    private ShoesRepository shoesRepository;

    @InjectMocks
    private ShoesService shoesService;

    @Test
    void getAllShoes_ReturnsListOfShoes() {
        // Arrange
        List<ShoesModel> shoesList = List.of(new ShoesModel(), new ShoesModel());
        when(shoesRepository.findAll()).thenReturn(shoesList);

        // Act
        List<ShoesModel> result = shoesService.getAllShoes();

        // Assert
        assertEquals(shoesList.size(), result.size());
    }

    @Test
    void getShoesById_ExistingId_ReturnsShoesModel() {
        // Arrange
        Long id = 1L;
        ShoesModel shoesModel = new ShoesModel();
        when(shoesRepository.findById(id)).thenReturn(Optional.of(shoesModel));

        // Act
        Optional<ShoesModel> result = shoesService.getShoesById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(shoesModel, result.get());
    }

    @Test
    void getShoesById_NonExistingId_ReturnsEmptyOptional() {
        // Arrange
        Long id = 999L;
        when(shoesRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<ShoesModel> result = shoesService.getShoesById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    // Similarly, tests for other methods can be added

    @Test
    void updateShoesModelMileage_ReturnsUpdatedShoesModel() {
        // Arrange
        ShoesModel shoesModel = new ShoesModel();
        shoesModel.setId(1L);
        shoesModel.setMileage(100); // Initial mileage

        // Act
        ShoesModel updatedShoesModel = shoesService.updateShoesModelMileage(shoesModel, 50); // Additional 50 kilometers

        // Assert
        assertEquals(150, updatedShoesModel.getMileage());
        assertEquals(ConditionShoesEnum.WORN_OUT, updatedShoesModel.getCondition());
    }
}