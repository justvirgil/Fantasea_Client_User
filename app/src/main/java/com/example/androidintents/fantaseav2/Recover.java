package com.example.androidintents.fantaseav2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class Recover extends AppCompatActivity {

    TextInputEditText emailAddress;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        emailAddress = findViewById(R.id.emailAdRecoveryText);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

    }

    public void backButtonRecover(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void btnSubmit(View view) {
        resetPassword();

    }

    private void resetPassword() {

        String email = emailAddress.getText().toString().trim();

        if(email.isEmpty()){
            emailAddress.setError("Email is required");
            emailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAddress.setError("Email must be valid");
            emailAddress.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(Recover.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Recover.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Recover.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}