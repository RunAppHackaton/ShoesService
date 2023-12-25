package com.runapp.shoesservice.utill;

import com.runapp.shoesservice.exception.NoEntityFoundException;
import com.runapp.shoesservice.feignClient.ProfileServiceClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserExistHandler {

    private final ProfileServiceClient profileServiceClient;

    public boolean handleRequest(int entityId) {
        try {
            profileServiceClient.getUserById(entityId);
        } catch (FeignException.NotFound e) {
            throw new NoEntityFoundException("User with id: " + entityId + " doesn't exist");
        }
        return true;
    }
}
