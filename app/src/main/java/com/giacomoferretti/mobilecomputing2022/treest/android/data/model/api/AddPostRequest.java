package com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;

public class AddPostRequest extends SidDidRequest {
    private final String comment;
    private final Integer delay;
    private final Integer status;

    public AddPostRequest(String sid, String did, String comment, Delay delay, Status status) {
        super(sid, did);
        this.comment = comment;

        if (delay == null) {
            this.delay = null;
        } else {
            this.delay = delay.ordinal();
        }

        if (status == null) {
            this.status = null;
        } else {
            this.status = status.ordinal();
        }
    }
}
