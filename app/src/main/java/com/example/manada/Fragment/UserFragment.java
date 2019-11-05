package com.example.manada.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.manada.Model.UserModel;
import com.example.manada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "UserFragment";

    public UserFragment() {

    }

    private View view;
    private TextView tv_frag_user_email, tv_frag_user_name, tv_frag_user_gender,
            tv_frag_user_personnel, tv_frag_user_mycollege, tv_frag_user_yourcollege;
    private Spinner sp_frag_user_personnel, sp_frag_user_mycollege, sp_frag_user_yourcollege;
    private Button btn_frag_user_save, btn_frag_user_modify;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private String p, m, y;
    private String name, gender, uid, personnel, mycollege, yourcollege;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        // To hide this warning and ensure your app does not break, you need to add the following code to your app before calling any other Cloud Firestore methods:
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build();
//        firebaseFirestore.setFirestoreSettings(settings);

        // email textview에 이메일 출력
        tv_frag_user_email.setText(firebaseUser.getEmail());

        // firestore에 users에 저장된 값을 불러와서 textview에 출력
        DocumentReference documentReference = firebaseFirestore.collection("users")
                .document(firebaseUser.getUid());

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        name = documentSnapshot.get("name").toString().trim();
                        gender = documentSnapshot.get("gender").toString().trim();
                        tv_frag_user_name.setText(name);
                        tv_frag_user_gender.setText(gender);

                    }
                });

        // 스피너 등록 및 이벤트 리스너 연결
        setSpinner();

        // firestore에 conditions 컬렉션 값 저장 유무 확인
        checkDocConditions();

        // 버튼 이벤트 리스너 등록
        btn_frag_user_save.setOnClickListener(this);
        btn_frag_user_modify.setOnClickListener(this);


        return view;
    }

    private void initView() {
        tv_frag_user_email = view.findViewById(R.id.tv_frag_user_email);
        tv_frag_user_name = view.findViewById(R.id.tv_frag_user_name);
        tv_frag_user_gender = view.findViewById(R.id.tv_frag_user_gender);
        tv_frag_user_personnel = view.findViewById(R.id.tv_frag_user_personnel);
        tv_frag_user_mycollege = view.findViewById(R.id.tv_frag_user_mycollege);
        tv_frag_user_yourcollege = view.findViewById(R.id.tv_frag_user_yourcollege);
        sp_frag_user_personnel = view.findViewById(R.id.sp_frag_user_personnel);
        sp_frag_user_mycollege = view.findViewById(R.id.sp_frag_user_mycollege);
        sp_frag_user_yourcollege = view.findViewById(R.id.sp_frag_user_yourcollege);
        btn_frag_user_save = view.findViewById(R.id.btn_frag_user_save);
        btn_frag_user_modify = view.findViewById(R.id.btn_frag_user_modify);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setSpinner() {
        String[] personnel_datas = getResources().getStringArray(R.array.personnel);
        ArrayAdapter<String> pd_aa = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, personnel_datas);
        pd_aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_frag_user_personnel.setAdapter(pd_aa);
        sp_frag_user_personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    p = item.toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] college_datas = getResources().getStringArray(R.array.college);
        ArrayAdapter<String> cd_aa = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, college_datas);
        cd_aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        sp_frag_user_mycollege.setAdapter(cd_aa);
        sp_frag_user_mycollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    m = item.toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_frag_user_yourcollege.setAdapter(cd_aa);
        sp_frag_user_yourcollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item != null) {
                    y = item.toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btn_frag_user_save) {
            save();
        }
        if (view == btn_frag_user_modify) {
            modify();
        }
    }

    private void save() {
//        condition = new HashMap<>();
//        condition.put("personnel", p);
//        condition.put("mycollege", m);
//        condition.put("yourcollege", y);

        uid = firebaseUser.getUid();

        UserModel userConditions = new UserModel();
        userConditions.name = name;
        userConditions.gender = gender;
        userConditions.uid = uid;
        userConditions.personnel = p;
        userConditions.mycollege = m;
        userConditions.yourcollege = y;

        // firestore의 conditions collection에 저장
        if (firebaseUser != null) {
            firebaseFirestore.collection("conditions")
                    .document(firebaseUser.getUid()).set(userConditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            showToast("저장되었습니다");
                            checkDocConditions();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("알 수 없는 오류");
                        }
                    });
        }
    }

    private void modify() {
        sp_frag_user_personnel.setVisibility(View.VISIBLE);
        sp_frag_user_mycollege.setVisibility(View.VISIBLE);
        sp_frag_user_yourcollege.setVisibility(View.VISIBLE);

        tv_frag_user_personnel.setVisibility(View.GONE);
        tv_frag_user_mycollege.setVisibility(View.GONE);
        tv_frag_user_yourcollege.setVisibility(View.GONE);

        btn_frag_user_save.setVisibility(View.VISIBLE);
        btn_frag_user_modify.setVisibility(View.GONE);
    }

    private void afterSave() {
        sp_frag_user_personnel.setVisibility(View.GONE);
        tv_frag_user_personnel.setText(p);
        tv_frag_user_personnel.setVisibility(View.VISIBLE);

        sp_frag_user_mycollege.setVisibility(View.GONE);
        tv_frag_user_mycollege.setText(m);
        tv_frag_user_mycollege.setVisibility(View.VISIBLE);

        sp_frag_user_yourcollege.setVisibility(View.GONE);
        tv_frag_user_yourcollege.setText(y);
        tv_frag_user_yourcollege.setVisibility(View.VISIBLE);
    }

    private void checkDocConditions() {
        if (firebaseUser != null) {
            DocumentReference documentReference = firebaseFirestore.collection("conditions").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null) {
                            // 필드가 존재하면
                            if (documentSnapshot.exists()) {
                                personnel = documentSnapshot.get("personnel").toString().trim();
                                mycollege = documentSnapshot.get("mycollege").toString().trim();
                                yourcollege = documentSnapshot.get("yourcollege").toString().trim();


                                // spinner and textview의 visibility 설정
                                sp_frag_user_personnel.setVisibility(View.GONE);
                                tv_frag_user_personnel.setVisibility(View.VISIBLE);
                                tv_frag_user_personnel.setText(personnel);

                                sp_frag_user_mycollege.setVisibility(View.GONE);
                                tv_frag_user_mycollege.setVisibility(View.VISIBLE);
                                tv_frag_user_mycollege.setText(mycollege);

                                sp_frag_user_yourcollege.setVisibility(View.GONE);
                                tv_frag_user_yourcollege.setVisibility(View.VISIBLE);
                                tv_frag_user_yourcollege.setText(yourcollege);

                                // save 버튼 사라지고 modify 버튼 나옴
                                btn_frag_user_save.setVisibility(View.GONE);
                                btn_frag_user_modify.setVisibility(View.VISIBLE);
                            }
                            // 필드가 존재하지 않으면
                            else {
                                sp_frag_user_personnel.setVisibility(View.VISIBLE);
                                sp_frag_user_mycollege.setVisibility(View.VISIBLE);
                                sp_frag_user_yourcollege.setVisibility(View.VISIBLE);

                                btn_frag_user_save.setVisibility(View.VISIBLE);
                                btn_frag_user_modify.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d(TAG, "error", task.getException());
                        }
                    }
                }
            });
        } else {
            Log.d(TAG, "error : User Null");
        }
    }
}
