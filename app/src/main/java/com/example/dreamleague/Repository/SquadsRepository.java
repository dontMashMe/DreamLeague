package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.SquadsDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Squads;

import java.util.List;

public class SquadsRepository {
    private SquadsDao squadsDao;
    private LiveData<List<Squads>> allSquads;

    public SquadsRepository(Application application){
        Database database = Database.getInstance(application);
        squadsDao = database.squadsDao();
        allSquads = squadsDao.getAllSquads();
    }

    public LiveData<List<Squads>> getAllSquads() {
        return allSquads;
    }
}
