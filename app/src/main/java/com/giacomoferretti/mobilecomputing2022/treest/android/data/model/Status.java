package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import android.content.Context;
import android.content.res.Resources;

public enum Status {
    IDEAL,
    ACCEPTABLE,
    HAS_PROBLEMS;

    public static Status fromInteger(int x) {
        switch(x) {
            case 0:
                return IDEAL;
            case 1:
                return ACCEPTABLE;
            case 2:
                return HAS_PROBLEMS;
        }
        return null;
    }

    public String getLabel(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier("enum_status_" + this.name(), "string", context.getPackageName());
        if (0 != resId) {
            return (res.getString(resId));
        }
        return (name());
    }
}
