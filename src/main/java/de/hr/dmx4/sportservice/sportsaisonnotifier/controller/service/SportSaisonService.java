package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaisonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SportSaisonService {

    private final FileStorageServiceClient fileStorageServiceClient;
    @Autowired
    public SportSaisonService(FileStorageServiceClient fileStorageServiceClient) {
        this.fileStorageServiceClient = fileStorageServiceClient;
    }
    /*
       Konvertiert eine Zelleninformation in Form eines Datums in ein LocalDate-Objekt.
       @param cell - die Zelle, die das Datum enthält.
       @return Das LocalDate-Objekt, das aus der Zelleninformation extrahiert wurde.
     */
    public static LocalDate convertDateCellToLocalDate(Cell cell) {
        LocalDate localDate = null;
        String cellValue = cell.toString().replace("\t", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        try {
            // Überflüssige Tabulatoren entfernen (Replace "\t")
            localDate = LocalDate.parse(cellValue, formatter);
        } catch (Exception e) {
            formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                localDate = LocalDate.parse(cellValue, formatter);

            } catch (Exception ex) {

                log.warn("Konnte Datum nicht parsen: " + cell);
            }
        }
        return localDate;
    }

    public static InputStream convertWorkbookToInputStream(Workbook workbook) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (bos) {
            workbook.write(bos);
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /*
      Bestätigt einen Eintrag in der Sport-Saison anhand des übergebenen Parameters.
      Der Status in der Tabelle wird auf "True" gesetzt, wenn der Eintrag gefunden wird.
      @param eintragTableStarDeliverer - Der zu bestätigende Eintrag.
      @return true, wenn die Bestätigung erfolgreich war, sonst false.
    */
    public Boolean confirm(String eintragTableStarDeliverer) {
        try (Workbook workbook = readExcelFile()) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cell = row.getCell(SportSaisonConstants.INDEX_EINTRAG_TABLESTARDELIVERER);
                String tableStarDelivererEntry = cell.getStringCellValue();
                if (tableStarDelivererEntry.equals(eintragTableStarDeliverer) || tableStarDelivererEntry.equals("#" + eintragTableStarDeliverer)) {
                    // Bestätigungsstatus in der Tabelle auf "True" setzen
                    row.createCell(SportSaisonConstants.INDEX_ERLEDIGT).setCellValue("True");
                    writeExcelFile(workbook);
                    return true;
                }
            }
        } catch (IOException e) {
            // Fehlerbehandlung für IOException
            log.error("Fehler beim Bestätigen der SportSaison: {}", e.getMessage());
            return false;
        }
        return false;
    }

    /*
      Liest eine Excel-Datei und gibt ein Workbook-Objekt zurück.
      Die Methode verwendet den Dateipfad aus den SportSaisonConstants.
      @return Das gelesene Workbook-Objekt.
      @throws IOException, wenn ein Fehler beim Lesen der Excel-Datei auftritt.
    */
    public Workbook readExcelFile() throws IOException {
        InputStream downloadFile = fileStorageServiceClient.loadLatest();
        Workbook workbook;
        if (downloadFile != null) {
            workbook = new XSSFWorkbook(downloadFile);
        } else {
            try (FileInputStream fileResource = new FileInputStream(SportSaisonConstants.FILE_PATH)) {
                workbook = new XSSFWorkbook(fileResource);
            }
        }
        return workbook;
    }

    /*
    Schreibt ein Workbook-Objekt in eine Excel-Datei.
    Die Methode verwendet den Dateipfad aus den SportSaisonConstants.
    @param workbook - Das zu schreibende Workbook-Objekt.
    @throws IOException Wenn ein Fehler beim Schreiben in die Excel-Datei auftritt.
    */
    private void writeExcelFile(Workbook workbook) throws IOException {
        try (InputStream inputStream = convertWorkbookToInputStream(workbook)) {
            fileStorageServiceClient.uploadToGCP(inputStream);
            log.info("Excel file successfully saved to GCP.");
        } catch (Exception e) {
            log.error("Error while saving Excel file to GCP: {}", e.getMessage());
            throw new RuntimeException("Error while saving Excel file to GCP", e);
        }
    }


    /*
    Liest alle Sport-Saisons aus der Excel-Datei und gibt sie als Liste von SportSaison-Objekten zurück.
    @return Eine Liste von SportSaison-Objekten, die aus der Excel-Datei extrahiert wurden.
    */
    public List<SportSaison> getAllSportSaisons() {
        List<SportSaison> sportSaisonList = new ArrayList<>();
        try (Workbook workbook = readExcelFile()) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                // Header-Zeile überspringen

                SportSaison sportSaison = new SportSaison();
                sportSaison.setSportArt(row.getCell(SportSaisonConstants.INDEX_SPORTART).getStringCellValue());
                sportSaison.setTeilNehmer(row.getCell(SportSaisonConstants.INDEX_TEILNEHMER).getStringCellValue());
                sportSaison.setEintragTableStarDeliverer(row.getCell(SportSaisonConstants.INDEX_EINTRAG_TABLESTARDELIVERER).getStringCellValue());
                sportSaison.setTableStarId((long) row.getCell(SportSaisonConstants.INDEX_TABLESTAR_ID).getNumericCellValue());
                sportSaison.setSaisonStart(convertDateCellToLocalDate(row.getCell(SportSaisonConstants.INDEX_SAISONSTART)));
                sportSaison.setSaisonEnde(convertDateCellToLocalDate(row.getCell(SportSaisonConstants.INDEX_SAISONENDE)));
                sportSaison.setAdditionalInfo(row.getCell(SportSaisonConstants.INDEX_ZUSATZINFO).getStringCellValue());
                sportSaison.setStatus(row.getCell(SportSaisonConstants.INDEX_STATUS).getStringCellValue());
                sportSaisonList.add(sportSaison);
            }
        } catch (IOException e) {
            // IOException behandeln (wirft eine RuntimeException, wenn etwas schiefgeht.)
            log.error("Fehler beim Lesen der SportSaisons: {}", e.getMessage());
        }

        return sportSaisonList;
    }

}
