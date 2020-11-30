package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    EditText et_login_username;
    EditText et_login_password;
    TextView txt_login_Doorlock;
    TextView txt_login;
    TextView txt_register;
    TextView txt_forgot_pass;
    Button btn_login;
    ImageView img_logo;
    private DatabaseReference ref;
    String name;
    String pass;
    FirebaseUser mAuth;
    FirebaseAuth firebaseAuth;
    AnimationDrawable lockAnimation;
    Handler fhandler;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_forgot_pass = (TextView)findViewById(R.id.txt_forgot_password);
        img_logo = (ImageView)findViewById(R.id.img_logo);
        txt_login = (TextView) findViewById(R.id.txt_login);
        et_login_username = (EditText) findViewById(R.id.et_login_Username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_register = (TextView) findViewById(R.id.txt_register);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , Forgot_Password.class);
                startActivity(intent);
                //finish();
            }
        });



            this.ref = FirebaseDatabase.getInstance().getReference().child("users/username1");

            txt_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                    finish();
                }
            });


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login.this.btn_login(v);
                }
            });

    }



public void btn_login(final View view) {
        this.name = this.et_login_username.getText().toString();
        this.pass = this.et_login_password.getText().toString();
        String str = "";
        if (this.name.matches(str) || this.pass.matches(str)) {
            Toast.makeText(this, "Please enter both Fields",Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(Login.this, "Network Error, No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else {
            try {

                auth.signInWithEmailAndPassword(et_login_username.getText().toString() , et_login_password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(Login.this , Loading.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    {
                                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                            }
                        });
               /* this.ref.child(this.name).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {

                            if (Login.this.pass.equals(((User) dataSnapshot.getValue(User.class)).getPassword())) {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                Login.this.startActivity(new Intent(Login.this, Loading.class));
                                Login.this.finish();
                                //onWindowFocusChanged(view.hasFocus());


                                return;
                            }
                            Toast.makeText(Login.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        } catch (NullPointerException e) {
                            Toast.makeText(Login.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
            } catch (NullPointerException e) {
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
