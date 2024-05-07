package com.jayameen.zfiles.config;

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

    @Value("${gcp.config-data}")
    private String gcpConfigData;

    @Value("${gcp.project.id}")
    private String gcpProjectId;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Bean
    Storage getStorage() throws Exception {
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                                     .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(Base64.getDecoder().decode(gcpConfigData))))
                                    .build();
            return options.getService();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
