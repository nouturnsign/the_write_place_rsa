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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    // declaration; objects in xml
    private EditText editEmail;
    private EditText editUsername;
    private EditText editPassword;
    private Button signIn;
    private Button register;
    private Button resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialization
        editEmail = findViewById(R.id.edit_email);

        editUsername = findViewById(R.id.edit_username);

        editPassword = findViewById(R.id.edit_password);

        signIn = findViewById(R.id.button_sign_in);
        signIn.setOnClickListener(this::onClickLogin);

        register = findViewById(R.id.button_register);
        register.setOnClickListener(this::onClickRegister);

        resetPassword = findViewById(R.id.button_reset_password);
        resetPassword.setOnClickListener(this::onResetPassword);

    }

    private void createAccount(String email, String password, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> tags = new HashMap<>();
        tags.put("English",true);
        tags.put("Math",true);
        tags.put("Social",true);
        tags.put("Science",true);
        final String WORKSPACE = "Users";
        db.collection(WORKSPACE).document(username).set(tags)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "tags created!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error creating tags+", e);
                    }
                });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg")) TODO: pfp goes here
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                });
                        // Goes to workspace activity
                        Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickRegister(View v) {
        String email = editEmail.getText().toString();
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        createAccount(email, password, username);
    }

    private void verifyCredentials(String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        // Goes to workspace activity
                        Intent intent = new Intent(LoginActivity.this, WorkspaceActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onClickLogin(View v) {
        String email = editEmail.getText().toString();
        String username = editUsername.getText().toString(); // TODO: allow username-password login
        String password = editPassword.getText().toString();
        verifyCredentials(email, password);
    }

    private void onResetPassword(View v) {

        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);

    }

}