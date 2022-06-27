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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "Review";
    private Button openInDocs;
    private Button cosmetic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        openInDocs = findViewById(R.id.Open);
        cosmetic = findViewById(R.id.Cosmetic); //cosmetic = confirm
        openInDocs.setOnClickListener(this::onClickOpen);
        cosmetic.setOnClickListener(this::onClickCosmetic);

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
    private void onClickCosmetic(View v){
        //dialog, completed = true
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("uwu");
            alert.setMessage("Are you done reviewing?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                    //change field complete to true
                    String link = getIntent().getStringExtra("url");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // asynchronously retrieve multiple documents
                    Query tagQuery = db.collection("ECG")
                            .whereEqualTo("url", link);
                    Task<QuerySnapshot> tagQueryTask = tagQuery.get();
                    RunnableTask runnable = new RunnableTask(tagQueryTask);
                    Thread thread = new Thread(runnable);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    QuerySnapshot tagQuerySnapshot = runnable.getValue();
                    DocumentReference confirmReviewDocRef = tagQuerySnapshot.getDocuments().get(0).getReference();
                    confirmReviewDocRef.update("complete",true);
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ReviewActivity.this, "Come back when you finish reviewing!", Toast.LENGTH_LONG).show();
                }
            });
            alert.create().show();
    }
}