package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToReviewListFragment extends Fragment implements EssayListAdapter.ItemClickListener {

    private static final String TAG = "ToReviewList";

    EssayListAdapter adapter;
    private FirebaseUser mUser;
    private Button otherEssaysButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "databaseEssays";

    // TODO: Rename and change types of parameters
    private ArrayList<HashMap<String, String>> mDatabaseEssays;

    public ToReviewListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param databaseEssays List of maps that represents essays fetched from the database.
     * @return A new instance of fragment FeedbackListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackListFragment newInstance(@NonNull ArrayList<HashMap<String, String>> databaseEssays) {
        FeedbackListFragment fragment = new FeedbackListFragment();
        Bundle args = new Bundle();

        ArrayList<ParcelableEssay> parcelableEssayList = new ArrayList<>();
        for (int i=0; i<databaseEssays.size(); i++) {
            parcelableEssayList.set(i, new ParcelableEssay(databaseEssays.get(i)));
        }
        args.putParcelableArrayList(ARG_PARAM1, parcelableEssayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArrayList<ParcelableEssay> parcelableEssayArrayList = getArguments().getParcelableArrayList(ARG_PARAM1);
            mDatabaseEssays = new ArrayList<>();
            for (ParcelableEssay essay : parcelableEssayArrayList) {
                mDatabaseEssays.add(essay.toHashMap());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_to_review_list, container, false);
        // Fake data
        // TODO: replace with real user stuff
        try {
            mDatabaseEssays = generateDatabaseEssayTitles();
            Log.i(TAG, "success");
        } catch (Exception e) {
            Log.e(TAG, "failure", e);
        } finally {
            Log.v(TAG, "attempted");
        }
        otherEssaysButton = v.findViewById(R.id.otherEssaysButton);
        Log.v(TAG,otherEssaysButton.getText().toString());
        otherEssaysButton.setOnClickListener(this::onClickOtherEssays);
        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EssayListAdapter(getContext(), mDatabaseEssays);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private ArrayList<HashMap<String, String>> generateDatabaseEssayTitles() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // asynchronously retrieve multiple documents
        Log.v(TAG, "user"+mUser.getDisplayName());
        Query tagQuery = db.collection("ECG")
                .whereEqualTo("tag", "eng")
                .whereEqualTo("reviewer",mUser.getDisplayName());
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
        // TODO: Make this actually do something
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        intent.putExtra("url",mDatabaseEssays.get(position).get("url"));
        startActivity(intent);
    }

    public void onClickOtherEssays(View view){
        Intent intent = new Intent(getActivity(), OtherEssaysActivity.class);
        startActivity(intent);
    }
}