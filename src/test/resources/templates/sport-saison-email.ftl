<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/styles.css">
    <title>Sport-Saison-Notification</title>
</head>

<body>

<div id="email-container" style="max-width: 600px; margin: 0 auto; padding: 20px; background-color: #dfe8eb;">
    <h2>Bevorstehende SportSaisons</h2>

    <#list sportSaisonList as sportSaison>
        <div class="main-title" style="border: 1px solid #ddd; padding: 10px; margin-bottom: 10px;">
            <p><strong class="sub-title">Sportart:</strong> ${sportSaison.sportArt}</p>
            <p><strong class="sub-title">Startdatum:</strong> ${sportSaison.saisonStart}</p>
            <p><strong class="sub-title">Bestätigungslink:</strong> <a href="${sportSaison.confirmationsLink}">${sportSaison.confirmationsLink}</a></p>
        </div>
    </#list>
</div>

</body>

</html>
