package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import de.hr.dmx4.sportservice.sportsaisonnotifier.config.EmailConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailConfigTest {

    @Autowired
    private EmailConfig emailConfig;

    @Test
    public void getEmailRecipients(){


        String[] actual = emailConfig.getEmailRecipients();
        String[] expected = {"jasan.chahrrour@hr.de","hamid.azimi@hr.de","laura.mai@hr.de"};

        // Überprüfung
        Assertions.assertArrayEquals(expected, actual);
    }
}
