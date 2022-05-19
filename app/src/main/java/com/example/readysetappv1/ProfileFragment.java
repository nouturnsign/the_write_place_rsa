package com.example.readysetappv1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    final private static String TAG = "Profile Fragment";
    private FirebaseUser mUser;
    private ImageView profilePicture;
    private ActivityResultLauncher<Intent> filePicker;
    private TextView displayName;
    private TextView email;
    private Button changePassword;
    private Button signOutButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        profilePicture = v.findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(this::onClickProfilePicture);
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Here, no request code
                            Intent data = result.getData();
                            try {
                                Uri imageUri = data.getData();
                                InputStream iStream = getContext().getContentResolver().openInputStream(imageUri);
                                // TODO: save InputStream iStream to storage
                                Log.d(TAG, "uploadPFP:success");
                            } catch (Exception e) {
                                Log.e(TAG, "uploadPFP:failure", e);
                            }
                        } else {
                            Log.w(TAG, "uploadPFP:failure");
                        }
                    }
                });

        displayName = v.findViewById(R.id.profileUsername);
        try {
            displayName.setText(getString(R.string.username).concat(" ").concat(Objects.requireNonNull(mUser.getDisplayName())));
            Log.d(TAG, "displayName:success");
        } catch (NullPointerException e) {
            Log.e(TAG, "displayName:failure", e);
        }

        email = v.findViewById(R.id.profileEmail);
        try {
            email.setText(getString(R.string.email).concat(" ").concat(Objects.requireNonNull(mUser.getEmail())));
            Log.d(TAG, "email:success");
        } catch (NullPointerException e) {
            Log.e(TAG, "email:failure", e);
        }

        changePassword = v.findViewById(R.id.profileChangePassword);
        changePassword.setOnClickListener(this::onChangePassword);

        signOutButton = v.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this::onSignOut);

        return v;
    }

    private void onClickProfilePicture(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        filePicker.launch(intent);
    }

    private void onChangePassword(View v) {
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void onSignOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Signed out.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

}