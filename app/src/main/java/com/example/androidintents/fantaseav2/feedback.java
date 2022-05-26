package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class feedback extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextInputLayout messageTo, subjectMain, messageMain;
    TextInputEditText messageToEditText, messageEditText, subjectEditText;
    String messageToTxt, subjectTxt, messageTxt, date_sent, time_sent,rating,ratingbutton,ratingtxt;
    Button btnCreateMsg;
    DatabaseReference reference;
    FirebaseDatabase rootNode;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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

        radioGroup = findViewById(R.id.radioGroupo);

        messageTo = findViewById(R.id.messageToTextInput);
        subjectMain = findViewById(R.id.subjectTextInput);
        messageMain = findViewById(R.id.messageWholeTextInputLayout);

        messageToEditText = findViewById(R.id.messageToTextInputEditText);
        messageEditText = findViewById(R.id.messageInputEditText);
        subjectEditText = findViewById(R.id.messageSubjectInputEditText);

        btnCreateMsg = findViewById(R.id.btnCreateMessage);

        dataRecieverFromMessage();
    }

    private void sendMessage() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("feedback");

        messageToTxt = messageTo.getEditText().getText().toString();
        subjectTxt = subjectMain.getEditText().getText().toString();
        messageTxt = messageMain.getEditText().getText().toString();


        String username = Information.globalUser;
        date_sent = getTodaysDate();
        time_sent = getTimeWithAmAndPm();

        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.radiobutton1:
                        rating = "1";
                        break;
                    case R.id.radiobutton2:
                        rating = "2";
                        break;
                    case R.id.radiobutton3:
                        rating = "3";
                        break;
                    case R.id.radiobutton4:
                        rating = "4";
                        break;
                    case R.id.radiobutton5:
                        rating = "5";
                        break;
                }
            }
        });

        rating = radioButton.getText().toString();

        UserRating userRating = new UserRating(username, messageToTxt, subjectTxt, messageTxt,date_sent,time_sent,rating);

        String id = reference.push().getKey();
        reference.child(messageToTxt).child(id).setValue(userRating);
        Toast.makeText(this, "Rating Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Dashboard.class));
    }

    //receiving data from FromMessage.class without using recyclerview
    private void dataRecieverFromMessage() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messageToTxt = extras.getString("username");
            subjectTxt = extras.getString("subjectTxt");
        }
        messageTo.getEditText().setText(messageToTxt);
        subjectMain.getEditText().setText(subjectTxt);
    }


    private String getTimeWithAmAndPm() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }

    private Boolean validateMessageTo() {
        String value = messageToEditText.getText().toString();

        if (value.isEmpty()) {
            messageToEditText.setError("Field is empty");
            return false;
        } else {
            messageToEditText.setError(null);
            return true;
        }
    }

    private Boolean validateSubject() {
        String value = subjectEditText.getText().toString();

        if (value.isEmpty()) {
            subjectEditText.setError("Field is empty");
            return false;
        } else {
            subjectEditText.setError(null);
            return true;
        }
    }

    private Boolean validateMessage() {
        String value = messageEditText.getText().toString();

        if (value.isEmpty()) {
            messageEditText.setError("Field is empty");
            return false;
        } else {
            messageEditText.setError(null);
            return true;
        }
    }

    //button
    public void btnCreateMsg(View view) {
        if (!validateMessageTo() || !validateSubject() || !validateMessage()) {
            return;
        }
        Toast.makeText(this, "Message Created Succesfully", Toast.LENGTH_SHORT).show();
        sendMessage();
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