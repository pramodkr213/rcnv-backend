package com.main.jobilitybackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.jobilitybackend.Exceptions.UnauthorizeException;
import com.main.jobilitybackend.dto.responseDTO.DataResponse;
import com.main.jobilitybackend.services.serviceInterface.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;;

    @GetMapping("/profile")
    public ResponseEntity<DataResponse> getProfile(@CookieValue(value = "accessToken", required = false) String cookieToken) throws Exception {
        try {
            String token = null;
            if (cookieToken != null) {
                token = cookieToken;
            }
            if(token==null){
                throw new UnauthorizeException("Unauthorize User");
            }
            DataResponse response = userService.getProfile(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
