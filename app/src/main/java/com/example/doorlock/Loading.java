package com.example.doorlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Loading extends AppCompatActivity
{
    AnimationDrawable lockAnimation;
    Handler handler;
    ImageView img_animation;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        firebaseAuth.signOut();
         Intent intent = new Intent(Loading.this , Login.class);
         startActivity(intent);
         finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        img_animation = (ImageView)findViewById(R.id.img_animation);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        img_animation.setBackgroundResource(R.drawable.animation);
        lockAnimation = (AnimationDrawable)img_animation.getBackground();
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                //tv.append("Hello World");
                lockAnimation.stop();
                handler.postDelayed(this, 1000);
                //start();


            }
        };

        handler.postDelayed(r, 1000);
        lockAnimation.start();


        handler = new Handler();

        final Runnable run = new Runnable() {
            public void run() {
                //after login code
                Intent intent = new Intent(Loading.this , MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(Loading.this, "Signed in successfully as "+ user.getEmail(), Toast.LENGTH_LONG).show();


            }
        };

        handler.postDelayed(run, 1000);



    }

    public void start()
    {
        Intent intent = new Intent(Loading.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
