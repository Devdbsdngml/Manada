package com.example.manada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Model.ChatModel;
import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    public List<UserModel> userModels;
    private List<ChatModel> chatModels;
    private List<String> keys;
    private ArrayList<String> destinationUsers;

    private String uid;
    private String gender;

    public OnClickListener clListener = null;


    public interface OnClickListener {
        void OnClick(View view, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.clListener = listener;
    }


    public ChatListAdapter() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        uid = firebaseUser.getUid();

        userModels = new ArrayList<>();
        chatModels = new ArrayList<>();
        keys = new ArrayList<>();
        destinationUsers = new ArrayList<>();

        firebaseFirestore.collection("chats").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        chatModels.clear();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                chatModels.add(document.toObject(ChatModel.class));
                                keys.add(document.getId());
                            }
                            notifyDataSetChanged();
                        }
                    }
                });

        firebaseFirestore.collection("conditions").document(uid)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        gender = documentSnapshot.get("gender").toString();

                        firebaseFirestore.collection("conditions")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                userModels.clear();
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                        UserModel userModel = document.toObject(UserModel.class);
                                        if(userModel.uid.equals(uid)) {
                                            continue;
                                        }
                                        userModels.add(userModel);
                                    }
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatlist, parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String destinationUid = null;

        for(String user : chatModels.get(position).users.keySet()) {
            if(user.equals(uid)) {
                destinationUid = user;
                destinationUsers.add(destinationUid);
            }
        }

        if(gender.equals("남자")) {
            ((CustomViewHolder)holder).chatlist_one_iv.setImageResource(R.drawable.ul_female);
            ((CustomViewHolder)holder).chatlist_two_iv.setImageResource(R.drawable.ul_male);
        } else if(gender.equals("여자")) {
            ((CustomViewHolder)holder).chatlist_one_iv.setImageResource(R.drawable.ul_male);
            ((CustomViewHolder)holder).chatlist_two_iv.setImageResource(R.drawable.ul_female);
        }


    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView chatlist_one_iv;
        private ImageView chatlist_two_iv;
        private Button chatlist_btn;

        private CustomViewHolder(@NonNull View view) {
            super(view);
            chatlist_one_iv = view.findViewById(R.id.chatlist_one_iv);
            chatlist_two_iv = view.findViewById(R.id.chatlist_two_iv);
            chatlist_btn = view.findViewById(R.id.chatlist_btn);

            chatlist_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (clListener != null) {
                            clListener.OnClick(view, pos);
                        }
                    }
                }
            });
        }
    }
}
