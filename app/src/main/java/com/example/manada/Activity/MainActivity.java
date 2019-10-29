package com.example.manada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// test

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btn_signup;
    private TextView tv_login;
    private long initTime;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btn_signup.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        checkDocUsers();

    }

    public void onClick(View view) {
        if(view == btn_signup) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        }
        if(view == tv_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        }
    }
    private void initView() {
        btn_signup = (Button) findViewById(R.id.btn_signup);
        tv_login = (TextView)findViewById(R.id.tv_login);
    }
    private void checkDocUsers() {

        if(firebaseUser != null) {
            if(firebaseUser.isEmailVerified()) {
                DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null) {
                                if(documentSnapshot.exists()) {
                                    Log.d(TAG, "error1");
                                    showToast("로그인 성공");
                                    Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                                }
                                else {
                                    Log.d(TAG, "error2");
                                    showToast("프로필 설정 화면으로 이동합니다");
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                                }
                            }
                            else {
                                showToast("에러");
                                finishAffinity();
                            }
                        }
                    }
                });
            } else {
                showToast("이메일 인증이 안 되었습니다");
            }
        }
    }
    // 뒤로가기 두 번 종료
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
