package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;

@Dao
public interface UserPictureDao {
    @Query("SELECT * FROM user_picture WHERE uid LIKE :uid AND pversion LIKE :version LIMIT 1")
    UserPicture findByUidAndVersion(String uid, String version);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPicture userPicture);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserPicture... userPictures);
}