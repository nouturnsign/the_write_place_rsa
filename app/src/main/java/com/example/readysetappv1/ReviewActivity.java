package com.example.readysetappv1;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "Review";

    private Button openInDocs;
    private Button confirmReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        openInDocs = findViewById(R.id.button_open);
        openInDocs.setOnClickListener(this::onClickOpen);
        confirmReview = findViewById(R.id.button_confirm_review);
        confirmReview.setOnClickListener(this::onClickConfirmReview);
    }

    private void onClickOpen(View v) {
        String link = getIntent().getStringExtra("url");
        Uri webpage = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "User probably does not have Google Docs installed.", e);
        }
    }

    private void onClickConfirmReview(View v) {
        //dialog, completed = true
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Finish Review");
        alert.setMessage("Are you done reviewing?");
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            //change field complete to true
            String link = getIntent().getStringExtra("url");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // asynchronously retrieve multiple documents
            Query tagQuery = db
                    .collection("ECG")
                    .whereEqualTo("url", link);
            Task<QuerySnapshot> tagQueryTask = tagQuery.get();
            RunnableTask runnable = new RunnableTask(tagQueryTask);
            Thread thread = new Thread(runnable);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Log.w(TAG, "Failed to join query thread", e);
                return;
            }
            QuerySnapshot tagQuerySnapshot = runnable.getValue();
            DocumentReference confirmReviewDocRef = tagQuerySnapshot.getDocuments().get(0).getReference();
            confirmReviewDocRef.update("complete", true);
        });
        alert.setNegativeButton("No", (dialogInterface, i) -> {
            Toast.makeText(ReviewActivity.this, "Come back when you finish reviewing!", Toast.LENGTH_LONG).show();
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