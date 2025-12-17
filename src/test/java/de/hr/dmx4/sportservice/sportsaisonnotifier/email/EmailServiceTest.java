package de.hr.dmx4.sportservice.sportsaisonnotifier.email;

import de.hr.dmx4.sportservice.sportsaisonnotifier.config.EmailConfig;
import de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;
    @Autowired
    EmailConfig emailConfigSportSaison;

    @Test
    void buildConfirmationLinkTest() {
        String actual = emailService.buildConfirmationLink("basketball.damen.dbbl_pokal_top4");
        String expected = emailConfigSportSaison.getEmailHost() + ":8080/api/sport/saisons/notifier/confirm?entry=basketball.damen.dbbl_pokal_top4";
        Assertions.assertEquals(expected, actual);

        String actual1 = emailService.buildConfirmationLink("basketball.maenner.bbl_pokal_top4");
        String expected1 = emailConfigSportSaison.getEmailHost() + ":8080/api/sport/saisons/notifier/confirm?entry=basketball.maenner.bbl_pokal_top4";
        Assertions.assertEquals(expected1, actual1);
    }

    @Test
    public void sendNotificationForUpcomingSportSaisonsTest() {
        try {
            int actual = emailService.sendNotificationForUpcomingSportSaisons();
            int expected = 2;
            System.out.println("Actual Liga Saison-Starts: " + actual);
            Assertions.assertEquals(expected, actual);

        } catch (Exception e) {
            Assertions.fail(e);
        }

    }


}
