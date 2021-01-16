package com.example.dreamleague.DataModels;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Squads", primaryKeys = {"teamId", "playerId"}, foreignKeys = {
        @ForeignKey(
                entity = Team.class,
                parentColumns =  "team_id",
                childColumns = "teamId"
        )
})
public class Squads {

    private final int playerId;
    private final int teamId;

    public Squads(int playerId, int teamId) {
        this.playerId = playerId;
        this.teamId = teamId;
    }

    public int getPlayerId() {
        return playerId;
    }


    public int getTeamId() {
        return teamId;
    }

}
