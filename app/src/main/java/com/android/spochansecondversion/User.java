package com.android.spochansecondversion;

public class User {

    private String email;
    private String id;
    private String firstName;
    private String secondName;
    private String sex;
    private String bornDate;

    //правой кнопкой мыши, потом generate и дальше Constructor
    public User() {
    }

    public User(String email, String id, String firstName, String secondName, String sex, String bornDate) {
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.sex = sex;
        this.bornDate = bornDate;
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

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }
}
