package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

public class Transaction extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    MyBookingAdapter myBookingAdapter;
    RecyclerView recyclerView;
    ArrayList<UserBooking> list;
    MyBookingAdapter.RecyclerViewClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

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

        recyclerView = findViewById(R.id.bookingRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardViewRetrieval();
    }

    private void cardViewRetrieval() {
        list = new ArrayList<>();
        setOnClickListener();
        myBookingAdapter = new MyBookingAdapter(this,list,listener);
        recyclerView.setAdapter(myBookingAdapter);
        //if equal siya sa username then ma detect iyang mga messages

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("/boatbooking/"); //retrieving data way apil ang key

        Query checkUser2 = reference2.orderByChild("customer_name").equalTo(Information.globalUser);//reference from the username in main activity
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserBooking user = dataSnapshot.getValue(UserBooking.class);
                    list.add(user);
                }
                myBookingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem menuItem) {

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
    //passing of current data para once e click ang cardview mo gawas ang info from here to there
    private void setOnClickListener() {
        listener = (v, Position) -> {
            UserBooking user = list.get(Position);
            Intent intent = new Intent(getApplicationContext(), Transaction2.class);
            intent.putExtra("date_booked", user.getDate_booked());
            intent.putExtra("date_scheduled", user.getDate_scheduled());
            intent.putExtra("agency_name", user.getAgency_name());
            intent.putExtra("destination_name", user.getDestination_name());
            intent.putExtra("activities", user.getActivities());
            intent.putExtra("destination_province", user.getDestination_province());
            intent.putExtra("pump_boat_name", user.getPump_boat_name());
            intent.putExtra("seating_capacity", user.getSeating_capacity());
            intent.putExtra("payID", user.getPayID());
            intent.putExtra("ticketStatus", user.getTicketStatus());
            intent.putExtra("price", user.getPrice());
            startActivity(intent);
        };
    }
}