package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeTagsActivity extends AppCompatActivity {

    private static final String TAG = "ChangeTags";

    private Button tagMath;
    private Button tagEnglish;
    private Button tagSocial;
    private Button tagScience;

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
