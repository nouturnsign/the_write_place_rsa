package com.example.readysetappv1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        // TODO: Set menu to middle slot
        // This is currently just doing the leftmost!
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReviewListFragment()).commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.review:
                selectedFragment = new ReviewListFragment();
                break;
            case R.id.upload:
                selectedFragment = new UploadFragment();
                break;
            case R.id.feedback:
                selectedFragment = new FeedbackListFragment();
                break;
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