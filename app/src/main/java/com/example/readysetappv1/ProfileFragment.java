package com.example.readysetappv1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "Profile";

    private FirebaseStorage pfps;
    private FirebaseUser mUser;
    private ImageView profilePicture;
    private ActivityResultLauncher<Intent> filePicker;
    private TextView displayName;
    private TextView email;
    private Button changePassword;
    private Button signOutButton;
    private Button changeTag;

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
        pfps = FirebaseStorage.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        profilePicture = v.findViewById(R.id.profilePicture);
        changeTag = v.findViewById(R.id.profileTag);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("pfps/"+mUser.getEmail().hashCode()+".jpeg");
        Glide.with(this /* context */).load(storageReference).into(profilePicture);

        profilePicture.setOnClickListener(this::onClickProfilePicture);
        changeTag.setOnClickListener(this::onClickChangeTags);
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
                                uploadPFPToStorage(imageUri);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(imageUri).build();
                                mUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });

                                // InputStream iStream = getContext().getContentResolver().openInputStream(imageUri);
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

    private void uploadPFPToStorage(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("pfps/"+mUser.getEmail().hashCode()+".jpeg");
        UploadTask uploadTask = riversRef.putFile(uri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG,"pfp upload failed", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.v(TAG, "pfp upload success");
            }
        });
    }


    private void onClickChangeTags(View v){
        Intent intent = new Intent(getContext(), ChangeTagsActivity.class);
        startActivity(intent);
    }

    private void onSignOut(View v) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Signed out.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

}