package de.hr.dmx4.sportservice.sportsaisonnotifier.email;

import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import de.hr.dmx4.sportservice.sportsaisonnotifier.util.HtmlEMailCreator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest
public class HtmlEmailCreatorTest {

    @Autowired
    private HtmlEMailCreator htmlEmailCreator;

    @Test
    public void testCreateHtmlEmail() throws IOException {
        //Objekt 1
        SportSaison sportSaison = new SportSaison("Basketball", "Damen", "basketball.damen.dbbl_pokal_top4=5571", 5571, LocalDate.of(2024, Month.MARCH, 16), LocalDate.of(2024, Month.MARCH, 17), true);
        sportSaison.setConfirmationsLink("http://localhost:8080/api/sport/saisons/notifier/5571");
        //Objekt 2
        SportSaison sportSaison1 = new SportSaison("Basketball", "Maenner", "basketball.maenner.bbl_pokal_top4=5570", 5570, LocalDate.of(2024, Month.FEBRUARY, 17), LocalDate.of(2024, Month.APRIL, 22), true);
        sportSaison1.setConfirmationsLink("http://localhost:8080/api/sport/saisons/notifier/5570");

        List<SportSaison> sportSaisonList = new ArrayList<>();
        sportSaisonList.add(sportSaison);
        sportSaisonList.add(sportSaison1);

        String html = this.htmlEmailCreator.createHtmlContent(sportSaisonList);

        Assertions.assertNotNull(html);
        String expectedHtml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("html/test-email.html")), StandardCharsets.UTF_8);

        Assertions.assertEquals(StringUtils.deleteWhitespace(expectedHtml), StringUtils.deleteWhitespace(html));


        System.out.println(html.replace(" ",""));
    }



}
