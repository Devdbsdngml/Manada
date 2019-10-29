package com.example.manada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.makeText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private Button btn_login, btn_cancel;
    private EditText login_et_email, login_et_password;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btn_login.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    public void onClick(View view) {
        if(view == btn_login) {
            Login();
        }
        if(view == btn_cancel) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        }
    }
    private void initView() {
        btn_login = findViewById(R.id.btn_login);
        btn_cancel = findViewById(R.id.btn_cancel);
        login_et_email = findViewById(R.id.login_et_email);
        login_et_password = findViewById(R.id.login_et_password);
    }
    private void Login() {

        if (!validateEmailAddress() | !validatePassword()) {
            return;
        }

        String email = login_et_email.getText().toString().trim();
        String password = login_et_password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(firebaseUser.isEmailVerified()) {
                                Log.d(TAG, "error1");
                                showToast("로그인 성공");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                            } else {
                                Log.d(TAG,"error2", task.getException());
                                showToast("이메일 인증을 해주세요");
                            }
                        } else {
                            showToast("존재하지 않는 아이디거나 " +
                                     "비밀번호가 틀렸습니다");
                        }
                    }
                });
    }
    private boolean validateEmailAddress() {
        String email = login_et_email.getText().toString().trim();
        if (email.isEmpty()) {
            login_et_email.setError("이메일 주소를 입력하세요");
            return false;
        }
//        else if(!email.contains("@ut.ac.kr")) {
//            login_et_email.setError("학교 이메일을 입력하세요.");
//            return false;
//        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_et_email.setError("존재하지 않는 이메일입니다.");
            return false;
        }else {
            login_et_email.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String password = login_et_password.getText().toString().trim();
        if (password.isEmpty()) {
            login_et_password.setError("비밀번호를 입력하세요.");
            return false;
        } else if (password.length() < 6) {
            login_et_password.setError("비밀번호를 6자 이상 입력하세요.");
            return false;
        } else {
            login_et_password.setError(null);
            return true;
        }
    }
    // 뒤로가기 두 번 종료
    long initTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis() - initTime > 3000) {
                showToast("종료하려면 한번 더 누르세요");
                initTime = System.currentTimeMillis();
            }else {
                finishAffinity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showToast(String msg) {
        makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
