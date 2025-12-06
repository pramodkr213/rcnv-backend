package com.main.jobilitybackend.enumConst;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${file.upload-dir}")
    public static String uploadDir;
}
