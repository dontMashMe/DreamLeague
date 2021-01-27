package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.MatchesDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Match;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchesRepository {

    private MatchesDao matchesDao;
    private LiveData<List<Match>> allMatches;

    public MatchesRepository(Application application){
        Database database = Database.getInstance(application);
        matchesDao = database.matchesDao();
        allMatches = matchesDao.getAllMatches();
    }

    public void insertMatch(Match match){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            matchesDao.insert(match);
        });
        executor.shutdown();
    }

    public LiveData<List<Match>> getAllMatches(){
        return this.allMatches;
    }
    public LiveData<List<Match>> getMatchesFromWeek(int week){
        return matchesDao.getMatchesFromWeek(week);
    }

    public LiveData<List<Match>> last5Matches(int currentWeek, int teamId){
        return matchesDao.last5Matches(currentWeek, teamId);
    }

    public void updateMatchWhere(int gameId, int homeScore, int awayScore){
        matchesDao.updateMatchWhere(awayScore, homeScore, gameId);
    }


    public LiveData<List<Match>> allMatchesFromPlayersTeam (int teamId, int currentWeek){
        return matchesDao.allMatchesFromPlayersTeam(teamId, currentWeek);
    }

    public void deleteAllMatches(){
        matchesDao.deleteAllMatches();
    }
}

