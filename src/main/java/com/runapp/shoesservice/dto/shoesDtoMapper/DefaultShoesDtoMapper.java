package com.runapp.shoesservice.dto.shoesDtoMapper;

import com.runapp.shoesservice.dto.request.ShoesRequest;
import com.runapp.shoesservice.dto.response.DefaultShoesResponse;
import com.runapp.shoesservice.model.DefaultShoesModel;
import org.springframework.stereotype.Component;

@Component
public class DefaultShoesDtoMapper {

    public DefaultShoesResponse toModel(ShoesRequest shoesRequest) {
        DefaultShoesResponse shoesModel = new DefaultShoesResponse();
        shoesModel.setBrand(shoesRequest.getBrand());
        shoesModel.setModel(shoesRequest.getModel());
        shoesModel.setShoesImageUrl("DEFAULT");
        return shoesModel;
    }

    public DefaultShoesResponse toResponse(DefaultShoesModel defaultShoesModel) {
        DefaultShoesResponse shoesResponse = new DefaultShoesResponse();
        shoesResponse.setId(defaultShoesModel.getId());
        shoesResponse.setBrand(defaultShoesModel.getBrand());
        shoesResponse.setModel(defaultShoesModel.getModel());
        return shoesResponse;
    }
}
