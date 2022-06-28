package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToReviewListFragment extends Fragment implements EssayListAdapter.ItemClickListener {

    private static final String TAG = "ToReviewList";

    private Button otherEssaysButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "databaseEssays";

    // TODO: Rename and change types of parameters
    private List<Map<String, String>> mDatabaseEssays;

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

        // set up OtherEssays
        otherEssaysButton = v.findViewById(R.id.button_other_essays);
        otherEssaysButton.setOnClickListener(this::onClickOtherEssays);

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // define query
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query tagQuery =
                db
                .collection("ECG") // TODO: change this by workspace and tag
                .whereEqualTo("reviewer", mUser.getDisplayName());

        // let adapter do the rest of the work
        EssayListAdapter adapter = new EssayListAdapter(getContext(), tagQuery);
        mDatabaseEssays = adapter.getDatabaseEssays();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        intent.putExtra("url",mDatabaseEssays.get(position).get("url"));
        startActivity(intent);
    }

    public void onClickOtherEssays(View view){
        Intent intent = new Intent(getActivity(), OtherEssaysActivity.class);
        startActivity(intent);
    }
}