package com.aws.playground.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.s3.config")
public class AWSS3Config {

    private String partnerName;
    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String endpointUrl;

}
