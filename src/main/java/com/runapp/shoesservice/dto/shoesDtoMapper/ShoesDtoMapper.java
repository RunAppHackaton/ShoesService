package com.runapp.shoesservice.dto.shoesDtoMapper;

import com.runapp.shoesservice.dto.request.ShoesRequest;
import com.runapp.shoesservice.dto.response.ShoesResponse;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.utill.ConditionShoesEnum;
import org.springframework.stereotype.Component;

@Component
public class ShoesDtoMapper {

    public ShoesModel toModel(ShoesRequest shoesRequest) {
        ShoesModel shoesModel = new ShoesModel();
        shoesModel.setBrand(shoesRequest.getBrand());
        shoesModel.setModel(shoesRequest.getModel());
        shoesModel.setSize(shoesRequest.getSize());
        shoesModel.setMileage(shoesRequest.getMileage());
        shoesModel.setCondition(ConditionShoesEnum.NEW);
        shoesModel.setUserId(shoesRequest.getUserId());
        shoesModel.setShoesImageUrl("DEFAULT");
        return shoesModel;
    }

    public ShoesResponse toResponse(ShoesModel shoesModel) {
        ShoesResponse shoesResponse = new ShoesResponse();
        shoesResponse.setId(shoesModel.getId());
        shoesResponse.setBrand(shoesModel.getBrand());
        shoesResponse.setModel(shoesModel.getModel());
        shoesResponse.setSize(shoesModel.getSize());
        shoesResponse.setMileage(shoesModel.getMileage());
        shoesResponse.setCondition(shoesModel.getCondition());
        shoesResponse.setUserId(shoesModel.getUserId());
        shoesResponse.setShoes_image_url(shoesModel.getShoesImageUrl());
        return shoesResponse;
    }
}
