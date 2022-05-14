package com.example.readysetappv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    // declaration; objects in xml
    private EditText theEmailEditText;
    private EditText theUsernameEditText;
    private EditText thePasswordEditText;
    private Button theSignInButton;
    private Button theRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // initialization
        theEmailEditText = findViewById(R.id.username);
        theUsernameEditText = findViewById(R.id.username3);
        thePasswordEditText = findViewById(R.id.password);
        theSignInButton = findViewById(R.id.login);
        theRegisterButton = findViewById(R.id.login2);

        // both buttons currently just do the same thing
        // TODO: Give sign in and register two separate meanings
        theSignInButton.setOnClickListener(this::onClickSubmit);
        theRegisterButton.setOnClickListener(this::onClickSubmit);
    }

    private void onClickSubmit(View v) {

        String email = theEmailEditText.getText().toString();
        String username = theUsernameEditText.getText().toString();
        String password = thePasswordEditText.getText().toString();

        // TODO: Credentials logic
        boolean isAuthenticated = true;

        if (isAuthenticated) {
            Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
            startActivity(intent);
        } else {
            // TODO: Handle failed authentication
            Toast.makeText(this, "Failed authentication", Toast.LENGTH_SHORT).show();
        }

    }
}