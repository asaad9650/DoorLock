package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Gif_View extends AppCompatActivity {
    TextView textView;
    ProgressBar progressBar;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth googleAuth;
    FirebaseUser user;
    String getvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif__view);
        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference().child("users/username1");
        reference = firebaseDatabase.getReference().child("current");
        textView = (TextView)findViewById(R.id.txt_putFinger);
        progressBar =(ProgressBar)findViewById(R.id.progress_bar);


        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                getvalue = snapshot.getValue().toString();
                Toast.makeText(Gif_View.this , "Finger Print Setup Successful" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Gif_View.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                getvalue = snapshot.getValue().toString();
//                Toast.makeText(Gif_View.this , "Finger Print Setup Successful" , Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(Gif_View.this , "Please put your finger on FingerPrint scanner " , Toast.LENGTH_LONG ).show();
    }
}