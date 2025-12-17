package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SpringBootTest
class SportSaisonServiceTest {
    @Autowired
    SportSaisonService sportSaisonService;

    @Test //
    void convertDateCellToLocalDate() {
        String FILE_PATH = Objects.requireNonNull(getClass().getClassLoader().getResource("FINALE_VERSION_tablestardelivererSaisonstart-Saisonende-2023-2024.xlsx")).getPath();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            // Dateipfad und Workbook initialisieren

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                // Durchlaufen der Zeilen (Rows)

                if (row.getRowNum() == 0) continue;

                LocalDate saisonStart = SportSaisonService.convertDateCellToLocalDate(row.getCell(4));
                Assertions.assertNotNull(saisonStart);
                // überprüft nicht null ist
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void confirmSportSaisonTest() {
        boolean confirmed = sportSaisonService.confirm("basketball.damen.dbbl_pokal_1_runde");
        Assertions.assertTrue(confirmed);

        boolean confirmedMitRoute = sportSaisonService.confirm("basketball.damen.dbbl_pokal_2_runde");
        Assertions.assertTrue(confirmedMitRoute);

    }

    @Test
    public void getAllSportSaisonsTest() {

        SportSaison sportSaisonExpected = new SportSaison();

        sportSaisonExpected.setSportArt("BASKETBALL");
        sportSaisonExpected.setTeilNehmer("DAMEN");
        sportSaisonExpected.setEintragTableStarDeliverer("basketball.damen.dbbl_pokal_top4");
        sportSaisonExpected.setTableStarId(5571);
        sportSaisonExpected.setSaisonStart(LocalDate.of( 2024, 3,16));
        sportSaisonExpected.setSaisonEnde(LocalDate.of( 2024, 3,17));

        List<SportSaison> actual = sportSaisonService.getAllSportSaisons();
        // Assertions.assertTrue(bool);

           Assertions.assertNotNull(actual);
            Assertions.assertEquals(147, actual.size());

        for (SportSaison sportSaison: actual) {
            if(sportSaison.getEintragTableStarDeliverer().equals(sportSaisonExpected.getEintragTableStarDeliverer())){
                Assertions.assertEquals(sportSaison.getSaisonStart(), sportSaisonExpected.getSaisonStart());
                Assertions.assertEquals(sportSaison.getTableStarId(), sportSaisonExpected.getTableStarId());
                Assertions.assertEquals(sportSaison.getSportArt(), sportSaisonExpected.getSportArt());
                Assertions.assertEquals(sportSaison.getSaisonEnde(), sportSaisonExpected.getSaisonEnde());
                Assertions.assertEquals(sportSaison.getTeilNehmer(), sportSaisonExpected.getTeilNehmer());
            }

        }
    }

   public void writeExcelFile(){


    }

}
