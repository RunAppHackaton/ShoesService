package com.runapp.shoesservice.controller;

import com.runapp.shoesservice.dto.request.DeleteStorageRequest;
import com.runapp.shoesservice.dto.request.ShoesDeleteRequest;
import com.runapp.shoesservice.dto.request.ShoesRequest;
import com.runapp.shoesservice.dto.response.DeleteResponse;
import com.runapp.shoesservice.dto.response.ShoesResponse;
import com.runapp.shoesservice.dto.shoesDtoMapper.ShoesDtoMapper;
import com.runapp.shoesservice.feignClient.StorageServiceClient;
import com.runapp.shoesservice.model.ShoesModel;
import com.runapp.shoesservice.service.ShoesService;
import com.runapp.shoesservice.utill.ConditionShoesEnum;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoes")
@Tag(name = "Shoes Management", description = "Operations related to shoes")
public class ShoesController {

    private final ShoesService shoesService;
    private final ShoesDtoMapper shoesDtoMapper;
    private final StorageServiceClient storageServiceClient;

    @Value("${storage-directory}")
    private String storageDirectory;

    @Autowired
    public ShoesController(ShoesService shoesService, ShoesDtoMapper shoesDtoMapper, StorageServiceClient storageServiceClient) {
        this.shoesService = shoesService;
        this.shoesDtoMapper = shoesDtoMapper;
        this.storageServiceClient = storageServiceClient;
    }

    @GetMapping
    @Operation(summary = "Get all shoes", description = "Retrieve a list of all shoes")
    public List<ShoesResponse> getAllShoes() {
        List<ShoesModel> shoesModels = shoesService.getAllShoes();
        List<ShoesResponse> shoesResponses = shoesModels.stream()
                .map(shoesDtoMapper::toResponse)
                .collect(Collectors.toList());
        return shoesResponses;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shoes by ID", description = "Retrieve shoes by their ID")
    @ApiResponse(responseCode = "200", description = "Shoes found", content = @Content(schema = @Schema(implementation = ShoesResponse.class)))
    @ApiResponse(responseCode = "404", description = "Shoes not found")
    public ResponseEntity<ShoesResponse> getShoesById(@Parameter(description = "Shoes ID", required = true) @PathVariable Long id) {
        Optional<ShoesModel> shoesModelOptional = shoesService.getShoesById(id);
        Optional<ShoesResponse> shoesResponseOptional = shoesModelOptional.map(shoesDtoMapper::toResponse);

        return shoesResponseOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Create new shoes", description = "Create new shoes with the provided data")
    @ApiResponse(responseCode = "201", description = "Shoes created", content = @Content(schema = @Schema(implementation = ShoesResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<ShoesResponse> createShoes(@Parameter(description = "Shoes data", required = true)
                                                     @RequestBody ShoesRequest shoesRequest) {
        ShoesModel shoesModel = shoesDtoMapper.toModel(shoesRequest);
        ShoesModel createdShoes = shoesService.createShoes(shoesModel);
        ShoesResponse createdShoesResponse = shoesDtoMapper.toResponse(createdShoes);
        return new ResponseEntity<>(createdShoesResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shoes", description = "Update existing shoes with the provided data")
    @ApiResponse(responseCode = "200", description = "Shoes updated", content = @Content(schema = @Schema(implementation = ShoesResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Shoes not found")
    public ResponseEntity<ShoesResponse> updateShoes(@Parameter(description = "Shoes ID", required = true)
                                                     @PathVariable Long id,
                                                     @Parameter(description = "Updated shoes data", required = true)
                                                     @RequestBody ShoesRequest shoesRequest) {
        Optional<ShoesModel> optionalShoesModel = shoesService.getShoesById(id);
        if (optionalShoesModel.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ShoesModel shoesModel = shoesDtoMapper.toModel(shoesRequest);
        ShoesModel updatedShoes = shoesService.updateShoes(id, shoesModel);
        ShoesResponse updatedShoesResponse = shoesDtoMapper.toResponse(updatedShoes);
        return new ResponseEntity<>(updatedShoesResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shoes", description = "Delete shoes by their ID")
    @ApiResponse(responseCode = "204", description = "Shoes deleted")
    @ApiResponse(responseCode = "404", description = "Shoes not found")
    public ResponseEntity<DeleteResponse> deleteShoes(@Parameter(description = "Shoes ID", required = true)
                                                      @PathVariable Long id) {
        Optional<ShoesModel> optionalShoesModel = shoesService.getShoesById(id);
        if (optionalShoesModel.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        shoesService.deleteShoes(id);
        return new ResponseEntity<>(new DeleteResponse("Shoes deleted successfully"), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/upload-image")
    @Operation(summary = "Upload an image for shoes", description = "Upload an image file for specific shoes by providing the file and shoes ID.")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = @Content(schema = @Schema(implementation = ShoesModel.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Shoes not found")
    public ResponseEntity<Object> uploadImage(
            @Parameter(description = "Image file to upload", required = true) @RequestParam("file") MultipartFile file,
            @Parameter(description = "ID of the story to associate with the uploaded image", required = true) @RequestParam("shoes_id") Long shoesId) {
        Optional<ShoesModel> optionalShoesModel = shoesService.getShoesById(shoesId);
        if (optionalShoesModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shoes with id " + shoesId + " not found");
        } else {
            ShoesModel shoesModel = optionalShoesModel.orElse(null);
            shoesModel.setShoesImageUrl(storageServiceClient.uploadFile(file, storageDirectory).getFile_uri());
            shoesService.updateShoes(shoesId, shoesModel);
            return ResponseEntity.ok().body(shoesModel);
        }
    }

    @DeleteMapping("/delete-image")
    @Operation(summary = "Delete an image associated with a story", description = "Delete the image associated with a story by providing the image URI and story details.")
    @ApiResponse(responseCode = "200", description = "Image deleted successfully")
    @ApiResponse(responseCode = "404", description = "Story not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Object> deleteImage(@Parameter(description = "Request body containing story ID and image URI", required = true)
                                              @RequestBody ShoesDeleteRequest shoesDeleteRequest) {
        Optional<ShoesModel> optionalShoesModel = shoesService.getShoesById(shoesDeleteRequest.getShoes_id());
        if (optionalShoesModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shoes with id " + shoesDeleteRequest.getShoes_id() + " not found");
        }
        ShoesModel shoesModel = optionalShoesModel.orElse(null);
        shoesModel.setShoesImageUrl("DEFAULT");
        shoesService.updateShoes(shoesDeleteRequest.getShoes_id(), shoesModel);
        try {
            storageServiceClient.deleteFile(new DeleteStorageRequest(shoesDeleteRequest.getFile_uri(), storageDirectory));
            return ResponseEntity.ok().build();
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse("the image does not exist or the data was transferred incorrectly"));
        }
    }

    @PutMapping("/update-mileage/{id}/{additionalKilometers}")
    @Operation(summary = "Update shoes mileage", description = "Update the mileage of shoes by providing the shoes ID and additional kilometers")
    @ApiResponse(responseCode = "200", description = "Mileage updated successfully", content = @io.swagger.v3.oas.annotations.media.Content)
    @ApiResponse(responseCode = "404", description = "Shoes not found")
    public ResponseEntity<Object> updateMileage(
            @Parameter(description = "Shoes ID", required = true) @PathVariable Long id,
            @Parameter(description = "Additional kilometers", required = true) @PathVariable int additionalKilometers) {
        ShoesModel shoesModel = shoesService.getShoesById(id).orElse(null);

        if (shoesModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shoes with id " + id + " not found");
        }

        int updatedMileage = shoesModel.getMileage() + additionalKilometers;
        shoesModel.setMileage(updatedMileage);

        if (updatedMileage < 150) {
            shoesModel.setCondition(ConditionShoesEnum.USED);
        } else if (updatedMileage < 400) {
            shoesModel.setCondition(ConditionShoesEnum.WORN_OUT);
        } else if (updatedMileage < 800) {
            shoesModel.setCondition(ConditionShoesEnum.DAMAGED);
        }
        shoesService.updateShoes(id, shoesModel);

        return ResponseEntity.ok().build();
    }
}
