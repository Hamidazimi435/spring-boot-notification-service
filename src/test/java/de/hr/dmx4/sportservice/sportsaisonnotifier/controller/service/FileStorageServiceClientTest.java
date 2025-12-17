package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FileStorageServiceClientTest {

    @Autowired
    private FileStorageServiceClientIF fileStorageServiceClient;

    @Test
    void loadLatestTest() {
        InputStream actuel = fileStorageServiceClient.loadLatest();
        InputStream expected = readFileFromResources();
        Assertions.assertEquals(expected, actuel);
    }

    @Test
    void storeTest() throws IOException {
        try (InputStream fileContent = readFileFromResources()) {
            fileStorageServiceClient.uploadToGCP(fileContent);
            InputStream loadErgebniss = fileStorageServiceClient.loadLatest();
            assertTrue(IOUtils.contentEquals(fileContent, loadErgebniss));
        }
        //TODO: Damit der Test funktioniert, musst du die Inhalte der beiden InputStreams vergleichen.
        // Dafür kannst du die Methode IOUtils.contentEquals(InputStream input1, InputStream input2) aus der Klasse IOUtils verwenden.
        // Die Methode gibt true zurück, wenn die Inhalte der beiden InputStreams gleich sind.
        // ACHTUNG: Die Variable fileContent muss noch einmal neu initialisert werden mit readFileFromResources(). Denn ein InputStream wird nach "Verwendung" geschlossen.
    }

    private InputStream readFileFromResources() {
        return getClass().getClassLoader().getResourceAsStream("FINALE_VERSION_tablestardelivererSaisonstart-Saisonende-2023-2024.xlsx");
    }
}
