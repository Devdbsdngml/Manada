package com.example.manada.Model;


import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public ChatModel() {
        super();
    }

    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Comment> comment = new HashMap<>();

    public static class Comment {
        public String Uid;
        public String DestinationUid;
        public String name;
        public String contents;
    }

//    public String ChatId;

}
