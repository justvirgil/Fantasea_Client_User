package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingReceipt extends AppCompatActivity {

    TextView activitiesTV, agencyNameTV, basePriceTV, destinationNameTV, destinationProvinceTV, agencyNameTV2, boatNameTV, dateSchedTV, capacityTV, payIDTV, payStateTV;
    TextView btnDateEditTextView;
    String activities, agency_name, destination_name, destination_province, boat_name, status, capacity, agency_username, date_sched, date_sent, payID, payState, base_price_first,ticketStatus;
    int base_price;

    DatabaseReference reference,reference2;
    FirebaseDatabase rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_receipt);

        //reference = FirebaseDatabase.getInstance().getReference("Pending");
        reference2 = FirebaseDatabase.getInstance().getReference("notifications");

        activitiesTV = findViewById(R.id.DestinationActivitiesTextView21);
        agencyNameTV = findViewById(R.id.DestinationAgencyNameTextView2);
        basePriceTV = findViewById(R.id.DestinationPriceTextView2);
        destinationNameTV = findViewById(R.id.DestinationNameTextView2);
        destinationProvinceTV = findViewById(R.id.DestinationProvinceTextView2);
        boatNameTV = findViewById(R.id.boatNameBK2TV);
        capacityTV = findViewById(R.id.capacityBK2TV);
        dateSchedTV = findViewById(R.id.dateSchedBK2TV);
        payIDTV = findViewById(R.id.paymentIDTextView2);
        payStateTV = findViewById(R.id.paymentStateTextView2);

        RetrievingData(); //naa diri ang problema maong di ka proceed
        creatingFirebaseData();

    }

    private void RetrievingData() {
        //getting the data from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activities = extras.getString("activities");
            agency_name = extras.getString("agency_name");
            destination_name = extras.getString("destination_name");
            destination_province = extras.getString("destination_province");
            base_price = extras.getInt("base_price");
            boat_name = extras.getString("boat_name");
            capacity = extras.getString("capacity");
            date_sched = extras.getString("date_sched");
            payID = extras.getString("payID");
            payState = extras.getString("state");

        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_LONG).show();
        }
        //putting data on activity
        //island information
        activitiesTV.setText(activities);
        agencyNameTV.setText(agency_name);
        destinationNameTV.setText(destination_name);
        destinationProvinceTV.setText(destination_province);
        basePriceTV.setText(String.valueOf(base_price));
        //boatinfo
        boatNameTV.setText(boat_name);
        capacityTV.setText(capacity);
        //details
        payIDTV.setText(payID);
        payStateTV.setText(payState);
        dateSchedTV.setText(date_sched);
    }

    private void creatingFirebaseData() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Pending");

        activities = activitiesTV.getText().toString();
        agency_name = agencyNameTV.getText().toString();
        destination_name = destinationNameTV.getText().toString();
        destination_province = destinationProvinceTV.getText().toString();
        boat_name = boatNameTV.getText().toString();
        capacity = capacityTV.getText().toString();
        date_sched = dateSchedTV.getText().toString();
        //sending int to firebase
        base_price_first = basePriceTV.getText().toString();
        base_price = Integer.parseInt(base_price_first);
        payID = payIDTV.getText().toString();

        String username = Information.globalUser;
        date_sent = getTodaysDate();
        ticketStatus = "pending";

        UserPending userPending = new UserPending(username, activities, agency_name, destination_name, destination_province,
                boat_name, capacity, date_sched, date_sent,ticketStatus, payID, base_price);

        String id = reference.push().getKey();
        reference.child(id).setValue(userPending);
        sendAcceptMessage();
        sendAcceptMessageToSelf();
        Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show();
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }
    private String getTimeWithAmAndPm() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    public void btnConfirm(View view) {
        startActivity(new Intent(BookingReceipt.this, Dashboard.class));
    }

    //notifies the pump boat after you book to them
    private void sendAcceptMessage() {

        String messageToTxt = boatNameTV.getText().toString(); // BOAT NAME
        String subjectTxt = "Pending Customer";
        String messageTxt = "Please respond to the booking transaction, Thank you!";

        String username = Information.globalUser;
        date_sent = getTodaysDate();
        String time_sent = getTimeWithAmAndPm();

        UserMessage messageClass = new UserMessage(username, messageToTxt, subjectTxt, messageTxt, date_sent, time_sent);

        String id = reference2.push().getKey();
        reference2.child(messageToTxt).child(id).setValue(messageClass);
    }
    //notifies self after booking
    private void sendAcceptMessageToSelf(){

        String messageToTxt = Information.globalUser; // CUSTOMER NAME
        String subjectTxt = "Booked Successfully";
        String messageTxt = "Please wait for the boat owner to respond!";

        String username = Information.globalUser;
        date_sent = getTodaysDate();
        String time_sent = getTimeWithAmAndPm();

        UserMessage messageClass = new UserMessage(username, messageToTxt, subjectTxt, messageTxt, date_sent, time_sent);

        String id = reference2.push().getKey();
        reference2.child(messageToTxt).child(id).setValue(messageClass);
    }
}