package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;

public interface DatabaseDataSource {
    void getUserPictureByUserIdAndVersion(String userId, String version, TaskCallback<UserPicture> callback);
    void savePicture(UserPicture data);
}
