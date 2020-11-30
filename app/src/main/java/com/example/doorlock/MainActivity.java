package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.iid.FirebaseInstanceId;

import static com.example.doorlock.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    TextView txt_doorLock;
    TextView txt_description;
    AlertDialog.Builder builder;
    private NotificationManagerCompat notificationManager;
    FirebaseAuth googleAuth;

    String user_name;
    String User_date;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        googleAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //notification initialize
        notificationManager = NotificationManagerCompat.from(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        txt_doorLock = (TextView) findViewById(R.id.txt_main_DoorLock);
        txt_description = (TextView) findViewById(R.id.txt_main_Description);

        builder = new AlertDialog.Builder(this);


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
                    Intent intent = new Intent(MainActivity.this , Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MainActivity.this , "Logged out successfully" , Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    return true;
                }
                else if (menuItem.getTitle().equals("Show Details"))
                {
                    Intent intent = new Intent(MainActivity.this , ShowDetails.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (menuItem.getTitle().equals("Door Lock/Unlock"))
                {
                    Intent intent = new Intent(MainActivity.this , Door_Lock_Unlock.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (menuItem.getTitle().equals("Description"))
                {
                    //Intent intent = new Intent(Door_Lock_Unlock.this , MainActivity.class);
                    //startActivity(intent);
                    //finish();
                }
                return false;
            }
        });






        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference();

        dataBaseRead();

        reference.child("notify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }






    @Override
    public void onBackPressed() {
        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(MainActivity.this , Login.class);
                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();


    }


    public  void dataBaseRead()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("notify");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {


                EntryInfo entryInfo = dataSnapshot.getValue(EntryInfo.class);


//                String userr = dataSnapshot.getValue().toString();
                user_name = entryInfo.getE_name();
                User_date = entryInfo.getDate_time();

  //              String nw = (user_name + " " + User_date);


                long[] v = {500,1000};
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //dataBaseRead();
                Intent notificationIntent = new Intent(MainActivity.this , ShowDetails.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(MainActivity.this , 0,
                        notificationIntent, 0);

                if ((!TextUtils.isEmpty(user_name)) && (!TextUtils.isEmpty(User_date)))
                {
                    if (!user_name.equals("Unknown"))
                    {
                    Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_one)
                            .setContentTitle("New user entered")
                            .setContentText("Person: " +user_name + " Date: " + User_date)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setVibrate(v)
                            .setSound(uri)
                            .addAction(R.drawable.ic_one, "Show", intent)
                            .build();


                    notificationManager.notify(1, notification);
                    }

                    else
                        {
                            Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_1_ID)
                                    .setSmallIcon(R.drawable.ic_one)
                                    .setContentTitle("Alert")
                                    .setContentText("Person: " +user_name + " Date: " + User_date)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setVibrate(v)
                                    .setSound(uri)
                                    .addAction(R.drawable.ic_one, "Show", intent)
                                    .build();

                            notificationManager.notify(1, notification);
                        }
                }

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

    }
}
