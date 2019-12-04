package com.example.manada.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Activity.ChatActivity;
import com.example.manada.Adapter.ChatListAdapter;
import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";

    private View view;
    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;

    private UserModel destinationUserModel;
    private String DestinationUid;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatlist, container, false);
        initView();

        destinationUserModel = new UserModel();

        chatListAdapter = new ChatListAdapter();
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatListAdapter.setOnClickListener(new ChatListAdapter.OnClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("DestinationUid", chatListAdapter.userModels.get(position).uid);

                DestinationUid = chatListAdapter.userModels.get(position).uid;
                FirebaseFirestore.getInstance().collection("users")
                        .document(DestinationUid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                destinationUserModel = documentSnapshot.toObject(UserModel.class);
                                showToast("신청완료!");
                            }
                        });
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.chatlist_frag_recyclerview);
    }

    public void showToast(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
