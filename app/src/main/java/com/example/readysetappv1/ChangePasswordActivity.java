package com.example.readysetappv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    final public static String TAG = "ChangePassword";
    private Button returnToProfile;
    private EditText password1;
    private EditText password2;
    private Button submitPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        returnToProfile = findViewById(R.id.returnToProfile);
        returnToProfile.setOnClickListener(this::onReturnToProfile);

        password1 = findViewById(R.id.passwordInput);

        password2 = findViewById(R.id.confirmPassword);

        submitPassword = findViewById(R.id.submitNewPassword);
        submitPassword.setOnClickListener(this::onSubmitPassword);

    }

    private void onReturnToProfile(View v) {
        Intent intent = new Intent(ChangePasswordActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void onSubmitPassword(View v) {

        String attemptPassword = password1.getText().toString();
        String confirmPassword = password2.getText().toString();

        if (!attemptPassword.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Failed: " + attemptPassword + " and " + confirmPassword + " do not match.", Toast.LENGTH_LONG).show();
        } else {
            // TODO: Firebase stuff
            onReturnToProfile(v);
        }

    }

}