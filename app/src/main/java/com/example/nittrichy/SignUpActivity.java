package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText email,name,password,confirmPassword;
    Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        signUpButton = findViewById(R.id.signInButton);
        email = findViewById(R.id.emailSign);
        name = findViewById(R.id.nameSign);
        password = findViewById(R.id.passwordSign);
        confirmPassword = findViewById(R.id.confirmpassword);
        confirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    signupfinal();
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupfinal();
            }
        });

    }

    public void signupfinal(){
        if(TextUtils.isEmpty(name.getText())){
            name.setError("Name is required");
        }else if(TextUtils.isEmpty(email.getText())){
            email.setError("EmailId is required");
        }else if(TextUtils.isEmpty(password.getText())){
            password.setError("Password is required");
        }else if(TextUtils.isEmpty(confirmPassword.getText())){
            confirmPassword.setError("Confirm Password is required");
        }else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Confirm password is not same. ", Toast.LENGTH_SHORT).show();
        }
        else{
            progress.setMessage("Signing Up! ...");
            progress.show();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),confirmPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mDatabaseUsers.child(user_id);
                        current_user.child("name").setValue(name.getText().toString());
                        current_user.child("userid").setValue(user_id);


                        progress.dismiss();
                        Intent intent = new Intent(SignUpActivity.this,PostActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else{
                        progress.dismiss();
                        String error = task.getException().getMessage();
                        Toast.makeText(SignUpActivity.this, "Errro: "+ error , Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

}
