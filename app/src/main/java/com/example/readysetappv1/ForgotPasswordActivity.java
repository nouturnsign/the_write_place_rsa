package com.example.readysetappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";

    private EditText emailToSendTo;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToSendTo = findViewById(R.id.emailToSendTo);
        submitButton = findViewById(R.id.submitEmail);
        submitButton.setOnClickListener(this::onSubmitEmail);

    }

    private void sendForgotPasswordEmail(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "Email failed to send.", task.getException());
                            Toast.makeText(ForgotPasswordActivity.this, "Invalid email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onSubmitEmail(View v) {
        String emailAddress = emailToSendTo.getText().toString();
        sendForgotPasswordEmail(emailAddress);
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}