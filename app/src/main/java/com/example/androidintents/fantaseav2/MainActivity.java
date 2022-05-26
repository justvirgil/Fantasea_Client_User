package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    TextInputLayout usernameSignIn, passwordSignIn;
    //TextInputEditText usernameSignIn, passwordSignIn;
    MaterialButton btnSignIn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameSignIn = findViewById(R.id.usernameLayout);
        passwordSignIn = findViewById(R.id.passwordLayout);
        btnSignIn = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

    }

    private Boolean validateUsername() {
        String value = usernameSignIn.getEditText().getText().toString();
        if (value.isEmpty()) {
            usernameSignIn.setError("Field is empty");
            return false;
        } else {
            usernameSignIn.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = passwordSignIn.getEditText().getText().toString();
        if (value.isEmpty()) {
            passwordSignIn.setError("Field is empty");
            return false;
        } else {
            passwordSignIn.setError(null);
            return true;
        }
    }

    public void btnSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void btnForgotPassword(View view) {
        Intent intent = new Intent(this, Recover.class);
        startActivity(intent);
    }

    public void btnLogin(View view) {
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            loginUser2();
            //loginUser();
            //isUser();
        }
    }

    //what if ang information global user kay database reference for the actual username :)
    private void loginUser() {
        String email = usernameSignIn.getEditText().getText().toString();
        String password = passwordSignIn.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Log In Succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                } else {
                    Toast.makeText(MainActivity.this, "Loggin failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        })
        ;
    }

    private void isUser() {
        //Information.globalUser = usernameSignIn.getEditText().getText().toString();
        String userEnteredPassword = passwordSignIn.getEditText().getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appusers");

        Query checkUser = reference.orderByChild("username").equalTo(Information.globalUser);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    usernameSignIn.setError(null);

                    String passwordFromDB = dataSnapshot.child(Information.globalUser).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        usernameSignIn.setError(null);

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                        startActivity(intent);
                    } else {
                        passwordSignIn.setError("Wrong Password");
                        passwordSignIn.requestFocus();
                    }
                } else {
                    usernameSignIn.setError("Username does not exist");
                    usernameSignIn.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //what if ang information global user kay database reference for the actual username :)
    private void loginUser2() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("appusers").child("ClientID");
        String email1 = usernameSignIn.getEditText().getText().toString();

        reference.orderByChild("email").equalTo(email1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loginUser();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please Create A Valid Account!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}