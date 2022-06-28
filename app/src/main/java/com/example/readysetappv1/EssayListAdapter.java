package com.example.readysetappv1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EssayListAdapter extends RecyclerView.Adapter<EssayListAdapter.ViewHolder> {

    private static final String TAG = "EssayListAdapter";

    private final List<String> ATTRIBUTES = Arrays.asList("submitter", "reviewer", "date", "url", "tag"); // essayTitle at .getId()
    private final List<Map<String, String>> mDatabaseEssays;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // List<Map<String, String>> is passed into the constructor
    public EssayListAdapter(Context context, List<Map<String, String>> databaseEssays) {
        this.mInflater = LayoutInflater.from(context);
        this.mDatabaseEssays = databaseEssays;
    }

    // Query is passed into the constructor
    public EssayListAdapter(Context context, Query tagQuery) {
        this.mInflater = LayoutInflater.from(context);
        this.mDatabaseEssays = getDatabaseEssays(tagQuery);
    }

    public List<Map<String, String>> getDatabaseEssays() {
        return mDatabaseEssays;
    }

    private List<Map<String, String>> getDatabaseEssays(Query tagQuery) {
        Task<QuerySnapshot> tagQueryTask = tagQuery.get();

        tagQueryTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "Successfully retrieved essays from database");
            } else {
                Log.i(TAG, "Failed to retrieve essays from database");
            }
        });

        List<Map<String, String>> databaseEssays = new ArrayList<>();
        RunnableTask runnable = new RunnableTask(tagQueryTask);
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QuerySnapshot tagQuerySnapshot = runnable.getValue();

        for (DocumentSnapshot document : tagQuerySnapshot.getDocuments()) {
            Map<String, String> documentAttributes = new HashMap<>();
            documentAttributes.put("essayTitle", document.getId());
            for (String attribute : ATTRIBUTES) {
                documentAttributes.put(attribute, (String) document.get(attribute));
            }
            databaseEssays.add(documentAttributes);
        }
        //todo: tags and profile pictures

        return databaseEssays;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> review = mDatabaseEssays.get(position);
        /*
            submitterUsername.add(document.get("username").toString());
            date.add(document.get("date").toString());
            essayTitle.add(document.getId());
            url.add(document.get("url").toString());
            tag.add(document.get("tag").toString());
        */
        String username;
        if (review.containsKey("submitter")) {
            username = review.get("submitter");
        } else {
            username = review.get("reviewer");
        }
        String date = review.get("date");
        String essayTitle = review.get("essayTitle");
        // Log.v(TAG, username + " " + date + " " + essayTitle);
        // String profilePicture = review.get("profilePicture");
        // String tagPicture = review.get("tagPicture");
        holder.usernameView.setText(username);
        holder.dateView.setText(date);
        holder.essayTitleView.setText(essayTitle);
        // kind of redundant to cast to String and back
//        if (profilePicture == null) profilePicture = String.valueOf(R.drawable.generic_profile_picture);
//        holder.profilePictureView.setImageResource(Integer.parseInt(profilePicture));
//        if (tagPicture == null) tagPicture = String.valueOf(R.drawable.engtag);
//        holder.tagPictureView.setImageResource(Integer.parseInt(tagPicture));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        try {
            Log.v(TAG, "Successfully invoked getItemCount.");
            return mDatabaseEssays.size();
        } catch (NullPointerException e) {
            Log.w(TAG, "Failed to invoke getItemCount.");
            return 0;
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView usernameView;
        TextView dateView;
        TextView essayTitleView;
        ShapeableImageView profilePictureView;
        ImageView tagPictureView;

        ViewHolder(View itemView) {
            super(itemView);
            usernameView = itemView.findViewById(R.id.profileUsername);
            dateView = itemView.findViewById(R.id.profileDate);
            essayTitleView = itemView.findViewById(R.id.essayTitle);
            profilePictureView = itemView.findViewById(R.id.profilePicture);
            tagPictureView = itemView.findViewById(R.id.tagPicture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Map<String, String> getItem(int id) {
        return mDatabaseEssays.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
