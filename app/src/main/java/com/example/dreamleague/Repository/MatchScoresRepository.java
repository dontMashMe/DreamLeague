package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.MatchScoresDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.NumberOfGoalsTuple;

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
        matchScoresDao.insert(matchScores);
    }

    public List<MatchScores> getMatchScoresForGame(int gameId){
        List<MatchScores> returnList = new ArrayList<>();
        ExecutorService executor =  Executors.newSingleThreadExecutor();
        executor.execute(()->{
             returnList.addAll(matchScoresDao.matchScoresForGame(gameId));
        });
        executor.shutdown();

        return returnList;
    }

    public LiveData<Integer> countPlayerNumberOfGoals(int playerId){
        return matchScoresDao.countPlayerNumberOfGoals(playerId);
    }


    public int returnNumberOfGoals(int playerId, int gameId){
        return matchScoresDao.returnNumberOfGoals(playerId,  gameId);
    }

    public void deleteAllMatchScores(){
        matchScoresDao.deleteAllMatchScores();
    }
    public LiveData<List<MatchScores>> numberOfGoals(List<Integer> playerIds){
        return matchScoresDao.numberOfGoals(playerIds);
    }

   public LiveData<List<NumberOfGoalsTuple>> getNumberOfGoals(List<Integer> playerIds){
        return matchScoresDao.getNumberOfGoals(playerIds);
   }




}
