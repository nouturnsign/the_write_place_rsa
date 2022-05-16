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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    final private static String TAG = "Login";
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        // initialization
        theEmailEditText = findViewById(R.id.username);
        theUsernameEditText = findViewById(R.id.username3);
        thePasswordEditText = findViewById(R.id.password);
        theSignInButton = findViewById(R.id.login);
        theRegisterButton = findViewById(R.id.login2);
        theSignInButton.setOnClickListener(this::onClickLogin);
        theRegisterButton.setOnClickListener(this::onClickRegister);
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Goes to workspace activity
                            Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickRegister(View v) {
        String email = theEmailEditText.getText().toString();
        String username = theUsernameEditText.getText().toString();
        // TODO: display username inside app (profile page?)
        String password = thePasswordEditText.getText().toString();
        createAccount(email, password);
    }

    private void verifyCredentials(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Goes to workspace activity
                            Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickLogin(View v) {
        String email = theEmailEditText.getText().toString();
        String username = theUsernameEditText.getText().toString();
        String password = thePasswordEditText.getText().toString();
        verifyCredentials(email, password);
    }

}