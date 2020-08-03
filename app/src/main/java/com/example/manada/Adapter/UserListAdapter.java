package com.example.manada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Model.ListModel;
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

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    public List<UserModel> userModels;
    private ListModel listModel;

    private String mycollege;
    private String personnel;
    private String yourcollege;
    private String gender;
    private String userName;

    public OnClickListener ulListener = null;


    public interface OnClickListener {
        void OnClick(View view, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.ulListener = listener;
    }

    public UserListAdapter() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String myuid = firebaseUser.getUid();
//        userName = firebaseUser.getDisplayName();

        userModels = new ArrayList<>();
        listModel = new ListModel();

        if(firebaseUser != null) {
            firebaseFirestore.collection("conditions").document(firebaseUser.getUid())
                    .get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null) {
                        if(documentSnapshot.exists()) {
                            mycollege = documentSnapshot.get("mycollege").toString();
                            personnel = documentSnapshot.get("personnel").toString();
                            yourcollege = documentSnapshot.get("yourcollege").toString();
                            gender = documentSnapshot.get("gender").toString();
                            userName = documentSnapshot.get("name").toString();

                            listModel.mycollege = mycollege;
                            listModel.personnel = personnel;
                            listModel.yourcollege = yourcollege;

                            if(gender.equals("남자")) {
                                listModel.gender = "여자";
                            }
                            else if(gender.equals("여자")) {
                                listModel.gender = "남자";
                            }

                            // 선택한 조건에따라 Userlist 목록 출력
                            firebaseFirestore.collection("conditions")
                                    .whereEqualTo("mycollege", listModel.yourcollege)
                                    .whereEqualTo("personnel", listModel.personnel)
                                    .whereEqualTo("yourcollege", listModel.mycollege)
                                    .whereEqualTo("gender", listModel.gender)
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
                        } else {
                            //
                        }
                    }


                }
            });
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userlist, parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((CustomViewHolder)holder).ul_rl_one_tv.setText(userName);
        ((CustomViewHolder)holder).ul_rl_two_tv.setText(userModels.get(position).name);

        if(gender.equals("남자")) {
            ((CustomViewHolder)holder).ul_rl_one_iv.setImageResource(R.drawable.ul_male);
            ((CustomViewHolder)holder).ul_rl_two_iv.setImageResource(R.drawable.ul_female);
        } else if(gender.equals("여자")) {
            ((CustomViewHolder)holder).ul_rl_one_iv.setImageResource(R.drawable.ul_female);
            ((CustomViewHolder)holder).ul_rl_two_iv.setImageResource(R.drawable.ul_male);
        }

//        ((CustomViewHolder)holder).userlist_tv_mycollege.setText(userModels.get(position).mycollege);
//        ((CustomViewHolder)holder).userlist_tv_yourcollege.setText(userModels.get(position).yourcollege);
//        ((CustomViewHolder)holder).userlist_tv_personnel.setText(userModels.get(position).personnel);
//        ((CustomViewHolder)holder).userlist_tv_gender.setText(userModels.get(position).gender);
            }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView ul_rl_one_iv;
        private ImageView ul_rl_two_iv;
        private TextView ul_rl_one_tv;
        private TextView ul_rl_two_tv;

        private CustomViewHolder(View view) {
            super(view);
            ul_rl_one_iv = view.findViewById(R.id.ul_rl_one_iv);
            ul_rl_one_tv = view.findViewById(R.id.ul_rl_one_tv);
            ul_rl_two_iv = view.findViewById(R.id.ul_rl_two_iv);
            ul_rl_two_tv = view.findViewById(R.id.ul_rl_two_tv);

            ul_rl_two_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (ulListener != null) {
                            ulListener.OnClick(view, pos);
                        }
                    }
                }
            });
        }
    }
}
