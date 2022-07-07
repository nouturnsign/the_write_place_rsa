package com.example.readysetappv1;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "Menu";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment;
        int itemId = item.getItemId();
        // effectively a switch statement, but allowing for nonfinal resource ids
        if (itemId == R.id.profile) {
            selectedFragment = new ProfileFragment();
        } else if (itemId == R.id.review) {
            selectedFragment = new ToReviewListFragment();
        } else if (itemId == R.id.upload) {
            selectedFragment = new UploadFragment();
        } else if (itemId == R.id.feedback) {
            selectedFragment = new FeedbackListFragment();
        } else {
            Log.w(TAG, "Failed to get associated menu item.");
            return false; // how? nothing will be selected
        }
        // It will help to replace the
        // one fragment to other.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
        return true;
    }
}