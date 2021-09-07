package org.admin.spochansecondversion.Competition;

public class Competition {

    private String competitionId;
    private String competitionTitle;
    private String competitionLocation;
    private String competitionAddress;
    private String competitionDescription;
    private String competitionImageUrl;
    private String daysCompetitionDate;
    private String monthCompetitionDate;
    private String yearCompetitionDate;
    private boolean isCompetitionRegistrationActive;//переменная, которая используется для того, чтобы отображать кнопку, позволяющую регистрироваться на чемпионат


    //правой кнопкой мыши, потом generate и дальше Constructor, а ниже это создавалось с помощью Getter and Setter
    public Competition() {
    }

    public Competition(String competitionId, String competitionTitle, String competitionLocation, String competitionAddress, String competitionDescription, String competitionImageUrl, String daysCompetitionDate, String monthCompetitionDate, String yearCompetitionDate, boolean isCompetitionRegistrationActive) {
        this.competitionId = competitionId;
        this.competitionTitle = competitionTitle;
        this.competitionLocation = competitionLocation;
        this.competitionAddress = competitionAddress;
        this.competitionDescription = competitionDescription;
        this.competitionImageUrl = competitionImageUrl;
        this.daysCompetitionDate = daysCompetitionDate;
        this.monthCompetitionDate = monthCompetitionDate;
        this.yearCompetitionDate = yearCompetitionDate;
        this.isCompetitionRegistrationActive = isCompetitionRegistrationActive;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    public String getCompetitionLocation() {
        return competitionLocation;
    }

    public void setCompetitionLocation(String competitionLocation) {
        this.competitionLocation = competitionLocation;
    }

    public String getCompetitionDescription() {
        return competitionDescription;
    }

    public void setCompetitionDescription(String competitionDescription) {
        this.competitionDescription = competitionDescription;
    }

    public String getCompetitionImageUrl() {
        return competitionImageUrl;
    }

    public void setCompetitionImageUrl(String competitionImageUrl) {
        this.competitionImageUrl = competitionImageUrl;
    }

    public String getCompetitionAddress() {
        return competitionAddress;
    }

    public void setCompetitionAddress(String competitionAddress) {
        this.competitionAddress = competitionAddress;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getDaysCompetitionDate() {
        return daysCompetitionDate;
    }

    public void setDaysCompetitionDate(String daysCompetitionDate) {
        this.daysCompetitionDate = daysCompetitionDate;
    }

    public String getMonthCompetitionDate() {
        return monthCompetitionDate;
    }

    public void setMonthCompetitionDate(String monthCompetitionDate) {
        this.monthCompetitionDate = monthCompetitionDate;
    }

    public String getYearCompetitionDate() {
        return yearCompetitionDate;
    }

    public void setYearCompetitionDate(String yearCompetitionDate) {
        this.yearCompetitionDate = yearCompetitionDate;
    }

    public boolean isCompetitionRegistrationActive() {
        return isCompetitionRegistrationActive;
    }

    public void setCompetitionRegistrationActive(boolean competitionRegistrationActive) {
        isCompetitionRegistrationActive = competitionRegistrationActive;
    }
}
