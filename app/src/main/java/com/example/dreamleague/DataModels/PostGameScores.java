package com.example.dreamleague.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostGameScores")
public class PostGameScores {
    @PrimaryKey(autoGenerate = true)
    private int recordId;
    @NonNull
    private String teamName;
    @NonNull
    private int totalScore;

    public PostGameScores(int recordId, @NonNull String teamName, int totalScore) {
        this.recordId = recordId;
        this.teamName = teamName;
        this.totalScore = totalScore;
    }

    public int getRecordId() {
        return recordId;
    }

    @NonNull
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(@NonNull String teamName) {
        this.teamName = teamName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
