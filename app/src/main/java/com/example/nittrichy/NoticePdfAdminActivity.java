package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nittrichy.Adapters.PDFAdapter;
import com.example.nittrichy.Models.UploadPdf;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticePdfAdminActivity extends AppCompatActivity {
    RecyclerView myPDFListView;
    DatabaseReference reference;
    List<UploadPdf> uploadPdfs;
    PDFAdapter pdfAdapter;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;
    DatabaseReference clickPDFRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_pdf_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String activity = getIntent().getStringExtra("activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myPDFListView = findViewById(R.id.PdfFileListView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        myPDFListView.setLayoutManager(mLayoutManager);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        if(activity.equals("notice")) {



            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NoticePdfAdminActivity.this, AddPDFActivity.class);
                    intent.putExtra("type","notice");
                    startActivity(intent);
                }
            });


            uploadPdfs = new ArrayList<>();
            pdfAdapter = new PDFAdapter(this, uploadPdfs, 1);
            myPDFListView.setAdapter(pdfAdapter);
            String data = "PDFUploads";
            viewAllFiles(data);
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    clickPDFRef = FirebaseDatabase.getInstance().getReference().child("PDFUploads").child(uploadPdfs.get(position).getKey());
                    clickPDFRef.removeValue();
                    Toast.makeText(NoticePdfAdminActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    pdfAdapter.notifyDataSetChanged();
                }
            }).attachToRecyclerView(myPDFListView);
        }else{
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NoticePdfAdminActivity.this, AddPDFActivity.class);
                    intent.putExtra("type","news");
                    startActivity(intent);
                }
            });
            uploadPdfs = new ArrayList<>();
            pdfAdapter = new PDFAdapter(this, uploadPdfs, 1);
            myPDFListView.setAdapter(pdfAdapter);
            String data = "newsUploads";
            viewAllFiles(data);
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    clickPDFRef = FirebaseDatabase.getInstance().getReference().child("newsUploads").child(uploadPdfs.get(position).getKey());
                    clickPDFRef.removeValue();
                    Toast.makeText(NoticePdfAdminActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    pdfAdapter.notifyDataSetChanged();
                }
            }).attachToRecyclerView(myPDFListView);
        }

    }
    private void viewAllFiles(String data){
        reference = FirebaseDatabase.getInstance().getReference().child(data);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadPdfs.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    UploadPdf uploadPdf = child.getValue(UploadPdf.class);
                    uploadPdfs.add(uploadPdf);
                    pdfAdapter.notifyDataSetChanged();


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
