package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.PlayerPointsDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerPoints;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerPointsRepository {
    private PlayerPointsDao playerPointsDao;

    public PlayerPointsRepository(Application application){
        Database database = Database.getInstance(application);
        playerPointsDao = database.playerPointsDao();
    }

    //called when creating team and only if all players are selected
    public void insertOnCreation(List<Player> players){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> {
            for(Player a : players){
                playerPointsDao.insertPlayer(a.getPlayerId());
            }
        });
        executor.shutdown();
    }
    //called when advancing a week; update ALL user player points at once
    //players points should be pre-calculated before calling this method
    public void updatePlayerPoints(List<Player> players){
        for(Player a : players){
            playerPointsDao.updatePlayerPoints(a.getPointsAcquired(), a.getPlayerId());
        }
    }


    public void deletePlayer(int playerId){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            playerPointsDao.deletePlayer(playerId);
        });
        executor.shutdown();
    }

    public void insertPlayer(int playerId){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            playerPointsDao.insertPlayer(playerId);
        });
        executor.shutdown();
    }
    public LiveData<Integer> getPlayerPoints(int playerId){
        return playerPointsDao.getPlayerPoints(playerId);
    }

    public int getPlayerPointsInt(int playerId){
        return playerPointsDao.getPlayerPointsInt(playerId);
    }
    public LiveData<List<PlayerPoints>> getAllPlayerPoints(){
        return playerPointsDao.getAllPlayerPoints();
    }
    public int getAllPointsSum(){
        return playerPointsDao.getAllPointsSum();
    }

    public void deleteAllPlayerPoints(){
        playerPointsDao.deleteAllPlayerPoints();
    }



}
