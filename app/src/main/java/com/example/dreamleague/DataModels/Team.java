package com.example.dreamleague.DataModels;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Team")
public class Team {

    @PrimaryKey
    private int team_id;
    private String name;
    private String crestUrl;
    private String venue;
    private int points;

    @Ignore
    private Integer teamKit;

    @Ignore
    private Integer teamLogo;

    //za leaderboard, opet ne zelim slat cijeli viewmodel recycler viewu
    @Ignore
    private String last5games;

    public String getLast5games() {
        return last5games;
    }

    public void setLast5games(String last5games) {
        this.last5games = last5games;
    }

    public int getPoints() {
        return points;
    }


    @Ignore
    private List<Player> playerList = new ArrayList<>();

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Integer getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(Integer teamLogo) {
        this.teamLogo = teamLogo;
    }

    public Integer getTeamKit() {
        return teamKit;
    }

    public void setTeamKit(Integer teamKit) {
        this.teamKit = teamKit;
    }

    public Team(int team_id, String name, String crestUrl, String venue, int points) {
        this.team_id = team_id;
        this.name = name;
        this.crestUrl = crestUrl;
        this.venue = venue;
        this.points = points;
    }

    public int getTeam_id() {
        return team_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public String getVenue() {
        return venue;
    }


}
