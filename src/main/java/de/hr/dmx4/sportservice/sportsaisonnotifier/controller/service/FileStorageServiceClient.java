package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;


import de.hr.dmx4.persistence.api.HttpClientApi;
import de.hr.dmx4.persistence.api.exception.MissingRequiredParameterException;
import de.hr.dmx4.persistence.impl.gcp.cloudrun.HttpCloudRunClient;
import de.hr.dmx4.persistence.impl.gcp.storage.util.FileStorageUrl;
import de.hr.dmx4.persistence.impl.gcp.storage.util.FileStorageUrlBuilder;
import de.hr.dmx4.sportservice.sportsaisonnotifier.config.FileStorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.time.Instant;

@Service
@Slf4j
public class FileStorageServiceClient implements FileStorageServiceClientIF {

    private final FileStorageConfig fileStorageConfig;

    private final HttpClientApi httpClientApi;

    @Autowired
    public FileStorageServiceClient(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
        try {
            this.httpClientApi = new HttpCloudRunClient(fileStorageConfig.getFileStorageRestServiceUrl());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private FileStorageUrl buildUrl() throws MissingRequiredParameterException {
        return new FileStorageUrlBuilder()
                .setThema(fileStorageConfig.getFileStorageThema())
                .setFileName(fileStorageConfig.getFileStorageName())
                .setStorageType("original")
                .setHost(fileStorageConfig.getFileStorageRestServiceUrl())
                .setEndpoint(fileStorageConfig.getFileStorageRestServicePath())
                .build();
    }

    @Override
    public void uploadToGCP(InputStream file) {
        try {
            FileStorageUrl storageUrl = buildUrl();
            String erstellteUrl = storageUrl.createPersistUrl("SportSaisonNotifier",
                    "UTF-8", Instant.now(), "7d", null,
                    "application/octet-stream", null, null );

            HttpResponse<String> response =  httpClientApi.post(erstellteUrl,fileStorageConfig.getFileStorageName(), file);
            if (response.statusCode() == HttpStatus.CREATED.value()) {
                log.info("Speicherung war Erfolgreich!");
            } else {
                log.warn("Die Speicherung war nicht Erfolgreich!! HTTP-Statuscode: {}", response.statusCode());
                log.warn("Serverantwort: {}", response.body());
            }
        } catch (Exception e) {
            log.error("Fehler beim Hochladen der Datei: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public InputStream loadLatest() {
        try {
            FileStorageUrl loadUrl = buildUrl();
            String erstellteUrl = loadUrl.createLoadLastUrl(FileStorageUrl.LoadLast.ORIGINAL, true);
            HttpResponse<InputStream> response =  httpClientApi.getAsInputStream(erstellteUrl);
            log.info("Heruntergeladene Datei Response: {}", response);
            if(response.statusCode() == 200){
                return response.body();
            }
        } catch (Exception e) {
            log.error("Fehler beim herunterladen der Datei:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
