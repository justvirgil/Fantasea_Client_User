package com.example.androidintents.fantaseav2;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Transaction2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    String date_booked, date_scheduled, agency_name, destination_name, activities, destination_province, pump_boat_name, seating_capacity, ticketStatus, payID;
    int price;
    TextView datebkTV, dateschedTV, agencynameTV, desnameTV, desActivitiesTV, desProvinceTV, pumpBoatTV, seatcapTV, payIDTV, ticketTV, priceTV;
    DatabaseReference reference, reference2,reference3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction2);
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

        reference2 = FirebaseDatabase.getInstance().getReference("boatbooking");

        datebkTV = findViewById(R.id.dateBookedTextView);
        dateschedTV = findViewById(R.id.dateScheduledTextView);
        agencynameTV = findViewById(R.id.agencyNameTextView);
        desnameTV = findViewById(R.id.destinationNameTextView);
        desActivitiesTV = findViewById(R.id.destinationActivitesTextView);
        desProvinceTV = findViewById(R.id.destinationProvinceTextView);
        pumpBoatTV = findViewById(R.id.pumpboatNameTextView);
        seatcapTV = findViewById(R.id.seatingCapacityTextView);
        payIDTV = findViewById(R.id.payIDTextView);
        ticketTV = findViewById(R.id.ticketStatusTextView);
        priceTV = findViewById(R.id.basePriceTextView);

        //retrieving data from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date_booked = extras.getString("date_booked");
            date_scheduled = extras.getString("date_scheduled");
            agency_name = extras.getString("agency_name");
            destination_name = extras.getString("destination_name");
            activities = extras.getString("activities");
            destination_province = extras.getString("destination_province");
            pump_boat_name = extras.getString("pump_boat_name");
            seating_capacity = extras.getString("seating_capacity");
            price = extras.getInt("price");
            payID = extras.getString("payID");
            ticketStatus = extras.getString("ticketStatus");
        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show();
        }
        //putting data on activity
        datebkTV.setText(date_booked);
        dateschedTV.setText(date_scheduled);
        agencynameTV.setText(agency_name);
        desnameTV.setText(destination_name);
        desActivitiesTV.setText(activities);
        desProvinceTV.setText(destination_province);
        pumpBoatTV.setText(pump_boat_name);
        seatcapTV.setText(seating_capacity);
        priceTV.setText(String.valueOf(price));
        payIDTV.setText(payID);
        ticketTV.setText(ticketStatus);


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

    public void btnChatAgency(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", agency_name);
        intent.putExtra("subjectTxt", destination_name);
        startActivity(intent);
    }

    public void btnChatBOat(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", pump_boat_name);
        intent.putExtra("subjectTxt", destination_name);
        startActivity(intent);
    }

    public void btnRateUs(View view) {
        String ongoingStatus = "Completed";
        String rejectedStatus = "Rejected";

        if (ticketTV.getText().toString().equals(ongoingStatus)) {
            Toast.makeText(Transaction2.this, "Booking already Complete!", Toast.LENGTH_SHORT).show();
            return;

        } else if (ticketTV.getText().toString().equals(rejectedStatus)) {
            Toast.makeText(Transaction2.this, "Booking is rejected", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(getApplicationContext(), feedback.class);
            intent.putExtra("username", agency_name);
            intent.putExtra("subjectTxt", destination_name);

            afterBooking();
            startActivity(intent);
        }
    }

    private void afterBooking() {
        String completed = "Completed";

        Query checkUser2 = reference2.orderByChild("payID").equalTo(payID);
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String id = dataSnapshot.getKey();

                    reference2.child(id).child("ticketStatus").setValue(completed);
                    Toast.makeText(Transaction2.this, "Booking Complete!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnRateClass(View view) {
        String ongoingStatus = "Ongoing";
        String rejectedStatus = "Rejected";

        if (ticketTV.getText().toString().equals(ongoingStatus)) {
            Toast.makeText(Transaction2.this, "Booking is not yet complete!", Toast.LENGTH_SHORT).show();
            return;
        } else if (ticketTV.getText().toString().equals(rejectedStatus)) {
            Toast.makeText(Transaction2.this, "Booking is rejected", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(getApplicationContext(), feedback.class);
            intent.putExtra("username", agency_name);
            intent.putExtra("subjectTxt", destination_name);
            startActivity(intent);
        }
    }

    public void btnDeleteTransaction(View view) {
        String ongoingStatus = "Ongoing";

        if (ticketTV.getText().toString().equals(ongoingStatus)) {
            Toast.makeText(Transaction2.this, "Cannot Delete Ongoing Booking!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            deleteTransaction();
        }
    }

    private void deleteTransaction() {
        reference3 = FirebaseDatabase.getInstance().getReference("boatbooking");
        Query checkUser = reference3.orderByChild("payID").equalTo(payID);//reference from the username in main activity
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();//to remove data in firebase based from payID
                    startActivity(new Intent(getApplicationContext(), Transaction.class));
                    Toast.makeText(Transaction2.this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btnViewBOat(View view) {
        Intent intent = new Intent(getApplicationContext(), pumpBoatProfile.class);
        intent.putExtra("username",pump_boat_name);
        intent.putExtra("destination_name",destination_name);
        startActivity(intent);
    }
}