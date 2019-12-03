package com.example.manada.Activity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerview;
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
    private String Name;
    private String gender;
    private String ChatId;

    private UserModel userModel;
    private ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();

        DestinationUid = getIntent().getStringExtra("DestinationUid");

        chat_btn_send.setOnClickListener(this);

        chatAdapter = new ChatAdapter(firebaseUser.getUid());
        recyclerview.setAdapter(chatAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));


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
        recyclerview = findViewById(R.id.chat_recyclerview);
    }

    @Override
    public void onClick(View view) {
        if (view == chat_btn_send) {

            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userModel = documentSnapshot.toObject(UserModel.class);
                    Uid = userModel.uid;
                    Name = userModel.name;
                    gender = userModel.gender;

                    chatModel = new ChatModel();

                    chatModel.users.put(Uid, true);
                    chatModel.users.put(DestinationUid, true);

                    if(ChatId == null) {
                        chat_btn_send.setEnabled(false);
                        firebaseFirestore.collection("chats").document()
                                //chatModel -> chatModel.users  12-03
                                .set(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkChatId();
                            }
                        });
                    } else {
                        ChatModel.Contents contents = new ChatModel.Contents();
                        contents.Uid = Uid;
                        contents.Name = Name;
                        contents.Content = chat_et_contents.getText().toString();
//                        contents.ChatId = ChatId;
                        firebaseFirestore.collection("chats").document(ChatId)
                                .collection("contents")
                                .add(contents)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(!chat_et_contents.toString().equals("") && chat_et_contents.length() != 0) {
                                            chat_et_contents.setText("");

                                        } else {
                                            chat_et_contents.setError("내용을 입력하세요");
                                        }
                                        StartListeningForMessages();
                                    }
                                });
                    }
                }
            });
        }
    }

    private void checkChatId() {
        firebaseFirestore.collection("chats").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ChatModel chatModel = document.toObject(ChatModel.class);
                                if(chatModel.users.containsKey(DestinationUid) && chatModel.users.size() == 2) {
                                    chat_btn_send.setEnabled(true);
                                    ChatId = document.getId();

                                    Map<String, Object> chatIds = new HashMap<>();
                                    chatIds.put("ChatId", ChatId);
                                    firebaseFirestore.collection("chats")
                                            .document(ChatId).update(chatIds);


                                    Log.d(TAG, "chatId : " + ChatId);
                                } else {
                                    Log.d(TAG, "chatModel.users failed");
                                }
                            }
                        } else {
                            Log.d(TAG, "task failed");
                        }
                    }
                });
    }

    private void StartListeningForMessages() {
        firebaseFirestore.collection("chats").document(ChatId)
                .collection("contents")
                .orderBy("dateSent")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            Log.d(TAG, "error in ChatActivity");
                        } else {
                            List<ChatModel.Contents> contents = queryDocumentSnapshots.toObjects(ChatModel.Contents.class);

                            chatAdapter.setData(contents);
                            recyclerview.smoothScrollToPosition(chatAdapter.getItemCount()-1);
                        }
                    }
                });
    }
}
