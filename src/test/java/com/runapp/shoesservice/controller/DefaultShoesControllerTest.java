package com.runapp.shoesservice.controller;

import com.runapp.shoesservice.controller.DefaultShoesController;
import com.runapp.shoesservice.dto.response.DefaultShoesResponse;
import com.runapp.shoesservice.dto.shoesDtoMapper.DefaultShoesDtoMapper;
import com.runapp.shoesservice.model.DefaultShoesModel;
import com.runapp.shoesservice.service.DefaultShoesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultShoesControllerTest {

    @Mock
    private DefaultShoesService defaultShoesService;

    @Mock
    private DefaultShoesDtoMapper defaultShoesDtoMapper;

    @InjectMocks
    private DefaultShoesController defaultShoesController;

    @Test
    void testGetAllShoes() {
        // Mocking the service response
        List<DefaultShoesModel> shoesModels = Arrays.asList(new DefaultShoesModel(), new DefaultShoesModel());
        when(defaultShoesService.getAllShoes()).thenReturn(shoesModels);

        // Mocking the mapper response
        DefaultShoesResponse response1 = new DefaultShoesResponse();
        DefaultShoesResponse response2 = new DefaultShoesResponse();
        when(defaultShoesDtoMapper.toResponse(any(DefaultShoesModel.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        // Perform the GET request
        List<DefaultShoesResponse> response = defaultShoesController.getAllShoes();

        // Verify the results
        assertEquals(2, response.size());
        verify(defaultShoesService, times(1)).getAllShoes();
        verify(defaultShoesDtoMapper, times(2)).toResponse(any(DefaultShoesModel.class));
    }
}

