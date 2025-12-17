
# spring-boot-notification-service

# Sport-saison-Notifier
## Beschreibung:
Die Backendanwendung ist ein Cloud-Service zur automatischen Benachrichtigung von Datenmanagern und Sportredakteuren über den Beginn neuer Sport-Saisons.

## Funktionalitäten:
- Automatische Benachrichtigung bei Beginn neuer Sportsaisons
- Konfigurierbare Benachrichtigungszeitpunkte für maximale Flexibilität
- Vermeidung redundanter Benachrichtigungen durch individuelle Bestätigungslinks
- Einfaches Hochladen und Abfragen von Tabelleninhalten für eine effiziente Datenverwaltung

### HTTP REST-Endpunkte

#### Basis URL

> **http://localhost:8084/api/sport/saisons/notifier**


### 1. HTTP GET sendNotificationsForAllSportSaisons
Dieser Endpunkt wird verwendet, um Benachrichtigungen für alle bevorstehenden Sport-Saisons zu senden. Es ruft den Service auf, der die Benachrichtigungen an die entsprechenden Empfänger verschickt. Die Anzahl der erfolgreich versendeten E-Mails wird als Antwort zurückgegeben.
####  URL
> http://localhost:8084/api/sport/saisons/notifier/
### Erfolgsantwort
````text
E-Mails erfolgreich versendet. Anzahl der Ligen: 1
````

### 2. HTTP GET getExcelContent
Dieser Endpunkt wird verwendet, um den Inhalt einer Excel-Datei abzurufen oder eine Liste von Sport-Saison-Objekten im JSON-Format zurückzugeben.
#### Parameter

> type (optional): Bestimmt das Format des zurückgegebenen Inhalts. Standardmäßig ist der Wert "json". Mögliche Werte sind "xls" und "json".
####  URL
> http://localhost:8084/api/sport/saisons/notifier/content
#### Response Beispiele
````json
[
    {
        "sportArt": "BASKETBALL",
        "teilNehmer": "DAMEN",
        "eintragTableStarDeliverer": "basketball.damen.2_bundesliga_aufstiegsrunde",
        "tableStarId": 5330,
        "saisonStart": "2023-04-08",
        "saisonEnde": "2023-05-14",
        "confirmed": null,
        "status": "FERTIG",
        "additionalInfo": "Saison 2022/2023",
        "confirmationsLink": null
    },
    {
        "sportArt": "BASKETBALL",
        "teilNehmer": "DAMEN",
        "eintragTableStarDeliverer": "basketball.damen.2_bundesliga_nord",
        "tableStarId": 5541,
        "saisonStart": "2023-09-30",
        "saisonEnde": "2024-03-23",
        "confirmed": null,
        "status": "AKTIV",
        "additionalInfo": "",
        "confirmationsLink": null
    },
    {
        "sportArt": "BASKETBALL",
        "teilNehmer": "DAMEN",
        "eintragTableStarDeliverer": "basketball.damen.2_bundesliga_nord_abstiegsrunde",
        "tableStarId": 5331,
        "saisonStart": "2022-10-22",
        "saisonEnde": "2023-05-06",
        "confirmed": null,
        "status": "FERTIG",
        "additionalInfo": "Saison 2022/2023",
        "confirmationsLink": null
    }....]
````


### 3. HTTP POST uploadExcelFile
Dieser Endpunkt wird verwendet, um eine Excel-Datei hochzuladen und in die Google Cloud Platform (GCP) zu speichern.
####  URL
> http://localhost:8084/api/sport/saisons/notifier/content
#### Parameter

> file: Die zu hochladende Excel-Datei als MultipartFile
#### Response Beispiele
````text
Fehlermeldung
````

### 4. HTTP GET confirm
Endpunkt für die Bestätigung eines Eintrags in der Sport-Saison.
####  URL
> http://localhost:8084/api/sport/saisons/notifier/confirm?entry=basketball.damen.dbbl_pokal_top4
#### Parameter

> "entry", der den Wert eintragTableStarDeliverer enthält.
#### Response Beispiele
````text
Vielen Dank! Ihre Bestätigung für TableStarDelivererEntry (basketball.damen.dbbl_pokal_top4) wurde erfolgreich durchgeführt.
````

### Tests
Die Integrationstests werden mit der Testenv abgefahren
Vorraussetzung ist das Docker-(Desktop) mit der Kubernetes Engine gestartet ist.

