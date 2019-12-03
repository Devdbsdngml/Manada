package com.example.manada.Model;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatModel {

    public ChatModel() {
        super();
    }


    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Contents> contents = new HashMap<>();

    public static class Contents {
        public String Uid;
        public String Name;
        public String Content;

        @ServerTimestamp
        public Date dateSent;

        public Date getDateSent() {
            return dateSent;
        }

        public void setDateSent(Date dateSent) {
            this.dateSent = dateSent;
        }
//        public Map<String,Object> readUsers = new HashMap<>();
    }

}
