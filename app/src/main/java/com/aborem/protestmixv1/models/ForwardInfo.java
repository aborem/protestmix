package com.aborem.protestmixv1.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "forward_info")
public class ForwardInfo {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "row_id")
    private String rowId;

    @ColumnInfo(name = "forward_to_phone_number")
    private String forwardToPhoneNumber;

    @ColumnInfo(name = "expiration_time_ms")
    private long expirationTimeMs;

    public ForwardInfo(String forwardToPhoneNumber, long expirationTimeMs) {
        this.forwardToPhoneNumber = forwardToPhoneNumber;
        this.expirationTimeMs = expirationTimeMs;
        this.rowId = UUID.randomUUID().toString();
    }

    public String getForwardToPhoneNumber() {
        return forwardToPhoneNumber;
    }

    public long getExpirationTimeMs() {
        return expirationTimeMs;
    }

    @NonNull
    public String getRowId() {
        return rowId;
    }

    public void setRowId(@NonNull String rowId) {
        this.rowId = rowId;
    }
}
