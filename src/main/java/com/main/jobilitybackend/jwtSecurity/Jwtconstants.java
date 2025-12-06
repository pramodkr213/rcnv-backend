package com.main.jobilitybackend.jwtSecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Jwtconstants {

    @Value("${secret.key}")
    private String apiKey;
    
    private static String API_KEY;

    public static long WEBEXPIRATION=7*24*60*60*1000;
    public static long ANDROIDEXPIRATION=90*24*60*60*1000;

    @PostConstruct
    public void init() {
        API_KEY = apiKey;
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
