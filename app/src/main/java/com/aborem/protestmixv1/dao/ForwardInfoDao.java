package com.aborem.protestmixv1.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aborem.protestmixv1.models.ForwardInfo;

import java.util.List;

@Dao
public interface ForwardInfoDao {
    @Query("SELECT * FROM forward_info ORDER BY expiration_time_ms DESC")
    LiveData<List<ForwardInfo>> getAll();

    @Insert
    void insert(ForwardInfo forwardInfo);

    @Delete
    void delete(ForwardInfo forwardInfo);

    @Query("DELETE FROM forward_info")
    void deleteAll();
}
