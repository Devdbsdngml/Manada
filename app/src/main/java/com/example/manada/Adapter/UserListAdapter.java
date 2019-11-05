package com.example.manada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private List<UserModel> userModels;
    private OnClickListener mListener = null;

    public interface OnClickListener {
        void OnClick(View view, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public UserListAdapter() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String myuid = firebaseUser.getUid();

        userModels = new ArrayList<>();

        firebaseFirestore.collection("conditions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                userModels.clear();
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        UserModel userModel = document.toObject(UserModel.class);
                        if(userModel.uid.equals(myuid)) {
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userlist,parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolder)holder).userlist_tv_name.setText(userModels.get(position).name);
        ((CustomViewHolder)holder).userlist_tv_mycollege.setText(userModels.get(position).mycollege);
        ((CustomViewHolder)holder).userlist_tv_yourcollege.setText(userModels.get(position).yourcollege);
        ((CustomViewHolder)holder).userlist_tv_personnel.setText(userModels.get(position).personnel);
        ((CustomViewHolder)holder).userlist_tv_gender.setText(userModels.get(position).gender);

    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView userlist_tv_name;
        public TextView userlist_tv_mycollege;
        public TextView userlist_tv_yourcollege;
        public TextView userlist_tv_personnel;
        public TextView userlist_tv_gender;
        public Button userlist_btn_request;

        public CustomViewHolder(View view) {
            super(view);
            userlist_tv_name = view.findViewById(R.id.userlist_tv_name);
            userlist_tv_mycollege = view.findViewById(R.id.userlist_tv_mycollege);
            userlist_tv_yourcollege = view.findViewById(R.id.userlist_tv_yourcollege);
            userlist_tv_personnel = view.findViewById(R.id.userlist_tv_personnel);
            userlist_tv_gender = view.findViewById(R.id.userlist_tv_gender);
            userlist_btn_request = view.findViewById(R.id.userlist_btn_request);

            userlist_btn_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.OnClick(view, pos);
                        }
                    }
                }
            });
        }
    }
}
