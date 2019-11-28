package com.example.manada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ProfileActivity";

    private Button btn_save, btn_cancel;
    private TextView profile_tv_email;
    private RadioGroup profile_rg;
    private RadioButton profile_rb_gender, profile_rb_male, profile_rb_female;
    private EditText profile_et_name;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private String uid;
    private String gender;
    private String name;

    public UserModel userModel;

    private long initTime;
    private int seletedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        profile_tv_email.setText(firebaseUser.getEmail());

        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


    }

    private void initView() {
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);
        profile_tv_email = findViewById(R.id.profile_tv_email);
        profile_rg = findViewById(R.id.profile_rg);
        profile_et_name = findViewById(R.id.profile_et_name);
        profile_rb_male = findViewById(R.id.profile_rb_male);
        profile_rb_female = findViewById(R.id.profile_rb_female);
    }
    public void onClick(View view) {
        if(view == btn_save) {
            // 입력한 데이터를 DB에 저장하고 MainActivity로 이동한 후 FragmentActivity로 이동
            registerUser();
        }
        if(view == btn_cancel) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        }
    }
    private void registerUser() {

        seletedID = profile_rg.getCheckedRadioButtonId();
        profile_rb_gender = findViewById(seletedID);

        gender = profile_rb_gender.getText().toString().trim();
        name = profile_et_name.getText().toString().trim();
        uid = firebaseUser.getUid();

        UserModel userModel = new UserModel();
        userModel.uid = uid;
        userModel.name = name;
        userModel.gender = gender;

        if(!name.isEmpty() && (profile_rb_male.isChecked() || profile_rb_female.isChecked())) {
            if(firebaseUser != null) {
                firebaseFirestore.collection("users").document(firebaseUser.getUid()).set(userModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "error1");
                                showToast("회원정보가 등록되었습니다");
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "error2");
                                showToast("회원정보 등록을 실패하였습니다");
                                profile_et_name.setText(null);
                                profile_rg.clearCheck();
                            }
                        });
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
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
