package com.android.spochansecondversion;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

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
    private String userPoints;//используем в рейтинге
    private String userCity;

    //правой кнопкой мыши, потом generate и дальше Constructor
    public User() {
    }

    public User(String email, String id, String firstName, String secondName, String sex, String daysBornDate, String monthBornDate, String yearBornDate, String avatarUrl, String userId, boolean isDirector, String userPoints, String userCity) {
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
        this.userPoints = userPoints;
        this.userCity = userCity;
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
}
