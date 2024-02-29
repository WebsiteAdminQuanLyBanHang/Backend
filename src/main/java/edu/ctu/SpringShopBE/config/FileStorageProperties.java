package edu.ctu.SpringShopBE.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadLogoDir;

    private String uploadProductImageDir;
}
