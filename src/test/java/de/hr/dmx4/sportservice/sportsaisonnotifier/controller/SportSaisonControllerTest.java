package de.hr.dmx4.sportservice.sportsaisonnotifier.controller;

import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.FileStorageServiceClient;
import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.FileStorageServiceClientTest;
import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.SportSaisonService;
import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class SportSaisonControllerTest {

    @Autowired
    SportSaisonController sportSaisonController;
    @Autowired
    SportSaisonService sportSaisonService;

    @Test
    public void sendNotificationsForAllSportSaisonsTest() throws Exception {
        ResponseEntity<String> response = this.sportSaisonController.sendNotificationsForAllSportSaisons();
        ResponseEntity<String> expected = ResponseEntity.ok("E-Mails erfolgreich versendet. Anzahl der Ligen: 2");
        Assertions.assertEquals(expected, response);
    }

    @Test
    public void confirmTest() {
        ResponseEntity<String> confirm = this.sportSaisonController.confirm("basketball.damen.dbbl_pokal_top4");
        ResponseEntity<String> expected = ResponseEntity.ok(" Vielen Dank! Ihre Bestätigung für TableStarDelivererEntry (" + "basketball.damen.dbbl_pokal_top4" + ") wurde erfolgreich durchgeführt.");
        Assertions.assertEquals(expected, confirm);
    }

    @Test
    void uploadExcelFileTest() throws IOException {
        // TODO: Teste die Methode sportSaisonController.uploadExcelFile(file)
        // TODO: Lade den Inhalt der Excel-Datei aus den resources und erstelle ein MockMultipartFile.
        // Bsp: MultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, "hierExcelDatei".getBytes());
        // Beachte: file ist ein MultipartFile. Dafür kannst du ein Dummy-File erstellen mit MockMultipartFile.
        InputStream fileContent = getClass().getClassLoader().getResourceAsStream("FINALE_VERSION_tablestardelivererSaisonstart-Saisonende-2023-2024.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("testFile", fileContent);
        ResponseEntity<String> content = sportSaisonController.uploadExcelFile(multipartFile);
        Assertions.assertNotNull(content);
        Assertions.assertEquals(HttpStatus.OK, ResponseEntity.ok());
        Assertions.assertEquals(ResponseEntity.status(500), ResponseEntity.status(400));

    }

    @Test
    void getExcelContentTest() {

        //TODO: Teste die Methode sportSaisonController.getExcelContent(type)
        //TODO: Teste die Methode mit type = "xls" und type = "json"
        ResponseEntity<?> jsonResponse = sportSaisonController.getExcelContent("json");
        Assertions.assertEquals(HttpStatus.OK, jsonResponse.getStatusCode());
        Assertions.assertNotNull(jsonResponse.getBody());
        List<SportSaison> responseList = (List<SportSaison>) jsonResponse.getBody();
        Assertions.assertEquals(sportSaisonService.getAllSportSaisons(), responseList);

        // Teste die Methode sportSaisonController.getExcelContent(type) für XLS
        ResponseEntity<?> xlsResponse = sportSaisonController.getExcelContent("xls");
        Assertions.assertEquals(HttpStatus.OK, xlsResponse.getStatusCode());
        Assertions.assertNotNull(xlsResponse.getBody());


    }

}

