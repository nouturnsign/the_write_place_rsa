package com.example.readysetappv1;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class WorkspaceActivity extends AppCompatActivity {

    private static final String TAG = "Workspace";

    // declaration; objects from xml
    private Button theButton1;
    private Button theButton2;
    private Button theButton3;
    private Button theButton4;
    private Button theButton5;
    private Button theButton6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        // TODO: Use an adapter instead?
        // initialization
        theButton1 = findViewById(R.id.button);
        theButton2 = findViewById(R.id.button2);
        theButton3 = findViewById(R.id.button3);
        theButton4 = findViewById(R.id.button4);
        theButton5 = findViewById(R.id.button5);
        theButton6 = findViewById(R.id.button6);

        // TODO: Know which workspace was selected
        // all just direct to the upload activity
        theButton1.setOnClickListener(this::onClickSubmit);
        theButton2.setOnClickListener(this::onClickSubmit);
        theButton3.setOnClickListener(this::onClickSubmit);
        theButton4.setOnClickListener(this::onClickSubmit);
        theButton5.setOnClickListener(this::onClickSubmit);
        theButton6.setOnClickListener(this::onClickSubmit);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

    }

    private void doMySearch(String query) {
        // does nothing
    }

    private void onClickSubmit(View v) {
        Intent intent = new Intent(WorkspaceActivity.this, MenuActivity.class);
        startActivity(intent);
    }

}