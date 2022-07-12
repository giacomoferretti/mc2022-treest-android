package com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api;

public class SidUidRequest extends SidRequest {
    private final String uid;

    public SidUidRequest(String sid, String uid) {
        super(sid);
        this.uid = uid;
    }

    public String getDid() {
        return uid;
    }
}
