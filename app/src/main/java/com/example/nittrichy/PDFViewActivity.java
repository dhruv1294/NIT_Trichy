package com.example.nittrichy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PDFViewActivity extends AppCompatActivity {
    PDFView pdfView;
    Toolbar toolbar;

    ProgressBar progressBar;
    String url;
    String name;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        pdfView =findViewById(R.id.pdfView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = getIntent().getStringExtra("name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
getSupportActionBar().setTitle(name);
        progressBar = findViewById(R.id.progress);
        url = getIntent().getStringExtra("url");
        Log.i("url",url);


        progressBar.setVisibility(View.VISIBLE);
        new RetrievePDFStream().execute(url);
        //LoadFile();

    }



   class RetrievePDFStream extends AsyncTask<String, Void , InputStream>{
       @Override
       protected InputStream doInBackground(String... strings) {
           InputStream inputStream = null;
           try {
               URL url = new URL(strings[0]);
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               if(urlConnection.getResponseCode() ==200){
                   inputStream = new BufferedInputStream(urlConnection.getInputStream());
               }
           }catch (Exception e){
               e.printStackTrace();
           }
           return inputStream;
       }

       @Override
       protected void onPostExecute(InputStream inputStream) {
           progressBar.setVisibility(View.GONE);
           pdfView.fromStream(inputStream).password(null)
                   .defaultPage(0)
                   .enableSwipe(true)
                   .swipeHorizontal(false)
                   .enableDoubletap(true)
                   .onRender(new OnRenderListener() {
                       @Override
                       public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                           pdfView.fitToWidth();
                       }
                   }).enableAnnotationRendering(true)
                   .invalidPageColor(Color.WHITE)
                   .load();
       }
   }



}
