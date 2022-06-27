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
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    final public static String TAG = "ChangePassword";

    private EditText inputPassword;
    private EditText confirmPassword;
    private Button submitNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        inputPassword = findViewById(R.id.edit_input_password);
        confirmPassword = findViewById(R.id.edit_confirm_password);
        submitNewPassword = findViewById(R.id.button_submit_new_password);
        submitNewPassword.setOnClickListener(this::onSubmitPassword);
    }

    private void changePassword(String newPassword) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            Objects.requireNonNull(user);
        } catch (NullPointerException e) {
            Log.w(TAG, "Failed to get current user.", e);
            return;
        }

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Successfully updated user password.");
                        Toast.makeText(ChangePasswordActivity.this, "Changed password.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "Failed to update user password.", task.getException());
                        Toast.makeText(ChangePasswordActivity.this, "Failed to change password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onSubmitPassword(View v) {

        String inputPasswordAsString = inputPassword.getText().toString();
        String confirmPasswordAsString = confirmPassword.getText().toString();

        if (!inputPasswordAsString.equals(confirmPasswordAsString)) {
            Toast.makeText(getApplicationContext(), "Failed: " + inputPasswordAsString + " and " + confirmPasswordAsString + " do not match.", Toast.LENGTH_LONG).show();
        } else {
            changePassword(inputPasswordAsString);
            Intent intent = new Intent(ChangePasswordActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }
}