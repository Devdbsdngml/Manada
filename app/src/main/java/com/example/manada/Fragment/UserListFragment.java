package com.example.manada.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manada.Adapter.UserListAdapter;
import com.example.manada.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserListFragment extends Fragment {

    private static final String TAG = "UserListFragment";

    private View view;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;


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

        return view;
    }

    private void initView() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.userlist_frag_recyclerview);
    }

}
