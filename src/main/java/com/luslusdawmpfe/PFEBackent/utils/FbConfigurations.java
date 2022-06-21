package com.luslusdawmpfe.PFEBackent.utils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="firebase")
@Data
public class FbConfigurations {
    private String bucketName;
    private String type;
    private String projectId;
    private String privateKeyId;
    private String privateKey;
    private String clientEmail;
    private String clientId;
    private String authUri;
    private String tokenUri;
    private String authProvider_x509_cert_url;
    private String client_x509_cert_url;

}
