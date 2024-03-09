package com.runapp.shoesservice.service;

import com.runapp.shoesservice.model.DefaultShoesModel;
import com.runapp.shoesservice.repository.DefaultShoesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultShoesServiceTest {

    @Mock
    private DefaultShoesRepository defaultShoesRepository;

    @InjectMocks
    private DefaultShoesService defaultShoesService;

    @Test
    void testGetAllShoes() {
        // Mocking the repository response
        List<DefaultShoesModel> shoesModels = Arrays.asList(new DefaultShoesModel(), new DefaultShoesModel());
        when(defaultShoesRepository.findAll()).thenReturn(shoesModels);

        // Perform the method call
        List<DefaultShoesModel> result = defaultShoesService.getAllShoes();

        // Verify the result
        assertEquals(2, result.size());
        verify(defaultShoesRepository, times(1)).findAll();
    }
}