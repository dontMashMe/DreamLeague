package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.TeamDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Team;

import java.util.List;

public class TeamRepository {
    private TeamDao teamDao;
    private LiveData<List<Team>> allTeams;

    public TeamRepository(Application application){
        Database database = Database.getInstance(application);
        teamDao = database.teamDao();
        allTeams = teamDao.getAllTeams();
    }

    public LiveData<List<Team>> getAllTeams() {
        return allTeams;
    }
    public void updateWinner(int team_id){
        teamDao.updateWinnerPoints(team_id);
    }
    public void updateDraw(int team_id){
        teamDao.updateDrawPoints(team_id);
    }
    public void resetAllTeamPoints(){
        teamDao.resetAllTeamPoints();
    }
}
