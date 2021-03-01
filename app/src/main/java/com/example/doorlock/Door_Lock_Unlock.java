package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import static com.example.doorlock.App.CHANNEL_1_ID;
import static com.example.doorlock.App.CHANNEL_2_ID;

public class Door_Lock_Unlock extends AppCompatActivity {
    Button btn_lock;
    Button btn_unlock;
    private DatabaseReference ref;
    AlertDialog.Builder builder;
    private NotificationManagerCompat notificationManager;
    FirebaseAuth googleAuth;
    TextView txt_door_status;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door__lock__unlock);

        notificationManager = NotificationManagerCompat.from(this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_lock = (Button)findViewById(R.id.btn_lock);
        btn_unlock = (Button)findViewById(R.id.btn_unlock);
        googleAuth = FirebaseAuth.getInstance();
        txt_door_status = (TextView)findViewById(R.id.txt_main_DoorLock);


        this.ref = FirebaseDatabase.getInstance().getReference().child("door_lock");
        btn_lock.setVisibility(View.INVISIBLE);
        btn_unlock.setVisibility(View.INVISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
               txt_door_status.setText(dataSnapshot.child("value").getValue().toString());
               if(dataSnapshot.child("value").getValue().toString().equals("The Door is unlock")){
//                   btn_lock.setText("Lock the door");
//                   btn_unlock.setVisibility(View.INVISIBLE);
                   btn_lock.setVisibility(View.VISIBLE);
                   btn_unlock.setVisibility(View.INVISIBLE);
//                   ref.child("value").setValue("The Door is lock");
//                   if (ni == null) {
//                       Toast.makeText(Door_Lock_Unlock.this, "Networkd Error, No Internet Connection", Toast.LENGTH_SHORT).show();
//                   }
//                   else {
                       //sendOnChannel2(v);
//                   }
               }
               else if(dataSnapshot.child("value").getValue().toString().equals("The Door is lock")){
//                   btn_unlock.setText("Unlock the door");
                   btn_unlock.setVisibility(View.VISIBLE);
                   btn_lock.setVisibility(View.INVISIBLE);
//                   btn_lock.setVisibility(View.INVISIBLE);
//                   ref.child("value").setValue("The Door is lock");
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //slider
        //navigation
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("DoorLock");
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
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
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
                    Intent intent = new Intent(Door_Lock_Unlock.this , Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Door_Lock_Unlock.this , "Logged out successfully" , Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    return true;
                }
                else if (menuItem.getTitle().equals("Show Details"))
                {
                    Intent intent = new Intent(Door_Lock_Unlock.this , ShowDetails.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (menuItem.getTitle().equals("Door Lock/Unlock"))
                {
                    //Intent intent = new Intent(Door_Lock_Unlock.this , Door_Lock_Unlock.class);
                    //startActivity(intent);
                    //finish();
                    return true;
                }
                else if(menuItem.getTitle().equals("Register Fingerprint")){
                    Intent intent = new Intent(Door_Lock_Unlock.this , RegisterFingerprint.class);
                    startActivity(intent);
                    finish();
                }
                else if (menuItem.getTitle().equals("Description"))
                {
                    Intent intent = new Intent(Door_Lock_Unlock.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });




        btn_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null) {
                    Toast.makeText(Door_Lock_Unlock.this, "Networkd Error, No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    ref.child("value").setValue("The Door is lock");
                    //sendOnChannel2(v);
                }
            }
        });

        btn_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null) {
                    Toast.makeText(Door_Lock_Unlock.this, "Networkd Error, No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    ref.child("value").setValue("The Door is unlock");
                    //sendOnChannel1(v);
                }


            }
        });

        builder = new AlertDialog.Builder(this);



    }




   /* public void sendOnChannel1(View v)
    {
        Notification notification = new NotificationCompat.Builder(this , CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Door")
                .setContentText("Door is Unlocked")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1 , notification);


    }

    public void sendOnChannel2(View v)
    {

        Notification notification = new NotificationCompat.Builder(this , CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle("Door")
                .setContentText("Door is Locked")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2 , notification);


    }*/

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
                Intent notificationIntent = new Intent(Door_Lock_Unlock.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(Door_Lock_Unlock.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(Door_Lock_Unlock.this, CHANNEL_1_ID)
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
                Intent notificationIntent = new Intent(Door_Lock_Unlock.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(Door_Lock_Unlock.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(Door_Lock_Unlock.this, CHANNEL_1_ID)
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
                Intent notificationIntent = new Intent(Door_Lock_Unlock.this , Door_Lock_Unlock.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(Door_Lock_Unlock.this , 0,
                        notificationIntent, 0);


                Notification notification = new NotificationCompat.Builder(Door_Lock_Unlock.this, CHANNEL_1_ID)
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
