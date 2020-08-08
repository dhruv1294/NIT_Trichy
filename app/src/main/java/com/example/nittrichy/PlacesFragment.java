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
public class PlacesFragment extends Fragment {
    CardView shoppingView,instituteView,boysView,girlsView;



    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_places, container, false);
        shoppingView = v.findViewById(R.id.shopping);
        instituteView = v.findViewById(R.id.institute);
        boysView = v.findViewById(R.id.boys);
        girlsView = v.findViewById(R.id.girls);
        shoppingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PlacesActivity.class);
                intent.putExtra("placeType",0);
                startActivity(intent);

            }
        });
        instituteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PlacesActivity.class);
                intent.putExtra("placeType",1);
                startActivity(intent);

            }
        });
        boysView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PlacesActivity.class);
                intent.putExtra("placeType",2);
                startActivity(intent);

            }
        });
        girlsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PlacesActivity.class);
                intent.putExtra("placeType",3);
                startActivity(intent);

            }
        });

        return v;

    }

}
