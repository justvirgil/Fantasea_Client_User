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
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView profile,traveldestination,messages,notification,transaction;
    FirebaseAuth mAuth;

    TextView usernameLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        profile = (CardView) findViewById(R.id.profileCardView);
        traveldestination = (CardView) findViewById(R.id.travelCardView);
        messages = (CardView) findViewById(R.id.messagesCardView);
        notification = (CardView) findViewById(R.id.notificationCardView);
        transaction = (CardView) findViewById(R.id.transactionCardView);
        usernameLabel = findViewById(R.id.usernameTextViewDB);

        //navigation view


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        //for cardview to be clickable

        profile.setOnClickListener(this);
        traveldestination.setOnClickListener(this);
        messages.setOnClickListener(this);
        notification.setOnClickListener(this);
        transaction.setOnClickListener(this);

        usernameRetrieval();

    }
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //for navigation on click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_travel:
                Intent intent = new Intent(Dashboard.this, Traveldestination.class);
                startActivity(intent);
                break;
            case R.id.nav_transaction:
                Intent intent2 = new Intent(Dashboard.this, Transaction.class);
                startActivity(intent2);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(Dashboard.this, profile.class);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                Intent intent4 = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_about:
                Intent intent5 = new Intent(Dashboard.this, aboutus.class);
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
    //for clicking profile card viewss
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.profileCardView:
                Intent intent1 = new Intent(this,profile.class);
                startActivity(intent1);
                break;
            case R.id.travelCardView:
                Intent intent2 = new Intent(this,Traveldestination.class);
                startActivity(intent2);
                break;
            case R.id.notificationCardView:
                Intent intent4 = new Intent(this,Notification.class);
                startActivity(intent4);
                break;
            case R.id.transactionCardView:
                Intent intent5 = new Intent(this,Transaction.class);
                startActivity(intent5);
                break;
            case R.id.messagesCardView:
                Intent intent6 = new Intent(this, messages.class);
                startActivity(intent6);
                break;
        }
    }
    //authentication
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(Dashboard.this,MainActivity.class));
        }
    }
    //retrieving username
    private void usernameRetrieval() { //retrieving data from firebase
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("appusers");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String UID = currentFirebaseUser.getUid();

        Information.globalID = currentFirebaseUser.getUid();

       // Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();

        reference2.child("ClientID").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Information.globalUser = snapshot.child("username").getValue().toString();

                   usernameLabel.setText(Information.globalUser);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}