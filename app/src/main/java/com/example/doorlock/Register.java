package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    TextView txt_register_Doorlock;
    TextView txt_register;
    EditText et_register_email;
    EditText et_register_username;
    EditText et_register_password;
    EditText et_register_confirmPass;
    Button btn_register;
    TextView txt_login;
    FirebaseDatabase database;
    DatabaseReference ref;
    private User user;
    String username;
    String password;
    String confirmPass;
    String email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        txt_register = (TextView) findViewById(R.id.txt_register_createAcount);
        et_register_username = (EditText) findViewById(R.id.et_register_Username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_confirmPass = (EditText) findViewById(R.id.et_register_confirmPass);
        et_register_email = (EditText)findViewById(R.id.et_register_email);
        btn_register = (Button) findViewById(R.id.btn_Regiter);
        txt_login = (TextView) findViewById(R.id.txt_register_login);


        this.database = FirebaseDatabase.getInstance();
        this.ref = this.database.getReference().child("users/username1");
        this.user = new User();


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.this.btn_register(v);
            }
        });

    }

        public void btn_register(View view){
            this.username = this.et_register_username.getText().toString();
            this.password = this.et_register_password.getText().toString();
            this.confirmPass = this.et_register_confirmPass.getText().toString();
            this.email = this.et_register_email.getText().toString();
            this.user.setUsername(this.et_register_username.getText().toString());
            this.user.setPassword(this.et_register_password.getText().toString());
            this.user.setEmail(this.et_register_email.getText().toString());
            String str = "";
            if (this.username.matches(str) || this.password.matches(str) || this.confirmPass.matches(str) || this.email.matches(str)) {
                Toast.makeText(this, "All fields are required",Toast.LENGTH_SHORT).show();
            }
            else if (this.password.matches(this.confirmPass))
            {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null)
                {
                    Toast.makeText(Register.this, "Network Error, No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            ref.orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    //if (dataSnapshot!=null && dataSnapshot.getChildren()!=null && dataSnapshot.getChildren().iterator().hasNext())
                                    //{
                                     //   Toast.makeText(Register.this , "User name already exists" , Toast.LENGTH_SHORT).show();
                                    //}

                                    //else
                                    //{
                                        ref.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                        {

                                 /*public void onDataChange(DataSnapshot dataSnapshot) {
                                     if (dataSnapshot != null && dataSnapshot.getChildren() != null && dataSnapshot.getChildren().iterator().hasNext()) {
                                         Toast.makeText(Register.this, "User name already exists", Toast.LENGTH_SHORT).show();
                                     } else {

                                     }
                                 }*/

                                            public void onComplete(Task<Void> task) {
//                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                                    String str = "";
                                                    Register.this.et_register_username.setText(str);
                                                    Register.this.et_register_password.setText(str);
                                                    Register.this.et_register_confirmPass.setText(str);
                                                    Register.this.et_register_email.setText(str);
                                                    return;
//                                                }
//                                                Toast.makeText(Register.this, "Error, Please Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                //}

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                        else
                            {
                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    }
                });





               /* else
                    {
                    this.ref.child(this.user.getUsername()).setValue(this.user).addOnCompleteListener(new OnCompleteListener<Void>()
                    {

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getChildren() != null && dataSnapshot.getChildren().iterator().hasNext()) {
                                Toast.makeText(Register.this, "User name already exists", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                String str = "";
                                Register.this.et_register_username.setText(str);
                                Register.this.et_register_password.setText(str);
                                Register.this.et_register_confirmPass.setText(str);
                                return;
                            }
                            Toast.makeText(Register.this, "Error, Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
            }


            else {
                Toast.makeText(this, "Password Does not Match",Toast.LENGTH_SHORT).show();
            }
        }

        public void onBackPressed() {
            super.onBackPressed();
            startActivity(new Intent(this, Login.class));
            finish();
        }


    }
