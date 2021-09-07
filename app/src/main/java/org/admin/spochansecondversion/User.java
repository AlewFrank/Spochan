package org.admin.spochansecondversion;

public class User {

    private String email;
    private String id;
    private String firstName;
    private String secondName;
    private String sex;
    private String daysBornDate;
    private String monthBornDate;
    private String yearBornDate;
    private String avatarUrl;
    private String userId;
    private boolean isDirector;//отвечает за то администратор человек или спортсмен
    private boolean isCoach;//отвечает за то тренер человек или нет
    private String userPoints;//используем в рейтинге
    private String userCity;
    private String coachId;
    private String userGroup;
    private String userClub;
    private String userCertification;

    private boolean hasPayed;//используем для задания того пришел человек или нет
    private boolean hasComeOn;

    //правой кнопкой мыши, потом generate и дальше Constructor
    public User() {
    }

    public User(String email, String id, String firstName, String secondName, String sex, String daysBornDate, String monthBornDate, String yearBornDate, String avatarUrl, String userId, boolean isDirector, boolean isCoach, String userPoints, String userCity, String coachId, String userGroup, String userClub, String userCertification, boolean hasPayed, boolean hasComeOn) {
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.sex = sex;
        this.daysBornDate = daysBornDate;
        this.monthBornDate = monthBornDate;
        this.yearBornDate = yearBornDate;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
        this.isDirector = isDirector;
        this.isCoach = isCoach;
        this.userPoints = userPoints;
        this.userCity = userCity;
        this.coachId = coachId;
        this.userGroup = userGroup;
        this.userClub = userClub;
        this.userCertification = userCertification;
        this.hasPayed = hasPayed;
        this.hasComeOn = hasComeOn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDaysBornDate() {
        return daysBornDate;
    }

    public void setDaysBornDate(String daysBornDate) {
        this.daysBornDate = daysBornDate;
    }

    public String getMonthBornDate() {
        return monthBornDate;
    }

    public void setMonthBornDate(String monthBornDate) {
        this.monthBornDate = monthBornDate;
    }

    public String getYearBornDate() {
        return yearBornDate;
    }

    public void setYearBornDate(String yearBornDate) {
        this.yearBornDate = yearBornDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDirector() {
        return isDirector;
    }

    public void setDirector(boolean director) {
        isDirector = director;
    }

    public String getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(String userPoints) {
        this.userPoints = userPoints;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserClub() {
        return userClub;
    }

    public void setUserClub(String userClub) {
        this.userClub = userClub;
    }

    public String getUserCertification() {
        return userCertification;
    }

    public void setUserCertification(String userCertification) {
        this.userCertification = userCertification;
    }

    public boolean isHasPayed() {
        return hasPayed;
    }

    public void setHasPayed(boolean hasPayed) {
        this.hasPayed = hasPayed;
    }

    public boolean isHasComeOn() {
        return hasComeOn;
    }

    public void setHasComeOn(boolean hasComeOn) {
        this.hasComeOn = hasComeOn;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }
}
