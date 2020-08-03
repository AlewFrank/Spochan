package com.android.spochansecondversion;

public class Competition {

    private String competitionTitle;
    private String competitionData;
    private String competitionLocation;
    private String competitionAddress;
    private String competitionDescription;
    private String competitionImageUrl;


    //правой кнопкой мыши, потом generate и дальше Constructor, а ниже это создавалось с помощью Getter and Setter
    public Competition() {
    }

    public Competition(String competitionTitle, String competitionData, String competitionLocation, String competitionAddress, String competitionDescription, String competitionImageUrl) {
        this.competitionTitle = competitionTitle;
        this.competitionData = competitionData;
        this.competitionLocation = competitionLocation;
        this.competitionAddress = competitionAddress;
        this.competitionDescription = competitionDescription;
        this.competitionImageUrl = competitionImageUrl;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    public String getCompetitionData() {
        return competitionData;
    }

    public void setCompetitionData(String competitionData) {
        this.competitionData = competitionData;
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
}
