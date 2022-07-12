package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Line implements Parcelable {
    private Terminus terminus1;
    private Terminus terminus2;

    public Line(Terminus terminus1, Terminus terminus2) {
        this.terminus1 = terminus1;
        this.terminus2 = terminus2;
    }

    public Terminus getTerminus1() {
        return terminus1;
    }

    public void setTerminus1(Terminus terminus1) {
        this.terminus1 = terminus1;
    }

    public Terminus getTerminus2() {
        return terminus2;
    }

    public void setTerminus2(Terminus terminus2) {
        this.terminus2 = terminus2;
    }

    public String getLineName() {
        return terminus2.getStationName() + " - " + terminus1.getStationName();
    }

    public String getInversedLineName() {
        return terminus1.getStationName() + " - " + terminus2.getStationName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.terminus1, flags);
        dest.writeParcelable(this.terminus2, flags);
    }

    protected Line(Parcel in) {
        this.terminus1 = in.readParcelable(Terminus.class.getClassLoader());
        this.terminus2 = in.readParcelable(Terminus.class.getClassLoader());
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };
}
