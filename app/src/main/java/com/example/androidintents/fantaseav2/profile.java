package com.example.androidintents.fantaseav2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //navigation view
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextInputLayout firstname, lastname, address, phonenumber, email;
    TextView profilenamelabel, usernamelabel, lastnamelabel;
    String userTxt, firstnameTxt, lastnameTxt, addressTxt, phoneTxt, emailTxt;
    DatabaseReference reference;
    ImageView profileImage;
    Button btnProfilePicture;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        reference = FirebaseDatabase.getInstance().getReference("appusers");
        storageReference = FirebaseStorage.getInstance().getReference("appusers");

        firstname = findViewById(R.id.firstNameProfile);
        lastname = findViewById(R.id.lastNameProfile);
        address = findViewById(R.id.addressProfile);
        phonenumber = findViewById(R.id.phoneNumberProfile);
        email = findViewById(R.id.emailProfile);
        profilenamelabel = findViewById(R.id.profileNameLabel);
        usernamelabel = findViewById(R.id.profileUsernameLabel);
        lastnamelabel = findViewById(R.id.profileLastNameLabel);
        profileImage = findViewById(R.id.profilePicture);

        profileData();
        //profilepic
        btnProfilePicture = findViewById(R.id.btnChangePic);
        //displaying picture
        StorageReference profileRef = storageReference.child("username/"+Information.globalUser+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        changePicture();
       // usernameRetrieval();
    }

    private void profileData() { //retrieving data from firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appusers");

        Query checkUser = reference.child("ClientID").orderByChild("username").equalTo(Information.globalUser);//reference from the username in main activity
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Information info = snapshot.getValue(Information.class);

                    userTxt = info.getUsername();
                    firstnameTxt = info.getFirstname();
                    lastnameTxt = info.getLastname();
                    addressTxt = info.getAddress();
                    phoneTxt = info.getPhonenumber();
                    emailTxt = info.getEmail();

                    profilenamelabel.setText(firstnameTxt);
                    lastnamelabel.setText(lastnameTxt);
                    usernamelabel.setText(userTxt);
                    firstname.getEditText().setText(firstnameTxt);
                    lastname.getEditText().setText(lastnameTxt);
                    address.getEditText().setText(addressTxt);
                    phonenumber.getEditText().setText(phoneTxt);
                    email.getEditText().setText(emailTxt);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//updating firebase information
    public void btnUpdateProfile(View view) {

        if (isNameChanged() || isLastNameChanged() || isAddressChanged()) {
            profilenamelabel.setText(firstname.getEditText().getText().toString());
            lastnamelabel.setText(lastname.getEditText().getText().toString());
        }
    }

    private boolean isNameChanged() {

        String value = firstname.getEditText().getText().toString();

        if (value.isEmpty()) {
            firstname.setError("Field is empty");
            return false;
        } else if (!firstnameTxt.equals(firstname.getEditText().getText().toString())) {
            reference.child("ClientID").child(Information.globalID).child("firstname").setValue(firstname.getEditText().getText().toString());
            firstnameTxt = firstname.getEditText().getText().toString();
            Toast.makeText(this, "First Name Updated!", Toast.LENGTH_SHORT).show();
            firstname.setError(null);
            isLastNameChanged();
            isAddressChanged();
            return true;
        } else {
            firstname.setError(null);
            return false;
        }
    }

    private boolean isLastNameChanged() {

        String value = lastname.getEditText().getText().toString();

        if (value.isEmpty()) {
            lastname.setError("Field is empty");
            return false;
        } else if(!lastnameTxt.equals(lastname.getEditText().getText().toString())) {
            reference.child("ClientID").child(Information.globalID).child("lastname").setValue(lastname.getEditText().getText().toString());
            lastnameTxt = lastname.getEditText().getText().toString();
            Toast.makeText(this, "Last Name Updated!", Toast.LENGTH_SHORT).show();
            lastname.setError(null);
            isNameChanged();
            isAddressChanged();
            return true;
        } else {
            lastname.setError(null);
            return false;
        }
    }

    private boolean isAddressChanged() {
        String value = address.getEditText().getText().toString();

        if (value.isEmpty()) {
            address.setError("Field is empty");
            return false;
        } else if(!addressTxt.equals(address.getEditText().getText().toString())) {
            reference.child("ClientID").child(Information.globalID).child("address").setValue(address.getEditText().getText().toString());
            addressTxt = address.getEditText().getText().toString();
            Toast.makeText(this, "Address Updated!", Toast.LENGTH_SHORT).show();
            address.setError(null);
            isNameChanged();
            isLastNameChanged();
            return true;
        } else {
            address.setError(null);
            return false;
        }
    }

    private boolean isPhoneChanged() {
        String value = phonenumber.getEditText().getText().toString();

        if (value.isEmpty()) {
            phonenumber.setError("Field is empty");
            return false;
        } else if (value.length() != 10){
            phonenumber.setError("Invalid Phone Number");
            return false;
        } else if(!phoneTxt.equals(phonenumber.getEditText().getText().toString())) {
            reference.child("ClientID").child(Information.globalID).child("phonenumber").setValue(phonenumber.getEditText().getText().toString());
            phoneTxt = phonenumber.getEditText().getText().toString();
            phonenumber.setError(null);
            isNameChanged();
            isLastNameChanged();
            isAddressChanged();
            return true;
        } else {
            phonenumber.setError(null);
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!emailTxt.equals(email.getEditText().getText().toString())) {
            reference.child("ClientID").child(Information.globalID).child("email").setValue(email.getEditText().getText().toString());
            emailTxt = email.getEditText().getText().toString();
            email.setError(null);
            return true;
        } else {
            email.setError(null);
            return false;
        }
    }

    //for profile picture
    private void changePicture() {

        btnProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
               // profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //uploading image to firebase storage
        StorageReference fileRef = storageReference.child("username/"+Information.globalUser+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"Upload Failed",Toast.LENGTH_SHORT).show();
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
    //retrieving username
    private void usernameRetrieval() { //retrieving data from firebase
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("appusers");

        reference2.child("ClientID").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                  //  Information.globalID = dataSnapshot.getKey();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}