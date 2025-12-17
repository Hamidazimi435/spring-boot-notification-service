package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import de.hr.dmx4.sportservice.sportsaisonnotifier.config.EmailConfig;
import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import de.hr.dmx4.sportservice.sportsaisonnotifier.util.HtmlEMailCreator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service    /*eine Klasse als Service-Komponente zu kennzeichnen*/
@Slf4j /*die automatisch Logger-Felder in Java-Klassen generiert*/
public class EmailService {

    private final EmailConfig emailConfigSportSaison;
    private final SportSaisonService sportSaisonService;
    private final JavaMailSender emailSender;
    private final HtmlEMailCreator htmlEMailCreator;
    @Autowired /*Um automatisch Abhängigkeiten zwischen verschiedenen Komponenten einer Anwendung zu injizieren*/
    public EmailService(EmailConfig emailConfigSportSaison,
                        SportSaisonService sportSaisonService,
                        JavaMailSender emailSender, HtmlEMailCreator htmlEMailCreator) {
        this.emailConfigSportSaison = emailConfigSportSaison;
        this.sportSaisonService = sportSaisonService;
        this.emailSender = emailSender;
        this.htmlEMailCreator = htmlEMailCreator;
    }
    /*
    Diese Methode sendet Benachrichtigungen für bevorstehende Sport-Saisons.
      @return Die Anzahl der bevorstehenden Sport-Saison Liga-Starts.
      @throws Exception, wenn ein Fehler beim Senden auftritt.
     */
    public int sendNotificationForUpcomingSportSaisons() {
        List<SportSaison> allSportSaison = sportSaisonService.getAllSportSaisons();

        log.info("es gibt Eintreage: {}", allSportSaison.size());
        LocalDate currentDate = LocalDate.now();
        List<SportSaison> upcomingSportSaisons = new ArrayList<>();

        for (SportSaison sportSaison : allSportSaison) {
            if (sportSaison != null) {
                LocalDate saisonStart = sportSaison.getSaisonStart();

                if (saisonStart != null) {
                    long daysUntilStart = ChronoUnit.DAYS.between(currentDate,
                            saisonStart);
                    log.info("Saisonstart: {}", sportSaison.getSaisonStart());
                    if (daysUntilStart <= emailConfigSportSaison.getEmailThresholdDays(
                    ) && daysUntilStart >= 0) {
                        sportSaison.setConfirmationsLink(buildConfirmationLink(
                                sportSaison.getEintragTableStarDeliverer()));
                        upcomingSportSaisons.add(sportSaison);
                    }
                }
            }
        }
        String htmlContent = htmlEMailCreator.createHtmlContent(upcomingSportSaisons);

        // Erstelle eine MimeMessage und setze den HTML-Inhalt
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfigSportSaison.getEmailSender());
            helper.setTo(emailConfigSportSaison.getEmailRecipients());
            helper.setSubject(emailConfigSportSaison.getEmailSubject());
            helper.setText(htmlContent, true);

            // Sende die E-Mail
            emailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            // Fehlerbehandlung
            log.error("Fehler beim Senden der E-Mail: {}", e.getMessage());
        }

        return upcomingSportSaisons.size();
    }

    /*
   Erstellt einen Bestätigungslink für einen Eintrag in der Sport-Saison.
   @param eintragTableStarDeliverer - der Eintrag, für den der Bestätigungslink erstellt wird.
   @return Der Bestätigungslink.
 */

    public String buildConfirmationLink(String eintragTableStarDeliverer) {
        // Überprüfen, ob der Eintrag mit einer Route beginnt
        boolean containsRoute = eintragTableStarDeliverer.startsWith("#");

        // Falls eine Route vorhanden ist, entferne das "#" Zeichen
        if (containsRoute) {
            eintragTableStarDeliverer = eintragTableStarDeliverer.substring(1);
        }
        String link = emailConfigSportSaison.getEmailHost();
        if (emailConfigSportSaison.getEmailHost().contains("localhost")) {
            link = link + ":8084";
        }
        // Bestätigungslink erstellen und zurückgeben
        return link + "/api/sport/saisons/notifier/confirm?entry=" + eintragTableStarDeliverer;


    }

}


