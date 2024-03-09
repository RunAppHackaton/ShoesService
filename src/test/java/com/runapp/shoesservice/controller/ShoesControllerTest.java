package com.runapp.shoesservice.controller;//package com.runapp.shoesservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.shoesservice.dto.request.ShoesRequest;
import com.runapp.shoesservice.dto.response.ShoesResponse;
import com.runapp.shoesservice.dto.shoesDtoMapper.ShoesDtoMapper;
import com.runapp.shoesservice.exception.GlobalExceptionHandler;
import com.runapp.shoesservice.feignClient.StorageServiceClient;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.service.ShoesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShoesControllerTest {

    @Mock
    private ShoesService shoesService;

    @Mock
    private ShoesDtoMapper shoesDtoMapper;

    @Mock
    private StorageServiceClient storageServiceClient;

    private ObjectMapper objectMapper;

    @InjectMocks
    private ShoesController shoesController;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shoesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
        ResponseEntity<ShoesResponse> responseEntity = shoesController.createShoes(shoesRequest,"1");

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(shoesDtoMapper, times(1)).toModel(shoesRequest);
        verify(shoesService, times(1)).createShoes(shoesModel);
        verify(shoesDtoMapper, times(1)).toResponse(createdShoes);
    }

    @Test
    public void getShoesById_NonExistingShoes_ReturnsNotFound() throws Exception {
        Long shoesId = 1L;
        // Mock the service response
        Mockito.when(shoesService.getShoesById(shoesId)).thenReturn(Optional.empty());

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/shoes/{id}", shoesId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShoes_ExistingShoes_ReturnsUpdatedShoesResponse() throws Exception {
        Long shoesId = 1L;
        // Mock the service response
        ShoesModel existingShoesModel = new ShoesModel();
        ShoesModel updatedShoesModel = new ShoesModel();
        ShoesResponse expectedResponse = new ShoesResponse();
        ShoesRequest shoesRequest = new ShoesRequest();
        shoesRequest.setBrand("A");
        shoesRequest.setModel("B");
        shoesRequest.setMileage(111);
        shoesRequest.setUserId("1");
        shoesRequest.setSize(33);
        Mockito.when(shoesService.getShoesById(shoesId)).thenReturn(Optional.of(existingShoesModel));
        Mockito.when(shoesService.updateShoes(shoesId, existingShoesModel)).thenReturn(updatedShoesModel);
        Mockito.when(shoesDtoMapper.toResponse(updatedShoesModel)).thenReturn(expectedResponse);

        // Perform the request and assert the response
        mockMvc.perform(put("/shoes/{id}", shoesId).header("X-UserId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoesRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateShoes_NonExistingShoes_ReturnsNotFound() throws Exception {
        Long shoesId = 1L;
        // Mock the service response
        ShoesRequest shoesRequest = new ShoesRequest();
        Mockito.when(shoesService.getShoesById(shoesId)).thenReturn(Optional.empty());

        // Perform the request and assert the response
        mockMvc.perform(put("/shoes/{id}", shoesId).header("X-UserId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shoesRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShoes_ExistingShoes_ReturnsNoContent() throws Exception {
        Long shoesId = 1L;
        // Mock the service response
        Mockito.when(shoesService.getShoesById(shoesId)).thenReturn(Optional.of(new ShoesModel()));

        // Perform the request and assert the response
        mockMvc.perform(delete("/shoes/{id}", shoesId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShoes_NonExistingShoes_ReturnsNotFound() throws Exception {
        Long shoesId = 1L;
        // Mock the service response
        Mockito.when(shoesService.getShoesById(shoesId)).thenReturn(Optional.empty());

        // Perform the request and assert the response
        mockMvc.perform(delete("/shoes/{id}", shoesId))
                .andExpect(status().isNotFound());
    }
}

