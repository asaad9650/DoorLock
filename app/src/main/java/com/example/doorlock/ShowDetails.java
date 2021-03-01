package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDetails extends AppCompatActivity {


    ListView lv_details;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EntryInfo entryInfo;

    TextView txt_details;
    AlertDialog.Builder builder;
    FirebaseAuth googleAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        user = FirebaseAuth.getInstance().getCurrentUser();
        googleAuth = FirebaseAuth.getInstance();
        entryInfo = new EntryInfo();
        lv_details = (ListView)findViewById(R.id.lv_database_list);
        txt_details = (TextView) findViewById(R.id.txt_showDetails);

        //Dialogue box
        builder = new AlertDialog.Builder(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Entry_info");

        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this , R.layout.activity_user,R.id.txt_userInfo, list);




        //slider
        //navigation
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Details");
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
                    Intent intent = new Intent(ShowDetails.this , Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(ShowDetails.this , "Logged out successfully" , Toast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    return true;
                }
                else if (menuItem.getTitle().equals("Show Details"))
                {
//                    Intent intent = new Intent(ShowDetails.this , ShowDetails.class);
//                    startActivity(intent);
//                    finish();
//                    return true;
                }
                else if (menuItem.getTitle().equals("Door Lock/Unlock"))
                {
                    Intent intent = new Intent(ShowDetails.this , Door_Lock_Unlock.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if(menuItem.getTitle().equals("Register Fingerprint")){
                    Intent intent = new Intent(ShowDetails.this , RegisterFingerprint.class);
                    startActivity(intent);
                    finish();
                }
                else if (menuItem.getTitle().equals("Description"))
                {
                    Intent intent = new Intent(ShowDetails.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });





        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    entryInfo = ds.getValue(EntryInfo.class);
                    //Toast.makeText(ShowDetails.this, entryInfo.getE_name(), Toast.LENGTH_SHORT).show();
                    list.add(entryInfo.getE_name() + "       "+ entryInfo.getDate_time());
                }
                Collections.reverse(list);
                lv_details.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowDetails.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

}