package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NotificationMessage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextInputLayout messageFrom, messageSubject, messageMain;
    String messageFromTxt, messageSubjectTxt, messageMainTxt;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
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

        reference = FirebaseDatabase.getInstance().getReference("/notifications/"+Information.globalUser);

        //declaring variables
        messageFrom = findViewById(R.id.messageFromTextInput);
        messageSubject = findViewById(R.id.messageSubjectTextInput);
        messageMain = findViewById(R.id.messageWholeTextInputLayout);
        //retrieving data from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messageFromTxt = extras.getString("username");
            messageSubjectTxt = extras.getString("subjectTxt");
            messageMainTxt = extras.getString("messageTxt");
        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show();
        }
        //putting data on activity
        messageFrom.getEditText().setText(messageFromTxt);
        messageSubject.getEditText().setText(messageSubjectTxt);
        messageMain.getEditText().setText(messageMainTxt);

    }

    //passing of data from message para makareply nga naa ng username og subject without using recyclerview
    private void setOnClickListener() {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", messageFromTxt);
        intent.putExtra("subjectTxt", messageSubjectTxt);
        startActivity(intent);
    }

    public void btnCreateMsg(View view) {
        setOnClickListener();
    }

    //deleting data pero ang gibasihan kay ang messagetxt if pareha gani mao ratong linyaha + ang kuyog nga data ang deleton
    public void btnDelete(View view) {

        Query checkUser = reference.orderByChild("messageTxt").equalTo(messageMainTxt);//reference from the username in main activity
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();//to remove data in firebase based from messsageTxt
                    startActivity(new Intent(getApplicationContext(), Notification.class));
                    Toast.makeText(NotificationMessage.this, "Notification Deleted", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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