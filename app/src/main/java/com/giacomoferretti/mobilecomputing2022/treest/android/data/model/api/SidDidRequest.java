package com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api;

public class SidDidRequest extends SidRequest {
    private final String did;

    public SidDidRequest(String sid, String did) {
        super(sid);
        this.did = did;
    }

    public String getDid() {
        return did;
    }
}
