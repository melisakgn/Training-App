package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread logoAnimation =new Thread(){
            @Override
            public void run(){
                ImageView logo =findViewById(R.id.imageView);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_intro_logo);
                logo.startAnimation(animation);
            }
        };
        logoAnimation.start();

        Thread redirect =new Thread(){
            public void  run(){
                try {
                    sleep(3000);
                    Intent i = new Intent(getApplicationContext(),girisActivity.class);
                    startActivity(i);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        redirect.start();

    }
}