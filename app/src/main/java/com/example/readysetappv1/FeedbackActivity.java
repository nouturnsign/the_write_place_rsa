package com.example.readysetappv1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = "Feedback";

    private Button openInDocs;
    private Button rateFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        openInDocs = findViewById(R.id.button_open);
        openInDocs.setOnClickListener(this::onClickOpen);
        rateFeedback = findViewById(R.id.RateFeedback);
        rateFeedback.setOnClickListener(this::onClickRateFeedback);

    }
    private void onClickOpen(View v){
        String link = getIntent().getStringExtra("url");
        Uri webpage = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "oop", e);
        }
    }
    private void onClickRateFeedback(View v){
        //dialog, completed = true
        //todo: change the dialog to edittext where the submitter can enter
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rate the Feedback");
        alert.setMessage("if they did bad let them know lol");
        EditText input = new EditText(v.getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Rating (1-10)");
        input.setGravity(Gravity.CENTER);
        input.setMaxLines(1);
        alert.setView(input);
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            int rating = Integer.parseInt(input.getText().toString());
        });
        alert.setNegativeButton("No", (dialogInterface, i) -> {
            Toast.makeText(FeedbackActivity.this, "Come back when you have a rating!", Toast.LENGTH_LONG).show();
            dialogInterface.cancel();
        });
        AlertDialog dialog = alert.create();
        dialog.setOnShowListener(arg0 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        });
        dialog.show();
    }
}