package com.example.dreamleague.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MatchScores")
public class MatchScores {
    @PrimaryKey(autoGenerate = true)
    private int recordId;
    @NonNull
    private int gameId;
    @NonNull
    private int playerId;
    @NonNull
    private int numberOfGoals;

    @NonNull
    private int team;

    public MatchScores(int recordId, int gameId, int playerId, int numberOfGoals, int team) {
        this.recordId = recordId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.numberOfGoals = numberOfGoals;
        this.team = team;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getNumberOfGoals() {
        return numberOfGoals;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
