package com.runapp.shoesservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoesDeleteRequest {

    private String file_uri;
    private Long shoes_id;
}
