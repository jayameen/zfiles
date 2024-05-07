package com.jayameen.zfiles.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author Madan KN
 */

@Configuration
public class ApplicationBeanConfigurations {

    @Value("${gcp.config-data:''}")
    private String gcpConfigData;

    @Value("${gcp.project.id:''}")
    private String gcpProjectId;

    @Value("${s3.key:''}")
    private String s3Key;

    @Value("${s3.secret:''}")
    private String s3Secret;

    @Value("${s3.upload-url:''}")
    private String uploadURL;

    @Value("${s3.region:''}")
    private String s3Region;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean("gcpStorage")
    Storage getStorage() throws Exception {
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                                     .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(Base64.getDecoder().decode(gcpConfigData))))
                                    .build();
            return options.getService();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean("awsS3Client")
    public AmazonS3 generateAWSS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(s3Key,s3Secret);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration(uploadURL, s3Region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
