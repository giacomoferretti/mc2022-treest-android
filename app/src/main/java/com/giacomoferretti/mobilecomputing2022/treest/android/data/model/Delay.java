package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import android.content.Context;
import android.content.res.Resources;

public enum Delay {
    ON_TIME,
    MINOR,
    MAJOR,
    CANCELLED;

    public static Delay fromInteger(int x) {
        switch(x) {
            case 0:
                return ON_TIME;
            case 1:
                return MINOR;
            case 2:
                return MAJOR;
            case 3:
                return CANCELLED;
        }
        return null;
    }

    public String getLabel(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier("enum_delay_" + this.name(), "string", context.getPackageName());
        if (0 != resId) {
            return (res.getString(resId));
        }
        return name();
    }
}
