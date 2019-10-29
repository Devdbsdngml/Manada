package com.example.manada.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.manada.Activity.ChatActivity;
import com.example.manada.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {
        // Required empty public constructor
    }

    private View view;

    private Handler handler;
    private TextView tv_clock;
    private Button btn_confirm;

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        startClock();

        btn_confirm.setOnClickListener(this);

        matching();

        return view;
    }

    public void onClick(View view) {
        if(view == btn_confirm) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        }
    }
    private void initView() {
        tv_clock = view.findViewById(R.id.tv_clock);
        btn_confirm = view.findViewById(R.id.btn_confirm);
    }
    private void startClock() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Calendar cal = Calendar.getInstance() ;

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String strTime = sdf.format(cal.getTime());
                tv_clock.setText(strTime) ;
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace() ;
                    }
                    handler.sendEmptyMessage(0) ;
                }
            }
        }
        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start() ;
    }

    private void matching() {
        // firestore 의 conditions collection에서 비교 후 데이터가 맞으면
        // 매칭 확인 버튼이 활성화
        btn_confirm.setEnabled(true);
    }
}
