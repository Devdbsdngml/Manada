package com.example.manada.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.manada.Fragment.HomeFragment;
import com.example.manada.Fragment.MenuFragment;
import com.example.manada.Fragment.UserFragment;
import com.example.manada.Fragment.MessageListFragment;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class FragmentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FragmentActivity";

    private ImageView btn_user_frag, btn_chat_frag, btn_home_frag, btn_menu_frag;
    private FragmentManager fragmentManager;
    private UserFragment userFragment;
    private MessageListFragment chatFragment;
    private HomeFragment homeFragment;
    private MenuFragment menuFragment;

    private Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        btn_user_frag = findViewById(R.id.btn_user_frag);
        btn_chat_frag = findViewById(R.id.btn_chat_frag);
        btn_home_frag = findViewById(R.id.btn_home_frag);
        btn_menu_frag = findViewById(R.id.btn_menu_frag);

        btn_user_frag.setOnClickListener(this);
        btn_chat_frag.setOnClickListener(this);
        btn_home_frag.setOnClickListener(this);
        btn_menu_frag.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        userFragment = new UserFragment();
        chatFragment = new MessageListFragment();
        homeFragment = new HomeFragment();
        menuFragment = new MenuFragment();

        // pushToken 서버에 전달
        passPushTokenToServer();

        // 첫번 째 프레그먼트
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Fragment_Container, homeFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        if(view == btn_user_frag) {
            if(!userFragment.isVisible()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment_Container, userFragment);
                fragmentTransaction.commit();
            }
        }
        else if(view == btn_chat_frag) {
            if(!chatFragment.isVisible()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment_Container, chatFragment);
                fragmentTransaction.commit();
            }
        }
        else if(view == btn_home_frag) {
            if(!homeFragment.isVisible()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment_Container, homeFragment);
                fragmentTransaction.commit();
            }
        }
        else if(view == btn_menu_frag) {
            if(!menuFragment.isVisible()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Fragment_Container, menuFragment);
                fragmentTransaction.commit();
            }
        }
    }

    private void passPushTokenToServer(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()) {
                            Log.w(TAG, task.getException());
                        }
                        String token = task.getResult().getToken();

                        map = new HashMap<>();
                        map.put("pushToken",token);
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .update(map);
                    }
                });
    }


    // 뒤로가기 두 번 종료
    long initTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis() - initTime > 3000) {
                showToast("종료하려면 한번 더 누르세요.");
                initTime = System.currentTimeMillis();
            }else {
                finishAffinity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
