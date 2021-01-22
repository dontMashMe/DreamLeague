package com.example.dreamleague.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlayerPoints")
public class PlayerPoints {
    @NonNull
    @PrimaryKey
    private int playerId;

    @NonNull
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayerId() {
        return playerId;
    }

    public PlayerPoints(int playerId, int points) {
        this.playerId = playerId;
        this.points = points;
    }
}
