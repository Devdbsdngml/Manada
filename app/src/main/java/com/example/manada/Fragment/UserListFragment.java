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
import com.example.manada.Adapter.UserListAdapter;
import com.example.manada.Model.NotificationModel;
import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class UserListFragment extends Fragment {

    private static final String TAG = "UserListFragment";

    private View view;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private String Uid;
    private String DestinationUid;
    private UserModel destinationUserModel;


    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_userlist, container, false);
        initView();

        userListAdapter = new UserListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userListAdapter);

        destinationUserModel = new UserModel();

        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userListAdapter.setOnClickListener(new UserListAdapter.OnClickListener() {
            @Override
            public void OnClick(View view, int position) {

                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("DestinationUid", userListAdapter.userModels.get(position).uid);

                DestinationUid = userListAdapter.userModels.get(position).uid;
                FirebaseFirestore.getInstance().collection("users")
                        .document(DestinationUid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                destinationUserModel = documentSnapshot.toObject(UserModel.class);
                                SendFCM();
                                showToast("신청!");
                            }
                        });
//                System.out.println(DestinationUid);
                startActivity(intent);
            }
        });
        return view;
    }

    private void SendFCM() {
        // FCM
        Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();

        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.data.title = getString(R.string.app_name);
        notificationModel.data.text = getString(R.string.request);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyBPTgIWlrI6PN8XMc8NXr78Vwkp2M70Br4")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    private void initView() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.userlist_frag_recyclerview);
    }

    public void showToast(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
