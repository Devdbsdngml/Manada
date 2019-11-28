package com.example.manada.Model;

public class UserModel {

    public UserModel() {
        super();
    }

    public String uid;
    public String name;
    public String gender;
    public String mycollege;
    public String personnel;
    public String yourcollege;

    public String pushToken;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMycollege() {
        return mycollege;
    }

    public void setMycollege(String mycollege) {
        this.mycollege = mycollege;
    }

    public String getPersonnel() {
        return personnel;
    }

    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    public String getYourcollege() {
        return yourcollege;
    }

    public void setYourcollege(String yourcollege) {
        this.yourcollege = yourcollege;
    }
}
