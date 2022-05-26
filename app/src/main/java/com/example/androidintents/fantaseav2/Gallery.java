package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    DatabaseReference reference;
    ImageView firstImage;
    StorageReference storageReference;
    Button btnBook;
    TextView islandActivity,agencyID,agencyName,basePrice,destinationName,destinationProvince,islandNameTextView;
    String activities, agency_id, agency_name, destination_name,destination_province, destination_pic;
    int base_price;


    MyIslandPictureAdapter myIslandPictureAdapter;
    RecyclerView recyclerView;
    ArrayList<UserIslandPicture> list;
    private MyIslandPictureAdapter.RecyclerViewClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

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


        //declaring variables
        islandNameTextView = findViewById(R.id.DestinationIslandTextView);
        islandActivity = findViewById(R.id.DestinationActivitiesTextView21);
        agencyID = findViewById(R.id.DestinationAgencyIDTextView2);
        agencyName = findViewById(R.id.DestinationAgencyNameTextView2);
        destinationName = findViewById(R.id.DestinationNameTextView2);
        destinationProvince = findViewById(R.id.DestinationProvinceTextView2);
        basePrice = findViewById(R.id.DestinationPriceTextView2);
        firstImage = findViewById(R.id.DestinationImageView);
        //getting the data from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            activities = extras.getString("activities");
            agency_id = extras.getString("agency_id");
            agency_name = extras.getString("agency_name");
            destination_name = extras.getString("destination_name");
            destination_province = extras.getString("destination_province");
            destination_pic = extras.getString("destination_pic");
            base_price = extras.getInt("base_price");
        }else{
            Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show();
        }
        //putting data on activity
        islandActivity.setText(activities);
        agencyID.setText(agency_id);
        agencyName.setText(agency_name);
        destinationName.setText(destination_name);
        islandNameTextView.setText(destination_name);
        destinationProvince.setText(destination_province);
        basePrice.setText(String.valueOf(base_price)); // integer
        Picasso.get().load(destination_pic).into(firstImage);
        //islandpicture


        recyclerView = findViewById(R.id.islandRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        islandPictureRetrieval();

    }

    private void islandPictureRetrieval() {
        setOnClickListener();
        list = new ArrayList<>();
        myIslandPictureAdapter = new MyIslandPictureAdapter(this, list, listener);
        recyclerView.setAdapter(myIslandPictureAdapter);
        reference = FirebaseDatabase.getInstance().getReference("gallery/" + agency_name + "/"+ destination_name);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserIslandPicture user = dataSnapshot.getValue(UserIslandPicture.class);
                    list.add(user);

                }
                myIslandPictureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setOnClickListener() {
        listener = (v, Position) -> {
            UserIslandPicture user = list.get(Position);
            Intent intent = new Intent(getApplicationContext(), Gallery2.class);
            intent.putExtra("destination_name", destination_name);
            intent.putExtra("destination_province", destination_province);
            intent.putExtra("gallery_pic_url", user.getGallery_pic_url());
            startActivity(intent);
        };
    }

    public void btnBookNow(View view) {
        Intent intent = new Intent(getApplicationContext(), booking.class);
        intent.putExtra("activities", activities);
        intent.putExtra("agency_name", agency_name);
        intent.putExtra("destination_name", destination_name);
        intent.putExtra("destination_province", destination_province);
        intent.putExtra("base_price", base_price);
        startActivity(intent);
    }

    public void btnMessageNow(View view) {
        messageNow();
    }
    private void messageNow() {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", agency_name);
        intent.putExtra("subjectTxt", destination_name);
        startActivity(intent);
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
}