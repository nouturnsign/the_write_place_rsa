package com.example.readysetappv1;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.function.Consumer;

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
        tagMath.setOnClickListener(tagDialogBuilder("math"));
        tagEnglish = findViewById(R.id.tagEnglish);
        tagEnglish.setOnClickListener(tagDialogBuilder("eng"));
        tagSocial = findViewById(R.id.tagSocial);
        tagSocial.setOnClickListener(tagDialogBuilder("hist"));
        tagScience = findViewById(R.id.tagScience);
        tagScience.setOnClickListener(tagDialogBuilder("science"));
    }

    private View.OnClickListener tagDialogBuilder(String tag) {
        return new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ChangeTagsActivity.this);
                alert.setTitle(tag + " Tag");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String username = user.getDisplayName();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document(username);
                alert.setPositiveButton("Yes", (dialogInterface, i) -> {
                    docRef.update(tag, true);
                    Toast.makeText(ChangeTagsActivity.this, tag + " tag set to true", Toast.LENGTH_LONG);
                });
                alert.setNegativeButton("No", (dialogInterface, i) -> {
                    docRef.update(tag, false);
                    Toast.makeText(ChangeTagsActivity.this, tag + " tag set to false", Toast.LENGTH_LONG).show();
                    dialogInterface.cancel();
                });
                AlertDialog dialog = alert.create();
                dialog.setOnShowListener(arg0 -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                });
                dialog.show();
            }
        };
    }}
