package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nittrichy.Models.UploadPdf;
import com.example.nittrichy.Notifications.Client;
import com.example.nittrichy.Notifications.MyResponse;
import com.example.nittrichy.Notifications.NotificationApi;
import com.example.nittrichy.Notifications.NotificationData;
import com.example.nittrichy.Notifications.PushNotification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPDFActivity extends AppCompatActivity {
    Toolbar toolbar;

    private EditText editPDFName;
    private Button fileUploadButton;
    StorageReference storageReference;
    DatabaseReference noticeRef;
    DatabaseReference newsRef;
    ProgressDialog progress;
    String type;
    String topic = "/topics/notification";
    NotificationApi notificationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);

        editPDFName = findViewById(R.id.pdfFileName);
        fileUploadButton = findViewById(R.id.pdfUploadBtn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getStringExtra("type");
        notificationApi= Client.getClient("https://fcm.googleapis.com/").create(NotificationApi.class);
        storageReference = FirebaseStorage.getInstance().getReference();
        noticeRef = FirebaseDatabase.getInstance().getReference().child("PDFUploads");
        newsRef = FirebaseDatabase.getInstance().getReference().child("newsUploads");

        fileUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();

            }
        });
    }
    private void selectPDFFile(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF file"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData()!=null){
            uploadPDFFile(data.getData());
        }

    }

    private void uploadPDFFile(Uri data){

        final ProgressDialog progressDialog = new ProgressDialog(AddPDFActivity.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference  reference = storageReference.child("uploads/" + System.currentTimeMillis()+ ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                            Uri url = uri.getResult();
                            //UploadPdf uploadPdf = new UploadPdf(editPDFName.getText().toString(),url.toString());
                        if(type.equals("notice")) {
                            DatabaseReference newPdf = noticeRef.push();
                            newPdf.child("key").setValue(newPdf.getKey());
                            newPdf.child("name").setValue(editPDFName.getText().toString());
                            newPdf.child("url").setValue(url.toString());
                        }else{
                            DatabaseReference newPdf = newsRef.push();
                            newPdf.child("key").setValue(newPdf.getKey());
                            newPdf.child("name").setValue(editPDFName.getText().toString());
                            newPdf.child("url").setValue(url.toString());
                        }
                        notification();
                        Toast.makeText(AddPDFActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(AddPDFActivity.this,PostAdminActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+ (int)progress+"%");

            }
        });
    }
    private void notification(){
        NotificationData data = new NotificationData("NIT TRICHY","NEW Notice IS UP.");
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
