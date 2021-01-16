package com.example.dreamleague.DataModels;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Matches")
public class Match {
    @PrimaryKey(autoGenerate = true)
    private int gameId;
    private int week;
    private int teamHome;
    private int teamAway;
    private int teamHomeScore;
    private int teamAwayScore;

    //ova dva podatkovna člana postoje isključivo iz razloga kako bih MatchesRecViewAdapteru mogao poslati samo listu Matches
    //bez da prosljeđujem viewmodel, ili imam nekakvu logiku dobivanja Team objekta iz ID-a
    @Ignore
    private Team objTeamHome;
    @Ignore
    private Team objTeamAway;

    @Ignore
    private List<MatchScores> matchScores = new ArrayList<>();

    public List<MatchScores> getMatchScores() {
        return matchScores;
    }

    public void setMatchScores(List<MatchScores> matchScores) {
        this.matchScores = matchScores;
    }

    @Ignore
    private boolean expanded;

    public Match(int gameId, int week, int teamHome, int teamAway, int teamHomeScore, int teamAwayScore) {
        this.gameId = gameId;
        this.week = week;
        this.teamHome = teamHome;
        this.teamAway = teamAway;
        this.teamHomeScore = teamHomeScore;
        this.teamAwayScore = teamAwayScore;
    }

    public int getGameId() {
        return gameId;
    }
    public int getWeek() {
        return week;
    }
    public int getTeamHome() {
        return teamHome;
    }
    public int getTeamAway() {
        return teamAway;
    }
    public int getTeamHomeScore() {
        return teamHomeScore;
    }
    public int getTeamAwayScore() {
        return teamAwayScore;
    }

    public Team getObjTeamHome() {
        return objTeamHome;
    }

    public void setObjTeamHome(Team objTeamHome) {
        this.objTeamHome = objTeamHome;
    }

    public Team getObjTeamAway() {
        return objTeamAway;
    }

    public void setObjTeamAway(Team objTeamAway) {
        this.objTeamAway = objTeamAway;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

}
