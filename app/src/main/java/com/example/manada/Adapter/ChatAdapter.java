package com.example.manada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Model.ChatModel;
import com.example.manada.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int MY_MESSAGE = 1;
    private static int YOUR_MESSAGE = 2;

    private String uid;

    private List<ChatModel.Contents> contents;


    public ChatAdapter(String uid) {
        this.uid = uid;
        contents = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;

        if(viewType == MY_MESSAGE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_my_message, viewGroup, false);
            return new MyMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_your_message, viewGroup, false);
            return new MessageViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(contents.get(position).Uid.equals(uid))
            return MY_MESSAGE;
        else
            return YOUR_MESSAGE;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 1) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return 0;
//        return comments.size();
    }


    private class MyMessageViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView my_message;

        public MyMessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            my_message = view.findViewById(R.id.my_message);
        }
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView your_image;
        private TextView your_message;

        public MessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            your_image = view.findViewById(R.id.your_image);
            your_message = view.findViewById(R.id.your_message);
        }
    }
}
