package com.flux.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3Service{
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public void uploadFile(Path filePath,String s3Key){
        PutObjectRequest request = PutObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(s3Key)
                                                    .build();
        s3Client.putObject(request, RequestBody.fromFile(filePath));
    }
    public void downloadFile(String s3Key, Path destination) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.getObject(request, destination);
    }
    public void testConnection(){
        s3Client.headBucket(builder -> builder.bucket(bucketName));
    }
}
