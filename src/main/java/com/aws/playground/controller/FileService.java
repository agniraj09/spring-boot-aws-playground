package com.aws.playground.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.aws.playground.domain.AWSS3Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
public class FileService {

    private final AmazonS3 s3client;
    private final ObjectMapper mapper;
    private final AWSS3Config awsS3Config;

    public FileService(AmazonS3 s3client, ObjectMapper mapper, AWSS3Config awsS3Config) {
        this.s3client = s3client;
        this.mapper = mapper;
        this.awsS3Config = awsS3Config;
    }

    public String uploadFile(String fileName, MultipartFile file) {
        try {
            var putObjectRequest = buildPutObjectRequest(fileName, file);
            var putObjectResult = s3client.putObject(putObjectRequest);
            var response = mapper.writeValueAsString(putObjectResult);
            log.info("File upload response -> {}", response);
            return awsS3Config.getEndpointUrl() + "/" + awsS3Config.getBucketName() + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PutObjectRequest buildPutObjectRequest(String fileName, MultipartFile file) throws IOException {
        // Metadata builder
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // File name builder
        fileName = awsS3Config.getPartnerName() + "/" + fileName;

        return new PutObjectRequest(awsS3Config.getBucketName(), fileName, file.getInputStream(), metadata);
    }

    public Resource downloadFile(String partnerName, String fileName) {

        String objectKey = partnerName + "/" + fileName;

        try {
            if (s3client.doesObjectExist(awsS3Config.getBucketName(), objectKey)) {
                GetObjectRequest getObjectRequest = new GetObjectRequest(awsS3Config.getBucketName(), objectKey);
                var s3Object = s3client.getObject(getObjectRequest);
                return new ByteArrayResource(IOUtils.toByteArray(s3Object.getObjectContent()));
            } else {
                throw new FileNotFoundException("File not available in S3");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
