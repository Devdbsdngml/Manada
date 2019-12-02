package com.example.manada.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Adapter.ChatAdapter;
import com.example.manada.Model.ChatModel;
import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;


    private EditText chat_et_contents;
    private Button chat_btn_send;

    private String Uid;
    private String DestinationUid;
    private String name;
    private String gender;
    private String chatId;

    private UserModel userModel;
    private ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();

        DestinationUid = getIntent().getStringExtra("DestinationUid");

        chat_btn_send.setOnClickListener(this);

        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatModel = new ChatModel();
    }

    private void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chat_et_contents = findViewById(R.id.chat_et_contents);
        chat_btn_send = findViewById(R.id.chat_btn_send);
        recyclerView = findViewById(R.id.chat_recyclerview);
    }

    @Override
    public void onClick(View view) {
        if (view == chat_btn_send) {

            FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userModel = documentSnapshot.toObject(UserModel.class);
                    Uid = userModel.uid;
                    name = userModel.name;
                    gender = userModel.gender;

//                    ChatModel chatModel = new ChatModel();
                    chatModel.users.put(Uid, true);
                    chatModel.users.put(DestinationUid, true);

                    if(chatId == null) {
                        chat_btn_send.setEnabled(false);
                        databaseReference.child("chats").push().setValue(chatModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        checkChatId();
                                    }
                                });
                    } else {
                        ChatModel.Comment comment = new ChatModel.Comment();
                        comment.Uid = Uid;
                        comment.DestinationUid = DestinationUid;
                        comment.name = name;
                        comment.contents = chat_et_contents.getText().toString();

                        databaseReference.child("chats").child(chatId).child("comments")
                                .push().setValue(comment)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(chat_et_contents.length() != 0) {
                                            chat_et_contents.setText("");
                                        } else {
                                            chat_et_contents.setError("내용을 입력해주세요");
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    private void checkChatId() {
        databaseReference.child("chats").orderByChild("users/"+Uid).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ChatModel chatModel = item.getValue(ChatModel.class);
                            if (chatModel.users.containsKey(DestinationUid) && chatModel.users.size() == 2) {
                                chatId = item.getKey();
                                chat_btn_send.setEnabled(true);

                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(chatAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
