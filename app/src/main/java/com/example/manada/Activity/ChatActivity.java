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
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
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

    private String uid;
    private String name;
    private String gender;
    private ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();


        chat_btn_send.setOnClickListener(this);


        // recyclerview 등록
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        chatModel = new ChatModel();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                uid = documentSnapshot.get("uid").toString().trim();
                name = documentSnapshot.get("name").toString().trim();
                gender = documentSnapshot.get("gender").toString().trim();


                chatModel.chatUsers.put(uid, true);
            }
        });

    }

    private void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        chat_et_contents = findViewById(R.id.chat_et_contents);
        chat_btn_send = findViewById(R.id.chat_btn_send);
        recyclerView = findViewById(R.id.recyclerview);
    }

    @Override
    public void onClick(View view) {
        if (view == chat_btn_send) {
            chatModel.chatUsers.get(uid);
            Log.d(TAG, uid);
            Log.d(TAG, name);
            Log.d(TAG, gender);

        }
    }
}
