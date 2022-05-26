package com.example.androidintents.fantaseav2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    TextInputEditText usernameSignUp, firstNameSignUp, lastNameSignUp, phoneNumberSignUp, emailSignUp, passwordSignUp, addressSignUp;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseAuth mAuth;

    String usernameString, phoneString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameSignUp = findViewById(R.id.usernameSignUp);
        firstNameSignUp = findViewById(R.id.firstNameSignUp);
        lastNameSignUp = findViewById(R.id.lastNameSignUp);
        phoneNumberSignUp = findViewById(R.id.phoneNumberSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        addressSignUp = findViewById(R.id.addressSignUp);

        mAuth = FirebaseAuth.getInstance();
        usernameString = usernameSignUp.getText().toString();
        phoneString = phoneNumberSignUp.getText().toString();
    }

    public void backButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnMainPage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnCreateAccount(View view) {

        if (!validateUsername() || !validateFirstName() || !validateLastName() || !validateAddress() || !validatePhoneNumber() || !validateEmailAdd() || !validatePassword()) {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }else {
            validationRetrieval();
        }
        //  createUser();
        //validationDuplicates();
    }


    public void createUser() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("appusers");
        String email = emailSignUp.getText().toString();
        String password = passwordSignUp.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String username = usernameSignUp.getText().toString();
                    String firstname = firstNameSignUp.getText().toString();
                    String lastname = lastNameSignUp.getText().toString();
                    String address = addressSignUp.getText().toString();
                    String phonenumber = phoneNumberSignUp.getText().toString();
                    String email = emailSignUp.getText().toString();
                    String password = passwordSignUp.getText().toString();

                    UserHelperClass helperClass = new UserHelperClass(username, firstname, lastname, phonenumber, email, password, address);

                    reference.child("ClientID").child(mAuth.getUid()).setValue(helperClass);
                    // reference.child(username).setValue(helperClass);
                    Toast.makeText(SignUp.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                } else {
                    Toast.makeText(SignUp.this, "Registration failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private Boolean validateUsername() {
        String value = usernameSignUp.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (value.isEmpty()) {
            usernameSignUp.setError("Field is empty");
            return false;
        } else if (value.length() >= 15) {
            usernameSignUp.setError("Username too long");
            return false;
        } else if (!value.matches(noWhiteSpace)) {
            usernameSignUp.setError("White Spaces are not allowed");
            return false;
        } else {
            usernameSignUp.setError(null);
            return true;
        }
    }

    private Boolean validateFirstName() {
        String value = firstNameSignUp.getText().toString();

        if (value.isEmpty()) {
            firstNameSignUp.setError("Field is empty");
            return false;
        } else {
            firstNameSignUp.setError(null);
            return true;
        }
    }

    private Boolean validateLastName() {
        String value = lastNameSignUp.getText().toString();

        if (value.isEmpty()) {
            lastNameSignUp.setError("Field is empty");
            return false;
        } else {
            lastNameSignUp.setError(null);
            return true;
        }
    }

    private Boolean validateAddress() {
        String value = addressSignUp.getText().toString();

        if (value.isEmpty()) {
            addressSignUp.setError("Field is empty");
            return false;
        } else {
            addressSignUp.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String value = phoneNumberSignUp.getText().toString();

        if (value.isEmpty()) {
            phoneNumberSignUp.setError("Field is empty");
            return false;
        } else if (value.length() > 10) {
            phoneNumberSignUp.setError("Invalid Phone Number");
            return false;
        } else {
            phoneNumberSignUp.setError(null);
            return true;
        }
    }

    private Boolean validateEmailAdd() {
        String value = emailSignUp.getText().toString();
        String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (value.isEmpty()) {
            emailSignUp.setError("Field is empty");
            return false;
        } else if (!value.matches(emailPat)) {
            emailSignUp.setError("Invalid Email Address");
            return false;
        } else {
            emailSignUp.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = passwordSignUp.getText().toString();
        String passwordVal = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{4,}$";
        if (value.isEmpty()) {
            passwordSignUp.setError("Field is empty");
            return false;
        } else if (!value.matches(passwordVal)) {
            passwordSignUp.setError("Password is too weak");
            return false;
        } else {
            passwordSignUp.setError(null);
            return true;
        }
    }

    //validating username/phonenumber
    private void validationRetrieval() { //retrieving data from firebase
        String usernameString = usernameSignUp.getText().toString();
        String phoneString = phoneNumberSignUp.getText().toString();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("appusers");

        reference2.child("ClientID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (usernameString.equals(dataSnapshot.child("username").getValue())) {

                            if (phoneString.equals(dataSnapshot.child("phonenumber").getValue())) {

                                phoneNumberSignUp.setError("Phone Number Already Exist!");
                                phoneNumberSignUp.requestFocus();
                            }else{
                                usernameSignUp.setError("Username Already Exist!");
                                usernameSignUp.requestFocus();
                            }
                            return;

                        } if (phoneString.equals(dataSnapshot.child("phonenumber").getValue())) {

                            phoneNumberSignUp.setError("Phone Number Already Exist!");
                            phoneNumberSignUp.requestFocus();
                            return;

                        }
                    }
                    phoneNumberSignUp.setError(null);
                    usernameSignUp.setError(null);
                    createUser();
                } else {
                    createUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}