package de.hr.dmx4.sportservice.sportsaisonnotifier.controller;

import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.FileStorageServiceClient;
import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.SportSaisonService;
import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.EmailService;
import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController /*um eine Klasse als Controller in einer Spring-Anwendung zu kennzeichnen, die RESTful-Endpunkte bereitstellt.*/
@RequestMapping("/api/sport/saisons/notifier")
public class SportSaisonController {


    private SportSaisonService sportSaisonService;
    private EmailService emailService;
    private FileStorageServiceClient fileStorageServiceClient;

    @Autowired
    public SportSaisonController(SportSaisonService sportSaisonService
            , EmailService emailService
            , FileStorageServiceClient fileStorageServiceClient) {
        this.sportSaisonService = sportSaisonService;
        this.emailService = emailService;
        this.fileStorageServiceClient = fileStorageServiceClient;
    }
    /*
        Endpunkt für das Senden von Benachrichtigungen für alle Sport-Saisons.
        @return ResponseEntity mit Status und Nachricht über den Erfolg oder
         Fehler des Benachrichtigungsversands.
    */
    @GetMapping({"", "/"})
    public ResponseEntity<String> sendNotificationsForAllSportSaisons() {
        try {
            int emailsSent = emailService.sendNotificationForUpcomingSportSaisons();
            return ResponseEntity.ok("E-Mails erfolgreich versendet. Anzahl der Ligen: " + emailsSent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Fehler beim Senden der Benachrichtigungen: " + e.getMessage());
        }
    }
    /*
   Diese Funktion gibt je nach Parameter "type" entweder den Inhalt der Excel-Datei als Anhang (xls) oder als JSON (json) zurück.
 Standardmäßig wird JSON zurückgegeben, falls "type" nicht angegeben ist.
 Bei einem ungültigen "type"-Wert wird eine Fehlermeldung mit dem HTTP-Statuscode 400 zurückgegeben.
 Bei einem allgemeinen Fehler wird eine Fehlermeldung mit dem HTTP-Statuscode 500 zurückgegeben.
*/
    @GetMapping("/content")
    public ResponseEntity<?> getExcelContent(@RequestParam(name = "type", defaultValue = "json") String type) {
        try {
            if ("xls".equalsIgnoreCase(type)) {
                Workbook excelContent = sportSaisonService.readExcelFile();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                excelContent.write(byteArrayOutputStream);
                InputStream excelInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "sport-saisons.xls");

                return new ResponseEntity<>(excelInputStream, headers, HttpStatus.OK);
            } else if ("json".equalsIgnoreCase(type)) {
                // Wenn "type" auf "json" gesetzt ist, JSON zurückgeben
                List<SportSaison> sportSaisonList = sportSaisonService.getAllSportSaisons();
                return ResponseEntity.ok().body(sportSaisonList);
            } else {
                // Wenn "type" nicht auf "xls" oder "json" gesetzt ist, Fehlermeldung zurückgeben
                return ResponseEntity.status(400).body("Ungültiger Wert für 'type'. Verwenden Sie 'xls' oder 'json'.");
            }
        } catch (Exception e) {
            // Allgemeiner Fehlerfall
            return ResponseEntity.status(500).body("Fehler beim Abrufen des Inhalts: " + e.getMessage());
        }
    }

    @PostMapping("/content")
    public ResponseEntity<String> uploadExcelFile(@RequestPart("file") MultipartFile file) {
        // Überprüfe, ob die Datei vorhanden ist
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Die hochgeladene Datei ist leer.");
        }
        try {
            InputStream inputStream = file.getInputStream();
            // Excel-Datei wird in die GCP geladen
            fileStorageServiceClient.uploadToGCP(inputStream);
            return ResponseEntity.ok("Die Excel-Datei wurde erfolgreich hochgeladen und in die GCP geladen.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Fehler beim Hochladen und Speichern der Excel-Datei: " + e.getMessage());
        }
    }

    /*
    Endpunkt für die Bestätigung eines Eintrags in der Sport-Saison.
     @param eintragTableStarDeliverer - Der zu bestätigende Eintrag.
     @return ResponseEntity mit Status und Nachricht zur Bestätigung.
*/
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam(name = "entry") String eintragTableStarDeliverer) {
        try {
            boolean confirmationResult = sportSaisonService.confirm(eintragTableStarDeliverer);

            if (confirmationResult) {
                return ResponseEntity.ok(" Vielen Dank! Ihre Bestätigung für TableStarDelivererEntry (" + eintragTableStarDeliverer + ") wurde erfolgreich durchgeführt.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TableStarDelivererEntry nicht gefunden.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Bestätigen: " + e.getMessage());
        }
    }
}
