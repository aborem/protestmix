package com.aborem.protestmixv1.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.aborem.protestmixv1.dao.ForwardInfoDao;
import com.aborem.protestmixv1.database.AppDatabase;
import com.aborem.protestmixv1.models.ForwardInfo;

import java.util.List;

public class ForwardInfoRepository {
    private ForwardInfoDao forwardInfoDao;
    private LiveData<List<ForwardInfo>> allForwardContacts;

    public ForwardInfoRepository(Application application) {
        AppDatabase appDb = AppDatabase.getInstance(application);
        this.forwardInfoDao = appDb.forwardInfoDao();
        this.allForwardContacts = forwardInfoDao.getAll();
    }

    public LiveData<List<ForwardInfo>> getAllForwardContacts() {
        return allForwardContacts;
    }

    public void insert(ForwardInfo forwardInfo) {
        AppDatabase.databaseWriteExecutor.execute(() -> forwardInfoDao.insert(forwardInfo));
    }

    public void delete(ForwardInfo forwardInfo) {
        AppDatabase.databaseWriteExecutor.execute(() -> forwardInfoDao.delete(forwardInfo));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> forwardInfoDao.deleteAll());
    }
}
