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

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EssayListAdapter extends RecyclerView.Adapter<EssayListAdapter.ViewHolder> {

    private static final String TAG = "EssayListAdapter";

    private final List<HashMap<String, String>> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    EssayListAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> review = mData.get(position);
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
            return mData.size();
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
    HashMap<String, String> getItem(int id) {
        return mData.get(id);
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