package de.hr.dmx4.sportservice.sportsaisonnotifier.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FileStorageConfig {

    @Value("${fileStorageRestServiceUrl}")
    private String fileStorageRestServiceUrl;

    @Value("${fileStorageRestServicePath}")
    private String fileStorageRestServicePath;

    @Value("${fileStorageThema}")
    private String fileStorageThema;

    @Value("${fileStorageName}")
    private String fileStorageName;


}
