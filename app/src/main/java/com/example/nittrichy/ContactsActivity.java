package com.example.nittrichy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.nittrichy.Adapters.ContactsAdapter;
import com.example.nittrichy.Models.Contacts;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactsAdapter contactsAdapter;
    ArrayList<Contacts> contacts;
    Toolbar toolbar;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recyclerView = findViewById(R.id.contactsView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = getIntent().getStringExtra("council");
        if(name.equals("student")){
            getSupportActionBar().setTitle("Student Council");
        }else{
            getSupportActionBar().setTitle("Technical Council");
        }



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        contacts = new ArrayList<>();
        contacts.add(new Contacts("President","Mr. Kamalesh Khanna","1234567890"));
        contacts.add(new Contacts("Vice-\nPresident","Ms. Fathima Maha","1234567890"));
        contacts.add(new Contacts("General Secretary","Mr. Rahul R","1234567890"));
        contacts.add(new Contacts("PG Secretary(Boys)","Mr. Rishabh Tripathi","1234567890"));
        contacts.add(new Contacts("PG Secretary(Girls)","Ms. Shruthi P M","1234567890"));
        contacts.add(new Contacts("Ph.D/M.S Secretary","Ms. Nivedha B","1234567890"));
        contacts.add(new Contacts("Joint Secretary","Mr. Arunesh A","1234567890"));
        contactsAdapter = new ContactsAdapter(this,contacts);
        recyclerView.setAdapter(contactsAdapter);
    }

}
