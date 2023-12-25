package com.runapp.shoesservice.feignClient;

import com.runapp.shoesservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service")
public interface ProfileServiceClient {

    @GetMapping("/users/{userId}")
    ResponseEntity<UserResponse> getUserById(@PathVariable int userId);
}
