package com.example.androidintents.fantaseav2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class booking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    DatePickerDialog.OnDateSetListener setListener;
    TextView activitiesTV,agencyNameTV,basePriceTV,destinationNameTV,destinationProvinceTV;
    int base_price,seating_capacity;
    String username,date_booked,time_booked,date_scheduled,agency_name,destination_name,destination_activities,destination_province,pump_boat_name,boat_owner_name,seating_capacitySTR,base_priceSTR;
    DatabaseReference reference;

    MyBoatAdapter myBoatAdapter;//name of adapter
    RecyclerView recyclerView;
    ArrayList<UserBoat> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //navigation view
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);//
        navigationView.setCheckedItem(R.id.nav_home);
        //navigation view

        //assigning variables
        reference = FirebaseDatabase.getInstance().getReference("/queue");

        recyclerView = findViewById(R.id.boatRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activitiesTV = findViewById(R.id.DestinationActivitiesTextView21);
        agencyNameTV = findViewById(R.id.DestinationAgencyNameTextView2);
        basePriceTV = findViewById(R.id.DestinationPriceTextView2);
        destinationNameTV = findViewById(R.id.DestinationNameTextView2);
        destinationProvinceTV = findViewById(R.id.DestinationProvinceTextView2);


        //getting the data from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            destination_activities = extras.getString("activities");
            agency_name = extras.getString("agency_name");
            destination_name = extras.getString("destination_name");
            destination_province = extras.getString("destination_province");
            base_price = extras.getInt("base_price");
        }else{
            Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show();
        }
        //putting data on activity
        activitiesTV.setText(destination_activities);
        agencyNameTV.setText(agency_name);
        destinationNameTV.setText(destination_name);
        destinationProvinceTV.setText(destination_province);
        basePriceTV.setText(String.valueOf(base_price));

        cardViewRetrieval();
        setOnClickListener();
    }

    private void cardViewRetrieval() {
        list = new ArrayList<>();
        myBoatAdapter = new MyBoatAdapter(this,list, myBoatAdapter.listener);
        recyclerView.setAdapter(myBoatAdapter);

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("/queue/" + agency_name); //retrieving data way apil ang key

        Query checkUser2 = reference2.orderByChild("boat_name").equalTo(agency_name);//reference from the username in main activity

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserBoat user = dataSnapshot.getValue(UserBoat.class);
                    list.add(user);
                }
                myBoatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //passing of data from cardview once press/click
    private void setOnClickListener() {
        myBoatAdapter.listener = new MyBoatAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int Position) {
                UserBoat user = list.get(Position);
                Intent intent = new Intent(getApplicationContext(), PaymentMethod.class);
                intent.putExtra("agency_username", user.getAgency_username());
                intent.putExtra("boat_name", user.getBoat_name());
                intent.putExtra("capacity", user.getCapacity());
                intent.putExtra("status", user.getStatus());
                intent.putExtra("activities", destination_activities);
                intent.putExtra("agency_name", agency_name);
                intent.putExtra("destination_name", destination_name);
                intent.putExtra("destination_province", destination_province);
                intent.putExtra("base_price", base_price);
                startActivity(intent);
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent6 = new Intent(this, Dashboard.class);
                startActivity(intent6);
                break;
            case R.id.nav_travel:
                Intent intent = new Intent(this, Traveldestination.class);
                startActivity(intent);
                break;
            case R.id.nav_transaction:
                Intent intent2 = new Intent(this, Transaction.class);
                startActivity(intent2);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(this, profile.class);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(this, MainActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_about:
                Intent intent5 = new Intent(this, aboutus.class);
                startActivity(intent5);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_report:
                Intent intent7 = new Intent(this, reportMessage.class);
                startActivity(intent7);
                break;
            case R.id.nav_notification:
                Intent intent8 = new Intent(this, Notification.class);
                startActivity(intent8);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void btnMessageNow(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", agency_name);
        intent.putExtra("subjectTxt", destination_name);
        startActivity(intent);
    }
}