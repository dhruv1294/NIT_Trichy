package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    Button sendEmailButton;
    private FirebaseAuth mAuth;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailEditText = findViewById(R.id.emailReset);

        sendEmailButton = findViewById(R.id.sendEmailButton);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(ResetPasswordActivity.this);
        progressDialog.setMessage("Sending..");


        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ResetPasswordActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ResetPasswordActivity.this, "Please check your email account to reset your password", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this,MainActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                progressDialog.dismiss();
                                String message = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, "Error occured: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
