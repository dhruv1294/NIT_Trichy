package com.example.nittrichy;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nittrichy.Adapters.PDFAdapter;
import com.example.nittrichy.Models.UploadPdf;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNoticeFilesFragment extends Fragment {

    RecyclerView myPDFListView;
    DatabaseReference reference;
    List<UploadPdf> uploadPdfs;
    PDFAdapter pdfAdapter;


    public ViewNoticeFilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_notice_files, container, false);
        myPDFListView = v.findViewById(R.id.PdfFileListView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        myPDFListView.setLayoutManager(mLayoutManager);
        String item = getArguments().getString("fragment");
        if(item.equals("notice")) {

            uploadPdfs = new ArrayList<>();
            pdfAdapter = new PDFAdapter(getActivity(), uploadPdfs, 0);
            myPDFListView.setAdapter(pdfAdapter);
            String data = "PDFUploads";
            viewAllFiles(data);
        }else{
            uploadPdfs = new ArrayList<>();
            pdfAdapter = new PDFAdapter(getActivity(), uploadPdfs, 0);
            myPDFListView.setAdapter(pdfAdapter);
            String data = "newsUploads";
            viewAllFiles(data);
        }

        return v;
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
