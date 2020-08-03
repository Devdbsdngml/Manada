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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SignUpActivity";

    private Button btn_signin,btn_sendemail;
    private EditText signup_et_email, signup_et_password, signup_et_password_confirm;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private long initTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btn_signin.setOnClickListener(this);
        btn_sendemail.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == btn_signin) {
            CreateUser();
        }
        if(view == btn_sendemail) {
            SendEmailVerify();
        }
    }
    private void initView() {
        btn_signin = findViewById(R.id.btn_signin);
        btn_sendemail = findViewById(R.id.btn_sendemail);
        signup_et_email = findViewById(R.id.signup_et_email);
        signup_et_password = findViewById(R.id.signup_et_password);
        signup_et_password_confirm = findViewById(R.id.signup_et_password_confirm);
    }
    private void CreateUser() {

        if (!validateEmailAddress() | !validatePassword()) {
            return;
        }

        String email = signup_et_email.getText().toString().trim();
        String password = signup_et_password.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"error1");
                            showToast("가입이 완료 되었습니다");
                            btn_signin.setEnabled(false);
                            btn_sendemail.setEnabled(true);
                        } else {
                            Log.d(TAG,"error2");
                        }
                    }
                });
    }
    private void SendEmailVerify() {

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("기입한 메일 주소로 인증 메일이 전송되었습니다");
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                        }
                        else {
                            Log.d(TAG,"Send Verification fail!");
                            showToast("error");
                        }
                    }
                });
    }
    private boolean validateEmailAddress() {
        String email = signup_et_email.getText().toString().trim();
        if (email.isEmpty()) {
            signup_et_email.setError("이메일 주소를 입력하세요");
            return false;
        }
        else if(!email.contains("@ut.ac.kr")) {
            signup_et_email.setError("학교 이메일을 입력하세요");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signup_et_email.setError("이메일 형식이 잘못 됐습니다");
            return false;
        }else {
            signup_et_email.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String password = signup_et_password.getText().toString().trim();
        String password_confirm = signup_et_password_confirm.getText().toString().trim();

        if (password.isEmpty()) {
            signup_et_password.setError("비밀번호를 입력하세요");
            return false;
        } else if (password.length() < 6) {
            signup_et_password.setError("비밀번호를 6자 이상 입력하세요");
            return false;
        } else if (!password.equals(password_confirm)) {
            signup_et_password.setError("비밀번호가 일치하지 않습니다");
            signup_et_password_confirm.setError("비밀번호가 일치하지 않습니다");
            return false;
        } else {
            signup_et_password.setError(null);
            return true;
        }
    }
    // 뒤로가기 두 번 종료
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
