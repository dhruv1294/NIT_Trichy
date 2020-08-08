package com.example.nittrichy;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    CardView StudentCouncil,TechnicalCouncil,TelephoneDirectory;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_contacts, container, false);
        StudentCouncil = v.findViewById(R.id.studentCouncil);
        TechnicalCouncil = v.findViewById(R.id.technicalCouncil);
        TelephoneDirectory = v.findViewById(R.id.TelephoneDirectory);
        StudentCouncil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ContactsActivity.class);
                intent.putExtra("council","student");
                startActivity(intent);
            }
        });
        TelephoneDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PDFViewActivity.class);
                intent.putExtra("name","Telephone Directory");
                intent.putExtra("url","https://www.nitt.edu/home/students/facilitiesnservices/ccs/Internal-Telephone-Directory-May2019.pdf");
                startActivity(intent);

            }
        });
        TechnicalCouncil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ContactsActivity.class);
                intent.putExtra("council","tech");
                startActivity(intent);

            }
        });
        return v;
    }

}
