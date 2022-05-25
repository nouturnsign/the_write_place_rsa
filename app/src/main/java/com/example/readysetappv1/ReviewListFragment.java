package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewListFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "workspace_index";

    // TODO: Rename and change types of parameters
    private int mWorkspaceIndex;

    public ReviewListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param workspace_index Index of workspace (0, 1).
     * @return A new instance of fragment FeedbackListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackListFragment newInstance(int workspace_index) {
        FeedbackListFragment fragment = new FeedbackListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, workspace_index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWorkspaceIndex = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedback_list, container, false);

        // Fake data
        // TODO: replace with real user stuff
        ArrayList<HashMap<String, String>> essayTitles = generateFakeEssayTitles();

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), essayTitles);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private ArrayList<HashMap<String, String>> generateFakeEssayTitles() {

        // data to populate the RecyclerView with
        ArrayList<HashMap<String, String>> essayTitles = new ArrayList<>();
        // ideally something like ArrayList<User>

        String[] submitterUsername = new String[] {"Jason Yin", "Eric Wang", "Roshan Sundaram", "Anika Suman", "Michael Scutari", "Giggy", "John Doe"};
        int[] day = new int[] {28, 23, 22, 19, 17, 15, 12};
        String[] essayTitle = new String[] {"The Monumental Importance of Monuments", "On Lagrange Multipliers", "Industrial Revolution DBQ", "Rédaction 1", "PHYS 122 Lab #8", "The Great Pacific Garbage Patch", "Titration of Acetic Acid with Sodium Hydroxide"};
        int[] profilePicture = new int[] {R.drawable.jason_square, R.drawable.eric_square, R.drawable.roshan_square, R.drawable.anika_square, R.drawable.michael_square, R.drawable.giggy_square, R.drawable.generic_profile_picture};
        int[] tagPicture = new int[] {R.drawable.engtag, R.drawable.mathtag, R.drawable.histtag, R.drawable.frenchtag, R.drawable.phystag, R.drawable.sciencetag, R.drawable.chemtag};

        for (int i=0; i<7; i++) {
            HashMap<String, String> review = new HashMap<>();
            review.put("reviewerUsername", submitterUsername[i]);
            review.put("date", "02/" + day[i] + "/2022");
            review.put("essayTitle", essayTitle[i]);
            review.put("profilePicture", String.valueOf(profilePicture[i]));
            review.put("tagPicture", String.valueOf(tagPicture[i]));
            essayTitles.add(review);
        }

        return essayTitles;
    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO: Make this actually do something
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        startActivity(intent);
    }
}