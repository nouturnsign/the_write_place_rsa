package com.example.readysetappv1;

import static java.lang.Boolean.getBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OtherEssaysActivity extends AppCompatActivity implements EssayListAdapter.ItemClickListener {

    private static final String TAG = "OtherEssaysActivity";
    String username;
    private FirebaseUser mUser;
    private List<Map<String, String>> mDatabaseEssays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_essays);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            Objects.requireNonNull(mUser);
        } catch (NullPointerException e) {
            Log.w(TAG, "User display name is null for some reason, maybe not signed in?", e);
            return;
        }

        // set up the recycler view
        RecyclerView mRecyclerView = findViewById(R.id.myRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // define query
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        username = mUser.getDisplayName();
        DocumentReference userRef = db.collection("Users").document(username);
        Task<DocumentSnapshot> userDocTask = userRef.get();
        userDocTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Successfully retrieved essays from database");
            } else {
                Log.i(TAG, "Failed to retrieve essays from database");
            }
        });
        RunnableTaskDocumentSnapshot runnable = new RunnableTaskDocumentSnapshot(userDocTask);
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.w(TAG, "Failed to make document snapshot", e);
        }
        DocumentSnapshot documentSnapshot = runnable.getValue();
        List<String> tagList = Arrays.asList("math", "eng", "hist", "science");
        List<String> userTags = new ArrayList<>();
        for (String tag: tagList) {
            boolean tagStatus = documentSnapshot.getBoolean(tag);
            Log.e(TAG,tag+tagStatus);
            if (tagStatus==true) {
                userTags.add(tag);
            }
        }
        //Query  = citiesRef.whereIn("tag", x);


        Query tagQuery = db
                .collection("ECG") // TODO: make this vary by workspace and tag
                .whereEqualTo("reviewer", null)
                .whereNotEqualTo("submitter", mUser.getDisplayName())
                .whereIn("tag",userTags);

        // let adapter do the rest of the work
        EssayListAdapter adapter = new EssayListAdapter(this, tagQuery);
        mDatabaseEssays = adapter.getDatabaseEssays();
        adapter.setClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        // firebase stuff
        // set review = user
        String link = mDatabaseEssays.get(position).get("url");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // asynchronously retrieve multiple documents
        Query tagQuery = db
                .collection("ECG") // TODO: get the correct workspace
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
        confirmReviewDocRef.update("reviewer",mUser.getDisplayName());
    }
}
