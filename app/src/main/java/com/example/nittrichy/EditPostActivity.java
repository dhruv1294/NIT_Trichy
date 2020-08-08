package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditPostActivity extends AppCompatActivity {
    private TextView postTitle,postDescription;
    private ImageView postImage;
    private Button editPostButton, deletePostButton;
    private String postKey;
    private DatabaseReference clickPostRef;
    private String title, desc , img;
    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        postTitle = findViewById(R.id.feedPostTitle);
        postDescription = findViewById(R.id.feedPostDescription);
        postImage = findViewById(R.id.feedPostImage);
        editPostButton = findViewById(R.id.EditPostButton);
        deletePostButton = findViewById(R.id.DeletePostButton);
        postKey = getIntent().getStringExtra("PostKey");
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);
        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   title = snapshot.child("Title").getValue().toString();
                   desc =snapshot.child("Desc").getValue().toString();
                   img = snapshot.child("image").getValue().toString();
                   postTitle.setText(title);
                   postDescription.setText(desc);
                   Picasso.with(EditPostActivity.this).load(img).into(postImage);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPostRef.removeValue();
                Toast.makeText(EditPostActivity.this, "Post deleted successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPostActivity.this,PostAdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        editPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCurrentPost(title,desc);
            }
        });

    }
    public void EditCurrentPost(String title, String desc){
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this);
        builder.setTitle("Edit Post");
        LinearLayout layout = new LinearLayout(EditPostActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText titleBox = new EditText(EditPostActivity.this);
        titleBox.setText(title);
        layout.addView(titleBox); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText descriptionBox = new EditText(EditPostActivity.this);
        descriptionBox.setText(desc);
        layout.addView(descriptionBox); // Another add method

        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickPostRef.child("Title").setValue(titleBox.getText().toString());
                clickPostRef.child("Desc").setValue(descriptionBox.getText().toString());
                Toast.makeText(EditPostActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditPostActivity.this,PostAdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

}
