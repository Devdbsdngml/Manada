package com.example.manada.Model;


import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public ChatModel() {
        super();
    }



    public Map<String, String> users = new HashMap<>(); //채팅방의 유저들
    public Map<String, Contents> contents = new HashMap<>();//채팅방의 대화내용

    public static class Contents {
        public String Uid;
        public String Name;
        public String Content;
//        public String ChatId;
//        public Map<String,Object> readUsers = new HashMap<>();
    }

}
