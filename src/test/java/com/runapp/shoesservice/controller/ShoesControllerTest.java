package com.runapp.shoesservice.controller;//package com.runapp.shoesservice.controller;

import com.runapp.shoesservice.dto.request.ShoesRequest;
import com.runapp.shoesservice.dto.response.ShoesResponse;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.service.ShoesService;
import com.runapp.shoesservice.dto.shoesDtoMapper.ShoesDtoMapper;
import com.runapp.shoesservice.feignClient.StorageServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ShoesControllerTest {

    @Mock
    private ShoesService shoesService;

    @Mock
    private ShoesDtoMapper shoesDtoMapper;

    @Mock
    private StorageServiceClient storageServiceClient;

    @InjectMocks
    private ShoesController shoesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllShoes() {
        // Arrange
        List<ShoesModel> shoesModels = new ArrayList<>();
        shoesModels.add(new ShoesModel());
        when(shoesService.getAllShoes()).thenReturn(shoesModels);

        List<ShoesResponse> expectedResponses = new ArrayList<>();
        expectedResponses.add(new ShoesResponse());
        when(shoesDtoMapper.toResponse(any(ShoesModel.class))).thenReturn(new ShoesResponse());

        // Act
        List<ShoesResponse> actualResponses = shoesController.getAllShoes();

        // Assert
        assertEquals(expectedResponses.size(), actualResponses.size());
        verify(shoesService, times(1)).getAllShoes();
        verify(shoesDtoMapper, times(1)).toResponse(any(ShoesModel.class));
    }

    @Test
    void testGetShoesById() {
        // Arrange
        Long id = 1L;
        ShoesModel shoesModel = new ShoesModel();
        when(shoesService.getShoesById(id)).thenReturn(Optional.of(shoesModel));

        ShoesResponse expectedResponse = new ShoesResponse();
        when(shoesDtoMapper.toResponse(shoesModel)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ShoesResponse> responseEntity = shoesController.getShoesById(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(shoesService, times(1)).getShoesById(id);
        verify(shoesDtoMapper, times(1)).toResponse(shoesModel);
    }

    @Test
    void testCreateShoes() {
        // Arrange
        ShoesRequest shoesRequest = new ShoesRequest();
        ShoesModel shoesModel = new ShoesModel();
        when(shoesDtoMapper.toModel(shoesRequest)).thenReturn(shoesModel);

        ShoesModel createdShoes = new ShoesModel();
        when(shoesService.createShoes(shoesModel)).thenReturn(createdShoes);

        ShoesResponse expectedResponse = new ShoesResponse();
        when(shoesDtoMapper.toResponse(createdShoes)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ShoesResponse> responseEntity = shoesController.createShoes(shoesRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(shoesDtoMapper, times(1)).toModel(shoesRequest);
        verify(shoesService, times(1)).createShoes(shoesModel);
        verify(shoesDtoMapper, times(1)).toResponse(createdShoes);
    }
}

