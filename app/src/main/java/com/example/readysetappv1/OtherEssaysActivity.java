package com.example.readysetappv1;

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
import java.util.HashMap;
import java.util.List;

public class OtherEssaysActivity extends AppCompatActivity implements EssayListAdapter.ItemClickListener {

    private static final String TAG = "OtherEssaysActivity";

    private FirebaseUser mUser;
    private ArrayList<HashMap<String, String>> mDatabaseEssays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_essays);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            mDatabaseEssays = generateDatabaseEssayTitles();
            Log.i(TAG, "success");
        } catch (Exception e) {
            Log.e(TAG, "failure", e);
        } finally {
            Log.v(TAG, "attempted");
        }

        EssayListAdapter adapter = new EssayListAdapter(this, mDatabaseEssays);
        adapter.setClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<HashMap<String, String>> generateDatabaseEssayTitles() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // asynchronously retrieve multiple documents
        Log.v(TAG, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Query tagQuery = db
                .collection("ECG")
                .whereEqualTo("reviewer", null)
                .whereNotEqualTo("submitter", "user")
                ;
        Task<QuerySnapshot> tagQueryTask = tagQuery.get();

        tagQueryTask.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                }
            }
        });

        ArrayList<HashMap<String, String>> databaseEssays = new ArrayList<>();
        RunnableTask runnable = new RunnableTask(tagQueryTask);
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QuerySnapshot tagQuerySnapshot = runnable.getValue();

        List<String> submitterUsername = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> essayTitle = new ArrayList<>();
        List<String> url = new ArrayList<>();
        List<String> tag = new ArrayList<>();
        int numQueries = 0;
        for (DocumentSnapshot document : tagQuerySnapshot.getDocuments()) {
            submitterUsername.add(document.get("submitter").toString());
            date.add(document.get("date").toString());
            essayTitle.add(document.getId());
            url.add(document.get("url").toString());
            tag.add(document.get("tag").toString());
            numQueries++;
        }
        //todo: tags and profile pictures
        //int[] profilePicture = new int[] {R.drawable.jason_square, R.drawable.eric_square, R.drawable.roshan_square, R.drawable.anika_square, R.drawable.michael_square, R.drawable.giggy_square, R.drawable.generic_profile_picture};
        //int[] tagPicture = new int[] {R.drawable.engtag, R.drawable.mathtag, R.drawable.histtag, R.drawable.frenchtag, R.drawable.phystag, R.drawable.sciencetag, R.drawable.chemtag};

        for (int i=0; i<numQueries; i++) {
            HashMap<String, String> review = new HashMap<>();
            review.put("submitter", submitterUsername.get(i));//todo: possible confuzzlement
            review.put("date",date.get(i));
            review.put("essayTitle", essayTitle.get(i));
            review.put("url",url.get(i));
            //review.put("profilePicture", String.valueOf(profilePicture[i]));
            //review.put("tagPicture", String.valueOf(tagPicture[i]));
            databaseEssays.add(review);
        }

        return databaseEssays;
    }

    @Override
    public void onItemClick(View view, int position) {
        // firebase stuff
        // set review = user
        String link = mDatabaseEssays.get(position).get("url");
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
        confirmReviewDocRef.update("reviewer",mUser.getDisplayName());
    }
}
