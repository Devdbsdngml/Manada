package com.example.manada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manada.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView uni_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        uni_name = findViewById(R.id.uni_name);

        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
                    logo.startAnimation(animation);
                    uni_name.startAnimation(animation);
                    sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                    finish();
                }
            }
        };
        timer.start();
    }
}
