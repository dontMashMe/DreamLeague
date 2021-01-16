package com.example.dreamleague.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Player")
public class Player {
    @PrimaryKey
    private int playerId;
    @NonNull
    private String name;
    @NonNull
    private String dateOfBirth;
    @NonNull
    private String position;
    @NonNull
    private String playerRating;
    private int playerValue;

    @Ignore
    private Team team;
    @Ignore
    private int realPosition;
    //realPosition je integer koji predstavlja "stvarnu" poziciju igraca na terenu, a mapirani su ispod
    //ovim redosljedom se daju konstruktoru DreamTeam klase, koja kao podatkovne clanove sadrzi id-eve igrača na njihovim pozicijima
    //1-goalie
    //2,3,4,5 - defenders (left, mid1, mid2, right)
    //6,7,8,9 - midfielders (left, mid1, mid2, right)
    //10, 11 - attackers (left, right)

    @Ignore
    private double probabilityWeight;

    public Player(int playerId, String name, String dateOfBirth, String position, String playerRating, int playerValue) {
        this.playerId = playerId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.position = position;
        this.playerRating = playerRating;
        this.playerValue = playerValue;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPosition() {
        return position;
    }

    public String getPlayerRating(){
        return playerRating;
    }

    public int getPlayerValue() {
        return playerValue;
    }


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


    public int getRealPosition() {
        return realPosition;
    }

    public void setRealPosition(int realPosition) {
        this.realPosition = realPosition;
    }

    public double getProbabilityWeight() {
        return probabilityWeight;
    }

    public void setProbabilityWeight(double probabilityWeight) {
        this.probabilityWeight = probabilityWeight;
    }
}
