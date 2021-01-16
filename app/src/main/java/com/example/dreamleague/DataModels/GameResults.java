package com.example.dreamleague.DataModels;

import java.util.Map;

public class GameResults {
    private int gameId;
    private int homeTeamId;
    private int teamAwayId;

    private int homeTeamScore;
    private int awayTeamScore;
    private Map<Player, Integer> homeTeamScorers;
    private Map<Player, Integer> awayTeamScorers;

    public GameResults(int gameId, int homeTeamId, int teamAwayId, int homeTeamScore, int awayTeamScore, Map<Player, Integer> homeTeamScorers, Map<Player, Integer> awayTeamScorers) {
        this.gameId = gameId;
        this.homeTeamId = homeTeamId;
        this.teamAwayId = teamAwayId;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.homeTeamScorers = homeTeamScorers;
        this.awayTeamScorers = awayTeamScorers;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public int getTeamAwayId() {
        return teamAwayId;
    }
    public int getHomeTeamScore() {
        return homeTeamScore;
    }
    public int getAwayTeamScore() {
        return awayTeamScore;
    }


    public Map<Player, Integer> getHomeTeamScorers() {
        return homeTeamScorers;
    }
    public Map<Player, Integer>getAwayTeamScorers() {
        return awayTeamScorers;
    }


}
