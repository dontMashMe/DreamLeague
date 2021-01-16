package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.MatchScoresDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchScoresRepository {
    private MatchScoresDao matchScoresDao;

    public MatchScoresRepository(Application application){
        Database database = Database.getInstance(application);
        matchScoresDao = database.matchScoresDao();
    }

    public void InsertMatchScores(MatchScores matchScores){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            matchScoresDao.insert(matchScores);
        });
        executor.shutdown();
    }

    public List<MatchScores> getMatchScoresForGame(int gameId){
        List<MatchScores> returnList = new ArrayList<>();
        ExecutorService executor =  Executors.newSingleThreadExecutor();
        executor.execute(()->{
             returnList.addAll(matchScoresDao.matchScoresForGame(gameId));
        });
        return returnList;
    }
}
