package com.flux.controller;


import com.flux.service.S3Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3TestController{
    private final S3Service s3Service;


    public S3TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/api/test/s3")
    public  String testS3(){
        s3Service.testConnection();
        return "S3 connection successful";
    }
}
