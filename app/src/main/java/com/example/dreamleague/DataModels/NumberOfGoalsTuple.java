package com.example.dreamleague.DataModels;

import androidx.room.ColumnInfo;

public class NumberOfGoalsTuple {
    @ColumnInfo(name = "sum(numberOfGoals)")
    public int numberOfGoals;

    @ColumnInfo(name = "playerId")
    public int playerId;

    public int getNumberOfGoals() {
        return numberOfGoals;
    }

    public void setNumberOfGoals(int numberOfGoals) {
        this.numberOfGoals = numberOfGoals;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
