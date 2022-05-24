package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeTagsActivity extends AppCompatActivity {
    private Button tagMath;
    private Button tagEnglish;
    private Button tagSocial;
    private Button tagScience;
    private Button returnToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tags);
        tagMath = findViewById(R.id.tagMath);
        tagMath.setOnClickListener(this::onClickMath);
        tagEnglish = findViewById(R.id.tagEnglish);
        tagEnglish.setOnClickListener(this::onClickEnglish);
        tagSocial = findViewById(R.id.tagSocial);
        tagSocial.setOnClickListener(this::onClickSocial);
        tagScience = findViewById(R.id.tagScience);
        tagScience.setOnClickListener(this::onClickScience);

        returnToProfile = findViewById(R.id.returnToProfile);
        returnToProfile.setOnClickListener(this::onReturnToProfile);
    }

    private void onReturnToProfile(View v) {
        Intent intent = new Intent(ChangeTagsActivity.this, MenuActivity.class);
        startActivity(intent);
    }
    private void onClickMath(View v){

    }
    private void onClickEnglish(View v){

    }
    private void onClickSocial(View v){

    }
    private void onClickScience(View v){

    }
}
