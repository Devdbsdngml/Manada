package com.example.manada.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
