package com.example.manada.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.manada.Activity.MainActivity;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class MenuFragment extends Fragment implements View.OnClickListener{

    public MenuFragment() {
        // Required empty public constructor
    }

    private Button btn_frag_menu_logout, btn_frag_menu_signout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btn_frag_menu_logout.setOnClickListener(this);
        btn_frag_menu_signout.setOnClickListener(this);


        return view;
    }

    private void initView() {
        btn_frag_menu_logout = view.findViewById(R.id.btn_frag_menu_logout);
        btn_frag_menu_signout = view.findViewById(R.id.btn_frag_menu_signout);
    }
    public void onClick(View view) {
        if(view == btn_frag_menu_logout) {
            firebaseAuth.signOut();
            showToast("로그아웃 완료");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        if(view == btn_frag_menu_signout) {
            firebaseUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                showToast("회원탈퇴 완료");
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
