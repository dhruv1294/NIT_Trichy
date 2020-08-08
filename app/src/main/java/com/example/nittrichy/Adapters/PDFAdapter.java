package com.example.nittrichy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.Models.UploadPdf;
import com.example.nittrichy.PDFViewActivity;
import com.example.nittrichy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder>{

    private Context context;
    private List<UploadPdf> pdfList;
    int admin;
    DatabaseReference clickPDFRef;


    public PDFAdapter(Context context, List<UploadPdf> pdfList, int admin) {
        this.context = context;
        this.pdfList = pdfList;
        this.admin = admin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.pdf_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        String name = pdfList.get(position).getName();
        holder.pdfFileName.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPdf uploadPdf = pdfList.get(position);
                Intent intent = new Intent(context, PDFViewActivity.class);
                intent.putExtra("name",uploadPdf.getName());
                intent.putExtra("url",uploadPdf.getUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pdfFileName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfFileName = itemView.findViewById(R.id.pdfFileName);
        }
    }
}
