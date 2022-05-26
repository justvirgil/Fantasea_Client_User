package com.example.androidintents.fantaseav2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;

public class PaymentMethod extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView activitiesTV, agencyNameTV, basePriceTV, destinationNameTV, destinationProvinceTV, agencyNameTV2, boatNameTV, statusTV, capacityTV;
    TextView btnDateEditTextView;
    String activities, agency_name, destination_name, destination_province, boat_name, status, capacity, agency_username, date_sent, date_sched, payID, state;
    int base_price;

    public static final String clientKey = "AVDUNhuDgUGWTkP7P0dyKSwyyGyUDXUHypxmTmz3JcJMHH2Rh2OdIKI9WxCXBOUvnqZrOUkmO1JN1fWE";//justvirgil acc
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready,
            // switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            // on below line we are passing a client id.
            .clientId(clientKey);
    private EditText amountEdt;
    private TextView paymentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

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

        btnDateEditTextView = findViewById(R.id.dateSchedBK2TV);

        activitiesTV = findViewById(R.id.DestinationActivitiesTextView21);
        agencyNameTV = findViewById(R.id.DestinationAgencyNameTextView2);
        basePriceTV = findViewById(R.id.DestinationPriceTextView2);
        destinationNameTV = findViewById(R.id.DestinationNameTextView2);
        destinationProvinceTV = findViewById(R.id.DestinationProvinceTextView2);
        agencyNameTV2 = findViewById(R.id.agencyNameBK2TV);
        boatNameTV = findViewById(R.id.boatNameBK2TV);
        statusTV = findViewById(R.id.statusBK2TV);
        capacityTV = findViewById(R.id.capacityBK2TV);

        CalendarSched();
        RetrievingData();


        //paypal
        // on below line we are initializing our variables.
        amountEdt = findViewById(R.id.idEdtAmount);
        amountEdt.setText(String.valueOf(base_price));
        // creating a variable for button, edit text and status tv.
        Button makePaymentBtn = findViewById(R.id.idBtnPay);
        paymentTV = findViewById(R.id.idTVStatus);

        // on below line adding click listener to our make payment button.
        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to get payment.
                if(!validateDateSchedule()){
                    return;
                }
                getPayment();
                //passingData();
            }
        });
    }


    private void getPayment() {

        // Getting the amount from editText
        String amount = amountEdt.getText().toString();

        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Booking Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        payID = payObj.getJSONObject("response").getString("id");
                        state = payObj.getJSONObject("response").getString("state");
                        //sending of data
                       // passingData();//mo gana rani ang pag recieve moy error
                       // paymentTV.setText("Payment Successful, Please get your ticket");
                        Intent intent = new Intent(getApplicationContext(), BookingReceipt.class);
                        intent.putExtra("activities", activities);
                        intent.putExtra("agency_name", agency_name);
                        intent.putExtra("destination_name", destination_name);
                        intent.putExtra("destination_province", destination_province);
                        intent.putExtra("base_price", base_price);
                        intent.putExtra("boat_name", boat_name);
                        intent.putExtra("capacity", capacity);
                        intent.putExtra("date_sched", date_sched);
                        intent.putExtra("payID", payID);
                        intent.putExtra("state", state);
                        startActivity(intent);
                        //ari ibutang ang passingdata()

                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
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
            status = extras.getString("status");
            capacity = extras.getString("capacity");
            agency_username = extras.getString("agency_username");

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
        agencyNameTV2.setText(agency_username);
        boatNameTV.setText(boat_name);
        statusTV.setText(status);
        capacityTV.setText(capacity);
        //datesched


    }
    //already declared


    private void CalendarSched() {
        //for calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnDateEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PaymentMethod.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        btnDateEditTextView.setText(date);
                        date_sched = btnDateEditTextView.getText().toString();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }



    private Boolean validateDateSchedule() {
        String value = btnDateEditTextView.getText().toString();

        if (value.isEmpty()) {
            btnDateEditTextView.setError("Field is empty");
            return false;
        } else {
            btnDateEditTextView.setError(null);
            return true;
        }
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

    public void btnMessageBoat(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateMessage.class);
        intent.putExtra("username", boat_name);
        intent.putExtra("subjectTxt", destination_name);
        startActivity(intent);
    }
}