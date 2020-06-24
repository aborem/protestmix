package com.aborem.protestmixv1.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.aborem.protestmixv1.models.ForwardInfo;

import java.util.List;

@Dao
public interface ForwardInfoDao {
    @Query("SELECT * FROM forward_info ORDER BY expiration_time_ms DESC")
    List<ForwardInfo> getAll();
}
