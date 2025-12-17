package de.hr.dmx4.sportservice.sportsaisonnotifier.config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter                 // Lombok macht methode selbe
public class EmailConfig {

    // Empfänger-E-Mail-Adressen, aus der Anwendungsproperties-Datei (application.properties) geladen
    @Value("${email.recipients}")
    private String emailRecipients;

    // Betreff der E-Mails, aus der Anwendungsproperties-Datei geladen
    @Value("${email.subject}")
    private String emailSubject;

    // Schwellenwert für Tage, aus der Anwendungsproperties-Datei geladen
    @Value("${email.thresholdDays}")
    private int emailThresholdDays;

    // E-Mail-Host, aus der Anwendungsproperties-Datei geladen
    @Value("${email.host}")
    private String emailHost;

    @Value("${email.sender}")
    private String emailSender;

    /*
      Gibt die Empfänger-E-Mail-Adressen als Array von Strings zurück,
      nachdem sie aus der kommaseparierten Liste extrahiert wurden.
      @return Ein Array von Empfänger-E-Mail-Adressen.
    */

    public String[] getEmailRecipients(){
        String[] arrayString =  emailRecipients.split(",");
        return arrayString;
    }
}

