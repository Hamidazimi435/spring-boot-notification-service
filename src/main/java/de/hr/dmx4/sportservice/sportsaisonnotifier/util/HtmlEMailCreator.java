package de.hr.dmx4.sportservice.sportsaisonnotifier.util;

import de.hr.dmx4.sportservice.sportsaisonnotifier.model.SportSaison;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HtmlEMailCreator {

    // Qualifizierter Name für die FreeMarker-Konfiguration
    @Qualifier("getFreemarkerConfiguration")
    private final Configuration freemarkerConfig;


    @Autowired
    public HtmlEMailCreator(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }


    /*
      Erstellt den HTML-Inhalt für die E-Mail-Vorlage unter Verwendung von FreeMarker.
      @param sportSaisonList - die Liste von SportSaison-Objekten, die in die Vorlage eingefügt werden sollen.
      @return Der erstellte HTML-Inhalt als String.
*/
    public String createHtmlContent(List<SportSaison> sportSaisonList) {
        try {
            Template template = freemarkerConfig.getTemplate("sport-saison-email.ftl");

            Map<String, Object> model = new HashMap<>();
            model.put("sportSaisonList", sportSaisonList);

            StringWriter writer = new StringWriter();
            template.process(model, writer);

            return writer.toString();
        } catch (IOException e) {
            log.error("Fehler beim Erstellen des HTML-Inhalts: {}", e.getMessage());
            return "";
        } catch (Exception e) {
            log.error("Allgemeiner Fehler: {}", e.getMessage());
            return "";
        }
    }
}
