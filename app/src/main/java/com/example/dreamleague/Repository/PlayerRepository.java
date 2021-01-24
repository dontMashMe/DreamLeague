package com.example.dreamleague.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DAOs.PlayerDao;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DAOs.TeamDao;

import java.util.List;
import java.util.concurrent.Executor;

public class PlayerRepository {
    private final PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;


    public PlayerRepository(Application application){
        Database database = Database.getInstance(application);
        playerDao = database.playerDao();
        allPlayers = playerDao.getAllPlayers();

    }

    public LiveData<List<Player>> getAllPlayersASC(){
        return playerDao.getAllPlayersPRasc();
    }

    public LiveData<List<Player>> getAllPlayersDESC(){
        return playerDao.getAllPlayersPRdesc();
    }


    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public LiveData<List<Player>> getPlayersSearch(String search){
        return playerDao.getSearchedPlayers(search);
    }

    public LiveData<List<Player>> getPlayersFromTeam(int teamId){
        return playerDao.getPlayersFromTeam(teamId);
    }

    public LiveData<List<Player>> getPlayersTransferQuery(String team_name, String position){
        return playerDao.getPlayersTransferQuery(team_name, position);
    }

    public LiveData<List<Player>> transferQueryOnlyPositionSet(String position){
        return playerDao.transferQueryOnlyPositionSet(position);
    }

    public LiveData<List<Player>> transferQueryOnlyNameSet(String teamName){
        return playerDao.transferQueryOnlyNameSet(teamName);
    }
    public LiveData<List<Player>> userPlayers(List<Integer> playerIds){
        return playerDao.userPlayers(playerIds);
    }


}
