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

import com.example.nittrichy.Adapters.PlacesAdapter;
import com.example.nittrichy.Models.Places;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PlacesAdapter placesAdapter;
    int placeType;
    Toolbar toolbar;
    ArrayList<Places> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        placeType = getIntent().getIntExtra("placeType",0);

        recyclerView = findViewById(R.id.placesView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        places = new ArrayList<>();
        String title = "";
        switch (placeType){
            case 0:
                title = "Shopping Complex Zone";
                fillShop();

                break;
            case 1:
                title = "Institute Complex Zone";
                fillInstitute();
                break;
            case 2:
                title = "Boys Hostel Zone";
                fillBoys();
                break;
            case 3:
                title = "Girls Hostel Zone";
                fillGirls();
                break;


        }
        getSupportActionBar().setTitle(title);
        placesAdapter = new PlacesAdapter(PlacesActivity.this,places);
        recyclerView.setAdapter(placesAdapter);

    }
    private void fillShop(){
        places.clear();
        places.add(new Places("SCZ 03","2K SuperMarket","9AM-\n9PM","M/s Kavya Needs",""));
        places.add(new Places("SCZ 07","Books and Stationary Shop","9AM-\n9PM","M/s Amritha  Agencies","9791777601"));
        places.add(new Places("SCZ 08","Tailoring Shop","9AM-\n9PM","M/s Kumar Tailoring",""));
        places.add(new Places("SCZ 09","Barber Shop","9AM-\n9PM","M/s Murugan Salon",""));
        places.add(new Places("SCZ 10","Laundry Shop","9AM-\n9PM","M/s Snow White Laundry",""));
        places.add(new Places("SCZ 11","Dimora","6AM-\n10PM","Mr A Muruganathan",""));
        places.add(new Places("SCZ 12","Fresh Juice Shop","9AM-\n9PM","M/s R K Fresh Juice Shop","9994005393"));
        places.add(new Places("SCZ 13","Vegetable and Fruits Shop","9AM-\n9PM","M/s Strawberry Vegetables","9361758443"));
        places.add(new Places("SCZ 15","Bakery & Snacks","9AM-\n9PM","M/s Nirmala Industries",""));
        places.add(new Places("SCZ 17","Xerox & Printing Shop","9AM-\n9PM","M/s Esspee Comprint","9334055669"));
        places.add(new Places("SCZ 18","Photo Studio","9AM-\n9PM","M/s Bright Star Studio",""));
        places.add(new Places("SCZ 19","Mobile Recharge Shop","9AM-\n9PM","M/s JPS Computer Needs","8903327289"));


    }

    private void fillInstitute(){
        places.clear();
        places.add(new Places("INZ 06","Tea & Snacks Stall","8AM-\n5PM","M/s Krishna Foods\nOrion Backside","9894375869"));
        places.add(new Places("INZ 08","Fast Food Canteen","9:30AM-7PM","M/s Classic Foods\nBackside EEE Dept","9842814083"));
        places.add(new Places("INZ 09","Tea & Snacks Stall","8:30AM-5:30PM","M/s Sona Traders\nNear ICE Dept","9842402026"));
        places.add(new Places("INZ 11","Bicycle Repair Shop","10AM-\n7PM","M/s Sree Venkata Cycles\nShopping Complex",""));
        places.add(new Places("INZ 10","Shoe & Bag Repair Shop","11AM-\n9PM","M/s V Subramani Shoe Repair\nShopping Complex","9994250375"));

    }
    private void fillBoys(){
        places.clear();
        places.add(new Places("BHZ 03","Coke Station","8AM-\n8PM","M/s SJ Foods\nNear Agate Hostel","9626390321"));
        places.add(new Places("BHZ 05","Chaat Shop","3PM-\n2AM","M/s Chaat Khazana\nNaturals Backside","9600237392"));
        places.add(new Places("BHZ 10","Tea & Snacks Stall","7PM-\n2AM","M/s Vinayaka Tea Stall\nNear MegaMess II","9443049383"));
        places.add(new Places("BHZ 11","Xerox & Printing","8:30AM-10:30PM","M/s Star Xerox\nOpp. MegaMess I","9566852526"));
        places.add(new Places("BHZ 13","Laundry Shop","8AM-\n9PM","M/s Chellam Laundry\nOpp. MegaMess I","9976923208"));
        places.add(new Places("BHZ 15","Fresh Juice Shop","9AM-\n2AM","M/s D2 Restaurant\nOpp. MegaMess I","8754315334"));
        places.add(new Places("BHZ 13","Bicycle Repair Shop","8AM-\n7PM","M/s Annai Cycle\nOpp. MegaMess I","9597661865"));
        places.add(new Places("","Naturals","10AM-\n9PM","M/s Predeepa",""));



    }
    private void fillGirls(){
        places.clear();
        places.add(new Places("GHZ 04","Laundry Shop","8:30AM-7:30PM","M/s Bio Care\nOpal Hostel","9786155755"));
        places.add(new Places("GHZ 06","Cycle Repair Shop","9AM-\n8PM","M/s Nethaji Cycle\nOpal Hostel","9597104006"));
        places.add(new Places("GHZ 03","Thozhar Xerox Shop","9:30AM-7PM","Opal Hostel","848931193"));
        places.add(new Places("","Shakti Food Court","6PM-\n2AM","Opal Hostel",""));

    }

}
