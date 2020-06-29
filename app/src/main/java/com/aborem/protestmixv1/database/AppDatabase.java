package com.aborem.protestmixv1.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.aborem.protestmixv1.Constants;
import com.aborem.protestmixv1.dao.ContactDao;
import com.aborem.protestmixv1.models.ContactModel;
import com.aborem.protestmixv1.models.ForwardInfo;
import com.aborem.protestmixv1.dao.ForwardInfoDao;
import com.aborem.protestmixv1.dao.MessageDao;
import com.aborem.protestmixv1.models.MessageModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MessageModel.class, ForwardInfo.class, ContactModel.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, Constants.DB_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract MessageDao messageDao();
    public abstract ForwardInfoDao forwardInfoDao();
    public abstract ContactDao contactDao();
}
