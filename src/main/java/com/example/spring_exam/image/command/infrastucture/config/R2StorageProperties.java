package com.example.spring_exam.image.command.infrastucture.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloud-flare-r2")
public class R2StorageProperties {
    private String endpoint;
    private String region;
    private String accessKey;
    private String secretKey;
    private String imageBucket;
    private String imagePublic;
}
