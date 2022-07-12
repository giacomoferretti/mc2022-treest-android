package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Terminus implements Parcelable {
    @SerializedName("sname")
    private String stationName;

    @SerializedName("did")
    private int directionId;

    public Terminus(String stationName, int directionId) {
        this.stationName = stationName;
        this.directionId = directionId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stationName);
        dest.writeInt(this.directionId);
    }

    protected Terminus(Parcel in) {
        stationName = in.readString();
        directionId = in.readInt();
    }

    public static final Creator<Terminus> CREATOR = new Creator<Terminus>() {
        @Override
        public Terminus createFromParcel(Parcel in) {
            return new Terminus(in);
        }

        @Override
        public Terminus[] newArray(int size) {
            return new Terminus[size];
        }
    };
}
