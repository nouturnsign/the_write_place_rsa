package com.example.readysetappv1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class ParcelableEssay implements Parcelable {

    private String username;
    private String date;
    private String essayTitle;
    private String url;

    // Constructor

    public ParcelableEssay(HashMap<String, String> essay){
        this.username = essay.get("username");
        this.date = essay.get("date");
        this.essayTitle = essay.get("essayTitle");
        this.url = essay.get("url");
    }

    // Parcelling part
    public ParcelableEssay(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.username = data[0];
        this.date = data[1];
        this.essayTitle = data[2];
        this.url = data[3];
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> essays = new HashMap<>();
        essays.put("username", this.username);
        essays.put("date", this.date);
        essays.put("essayTitle", this.essayTitle);
        essays.put("url", this.url);
        return essays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.username,
                this.date,
                this.essayTitle,
                this.url
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelableEssay createFromParcel(Parcel in) {
            return new ParcelableEssay(in);
        }

        public ParcelableEssay[] newArray(int size) {
            return new ParcelableEssay[size];
        }
    };

}
