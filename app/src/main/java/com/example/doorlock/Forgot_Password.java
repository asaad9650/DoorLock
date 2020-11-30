package com.example.doorlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    Button send_email;
    TextView forgot_pass_head;
    EditText forgot_pass_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        send_email = (Button)findViewById(R.id.button_send_email);
        forgot_pass_email = (EditText)findViewById(R.id.et_forgot_pass_email);
        forgot_pass_head = (TextView)findViewById(R.id.forgot_pass_head);


        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forgot_pass_email.getText().toString().isEmpty())
                {
                    Toast.makeText(Forgot_Password.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                }

                else
                    {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(forgot_pass_email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(Forgot_Password.this, "Verification sent to your email successfully.", Toast.LENGTH_SHORT).show();
                                            forgot_pass_email.setText("");
                                            Intent intent = new Intent(Forgot_Password.this , Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                    }
            }
        });
    }
}
