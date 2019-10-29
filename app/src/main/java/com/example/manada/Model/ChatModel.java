package com.example.manada.Model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public ChatModel() {
        super();
    }

    public Map<String, Boolean> chatUsers = new HashMap<>();
    public String name;
    public String gender;


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
}
