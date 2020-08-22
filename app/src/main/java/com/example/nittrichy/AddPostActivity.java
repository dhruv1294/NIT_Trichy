package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nittrichy.Notifications.Client;
import com.example.nittrichy.Notifications.MyResponse;
import com.example.nittrichy.Notifications.NotificationApi;
import com.example.nittrichy.Notifications.NotificationData;
import com.example.nittrichy.Notifications.PushNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity {

    ImageView postImage;
    EditText postTitle,postDescrption;
    Button addPost;
    Uri mainImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabasePost;
    Toolbar toolbar;
    NotificationApi notificationApi;
    String topic = "/topics/notification";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        setTitle("Add Post");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        postImage = findViewById(R.id.postImage);
        postTitle = findViewById(R.id.postTitleText);
        postDescrption = findViewById(R.id.postDescriptionText);
        mDatabasePost = FirebaseDatabase.getInstance().getReference().child("Posts");
        addPost = findViewById(R.id.addPostButton);
        notificationApi= Client.getClient("https://fcm.googleapis.com/").create(NotificationApi.class);
       mStorage = FirebaseStorage.getInstance().getReference();
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(AddPostActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(AddPostActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }else {



                       Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                       intent.setType("image/*");
                       startActivityForResult(intent,1);

                    }

                }else{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,1);
                }

                //CropImage.startPickImageActivity(AddPostActivity.this);
            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


       if (requestCode == 1) {
           // Uri result = data.getData();
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                mainImageUri = resultUri;
                postImage.setImageURI(resultUri);
            } else if (resultCode == 0) {

            }
        }






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==1 ){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        }
    }

    public void startPosting(){
        progress = new ProgressDialog(AddPostActivity.this);
        progress.setMessage("Uploading...");
        progress.show();
        final String title = postTitle.getText().toString();
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        final String saveCurrentDate = currentDate.format(calFordDate.getTime());
        final String description = postDescrption.getText().toString();
        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(description)&& mainImageUri!=null){
            final StorageReference filePath = mStorage.child("Post_Images").child(mainImageUri.getLastPathSegment());
            filePath.putFile(mainImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();
                                        Log.i("downloadUrl", downloadUrl);
                                        DatabaseReference newPost = mDatabasePost.push();
                                        newPost.child("Key").setValue(newPost.getKey());
                                        newPost.child("Title").setValue(title);
                                        newPost.child("Desc").setValue(description);
                                        newPost.child("image").setValue(downloadUrl);
                                        newPost.child("Time").setValue(saveCurrentDate);
                                       



                                    }
                                });
                               // String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult().toString();



                            PostAdminActivity.recyclerAdapter.notifyDataSetChanged();
                            Toast.makeText(AddPostActivity.this, "Successfully Posted!", Toast.LENGTH_SHORT).show();
                            notification();
                            progress.dismiss();
                            Intent intent = new Intent(AddPostActivity.this,PostAdminActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progress.dismiss();
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
    }
    private void notification(){

        NotificationData data = new NotificationData("NIT TRICHY","NEW POST IS UP.");
        PushNotification pushNotification = new PushNotification(data,topic);
        notificationApi.sendNotificattion(pushNotification).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.i("msg", "delivered");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

}
