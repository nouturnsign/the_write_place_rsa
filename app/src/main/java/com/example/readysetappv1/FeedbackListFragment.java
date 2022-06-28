package com.example.readysetappv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Use the {@link FeedbackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackListFragment extends Fragment implements EssayListAdapter.ItemClickListener {

    private static final String TAG = "FeedbackList";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "workspace_index";

    // TODO: Rename and change types of parameters
    private List<Map<String, String>> mDatabaseEssays;
    private int mWorkspaceIndex;

    public FeedbackListFragment() {
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

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // define query
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query tagQuery =
                db
                .collection("ECG") // TODO: change this by workspace and tag
                .whereEqualTo("submitter", mUser.getDisplayName());

        // let adapter do the rest of the work
        EssayListAdapter adapter = new EssayListAdapter(getContext(), tagQuery);
        mDatabaseEssays = adapter.getDatabaseEssays();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onItemClick(View view, int position) {
        // TODO: Make this actually do something
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        intent.putExtra("url",mDatabaseEssays.get(position).get("url"));
        startActivity(intent);
    }
}