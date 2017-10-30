package com.linalgs.misemillafinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY=3000;
    Intent intent;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        //me trae las preferencias
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        //para editar
        editor =prefs.edit();
        editor.commit();

        final int optLog = prefs.getInt("optLog", 0);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (optLog==0){
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_DELAY);
    }
}