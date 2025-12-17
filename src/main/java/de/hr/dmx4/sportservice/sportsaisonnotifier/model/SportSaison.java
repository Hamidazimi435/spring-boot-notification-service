package de.hr.dmx4.sportservice.sportsaisonnotifier.model;
import java.time.LocalDate;
public class SportSaison {


    private String sportArt;
    private String teilNehmer;
    private String eintragTableStarDeliverer;
    private long tableStarId;
    private LocalDate saisonStart;
    private LocalDate saisonEnde;
    private Boolean confirmed;
    private String status;
    private String additionalInfo;
    private String confirmationsLink;

    public SportSaison(String sportArt, String teilNehmer, String eintragTableStarDeliverer, long tableStarId, LocalDate saisonStart, LocalDate saisonEnde, Boolean status) {
        this.sportArt = sportArt;
        this.teilNehmer = teilNehmer;
        this.eintragTableStarDeliverer = eintragTableStarDeliverer;
        this.tableStarId = tableStarId;
        this.saisonStart = saisonStart;
        this.saisonEnde = saisonEnde;
        this.confirmed = status;
    }

    public SportSaison() {

    }

    public LocalDate getSaisonStart() {
        return saisonStart;
    }

    public void setSaisonStart(LocalDate saisonStart) {
        this.saisonStart = saisonStart;
    }

    public String getSportArt() {
        return sportArt;
    }

    public void setSportArt(String sportArt) {
        this.sportArt = sportArt;
    }

    public String getTeilNehmer() {
        return teilNehmer;
    }

    public void setTeilNehmer(String teilNehmer) {
        this.teilNehmer = teilNehmer;
    }

    public String getEintragTableStarDeliverer() {
        return eintragTableStarDeliverer;
    }

    public void setEintragTableStarDeliverer(String eintragTableStarDeliverer) {
        this.eintragTableStarDeliverer = eintragTableStarDeliverer;
    }

    public long getTableStarId() {
        return tableStarId;
    }

    public void setTableStarId(long tableStarId) {
        this.tableStarId = tableStarId;
    }

    public LocalDate getSaisonEnde() {
        return saisonEnde;
    }

    public void setSaisonEnde(LocalDate saisonEnde) {
        this.saisonEnde = saisonEnde;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getConfirmationsLink() {
        return confirmationsLink;
    }

    public void setConfirmationsLink(String confirmationsLink) {
        this.confirmationsLink = confirmationsLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "SportSaison{" +
                "sportArt='" + sportArt + '\'' +
                ", teilNehmer='" + teilNehmer + '\'' +
                ", eintragTableStarDeliverer='" + eintragTableStarDeliverer + '\'' +
                ", tableStarId=" + tableStarId +
                ", saisonStart=" + saisonStart +
                ", saisonEnde=" + saisonEnde +
                ", confirmed=" + confirmed +
                ", status='" + status + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", confirmationsLink='" + confirmationsLink + '\'' +
                '}';
    }
}
