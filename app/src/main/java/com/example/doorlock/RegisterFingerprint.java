package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.doorlock.App.CHANNEL_1_ID;

public class RegisterFingerprint extends AppCompatActivity {
    TextView txtView_registerFingerprint;
    EditText et_registerFingerprint_name, et_registerFingerprint_email , et_registerFingerprint_password , et_registerFingerprint_confirmPassword;
    Button btn_registerFingerprint_register;
    String name , email , password, confirmPassword;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth googleAuth;
    FirebaseUser user;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fingerprint);
        user = FirebaseAuth.getInstance().getCurrentUser();
        googleAuth = FirebaseAuth.getInstance();

        txtView_registerFingerprint = (TextView)findViewById(R.id.txt_registerFingerprint);
        et_registerFingerprint_name = (EditText)findViewById(R.id.et_registerFingerprint_name);
        et_registerFingerprint_email = (EditText)findViewById(R.id.et_registerFingerprint_email);
        et_registerFingerprint_password = (EditText)findViewById(R.id.et_registerFingerprint_password);
        et_registerFingerprint_confirmPassword = (EditText)findViewById(R.id.et_registerFingerprint_confirmPassword);
        btn_registerFingerprint_register= (Button)findViewById(R.id.btn_registerFingerprint_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("fingerprint");
        reference = firebaseDatabase.getReference().child("current");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Register");
        }

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        TextView nav_username = (TextView)headerView.findViewById(R.id.text_nav_head);
        nav_username.setText(user.getEmail());

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer , toolbar,
                R.string.Open_nav_drawer, R.string.Close_nav_drawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                if(menuItem.getTitle().equals("Logout"))
                {
                    googleAuth.getInstance().signOut();
                    Intent intent = new Intent(RegisterFingerprint.this , Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterFingerprint.this , "Logged out successfully" , Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    return true;
                }
                else if (menuItem.getTitle().equals("Show Details"))
                {
                    Intent intent = new Intent(RegisterFingerprint.this , ShowDetails.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (menuItem.getTitle().equals("Door Lock/Unlock"))
                {
                    Intent intent = new Intent(RegisterFingerprint.this , Door_Lock_Unlock.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if(menuItem.getTitle().equals("Register Fingerprint")){
//                    Intent intent = new Intent(RegisterFingerprint.this , RegisterFingerprint.class);
//                    startActivity(intent);
//                    finish();
//                    return true;
                }
                else if (menuItem.getTitle().equals("Description"))
                {
                    Intent intent = new Intent(RegisterFingerprint.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


        btn_registerFingerprint_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_registerFingerprint_name.getText().toString().trim();
                password = et_registerFingerprint_password.getText().toString().trim();
                email = et_registerFingerprint_email.getText().toString().trim();
                confirmPassword = et_registerFingerprint_confirmPassword.getText().toString().trim();
                if(name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals(""))
                {
                    Toast.makeText(RegisterFingerprint.this , "Please Enter All fields" , Toast.LENGTH_LONG).show();
                }
                else{
                    if(password.length()<6){
                        Toast.makeText(RegisterFingerprint.this , "Password should be at least 6 characters" , Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(password.equals(confirmPassword))
                        {
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo ni = cm.getActiveNetworkInfo();
                            if (ni == null)
                            {
                                Toast.makeText(RegisterFingerprint.this, "Network Error, No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                RegisterFingerprintModal registerFingerprintModal = new RegisterFingerprintModal(name , email , password);
                                databaseReference.push().setValue(registerFingerprintModal);
                                reference.child("key").setValue(name);
                                reference.child("fingerprint").setValue("register");
                                et_registerFingerprint_name.setText("");
                                et_registerFingerprint_email.setText("");
                                et_registerFingerprint_password.setText("");
                                et_registerFingerprint_confirmPassword.setText("");
                                Intent intent = new Intent(RegisterFingerprint.this, Gif_View.class );
                                startActivity(intent);
                                finish();
                            }

                        }
                        else{
                            Toast.makeText(RegisterFingerprint.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference notiref = database.getReference().child("noOfAttempts");
    DatabaseReference intrusionRef = database.getReference().child("intrusion");
    DatabaseReference unlockRef = database.getReference().child("unlock");

    //    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public void dataBaseRead()
    {



//        ref.child("users").child("username1").child("info");;

        notiref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

                long[] v = {500,1000};
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //dataBaseRead();
                Intent notificationIntent = new Intent(RegisterFingerprint.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(RegisterFingerprint.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(RegisterFingerprint.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_warning_24)
                        .setContentTitle("Warning")
                        .setContentText("Someone is trying to open the door")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVibrate(v)
                        .setSound(uri)
                        .addAction(R.drawable.ic_baseline_warning_24, "Show", intent)
                        .build();

                notificationManager.notify(1, notification);



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        intrusionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                long[] v = {500,1000};
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //dataBaseRead();
                Intent notificationIntent = new Intent(RegisterFingerprint.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(RegisterFingerprint.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(RegisterFingerprint.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_warning_24)
                        .setContentTitle("ALERT")
                        .setContentText("Intrusion Detection")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVibrate(v)
                        .setSound(uri)
                        .addAction(R.drawable.ic_baseline_warning_24, "Show", intent)
                        .build();

                notificationManager.notify(1, notification);

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

        unlockRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                long[] v = {500,1000};
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //dataBaseRead();
                Intent notificationIntent = new Intent(RegisterFingerprint.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(RegisterFingerprint.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(RegisterFingerprint.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_door_lock)
                        .setContentTitle("DOOR UNLOCK")
                        .setContentText("The door is unlocked.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVibrate(v)
                        .setSound(uri)
                        .addAction(R.drawable.ic_door_lock, "Show", intent)
                        .build();

                notificationManager.notify(1, notification);

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

    }

    }

