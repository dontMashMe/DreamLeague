package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dreamleague.DataModels.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insert(Player player);

    @Update
    void update(Player player);

    @Query("SELECT * FROM player ORDER BY playerId")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM Player ORDER BY playerRating ASC")
    LiveData<List<Player>> getAllPlayersPRasc();

    @Query("SELECT * FROM Player ORDER BY playerRating DESC")
    LiveData<List<Player>> getAllPlayersPRdesc();

    @Query("SELECT * FROM Player WHERE name LIKE :search")
    LiveData<List<Player>> getSearchedPlayers(String search);

    @Query("SELECT player.playerId, player.name, dateOfBirth, position, playerRating, playerValue from player\n" +
            "inner join squads on player.playerId = squads.playerId\n" +
            "inner join team on team.team_id = squads.teamId\n" +
            "where team_id = :team_id\n" +
            "order by player.playerValue ASC")
    LiveData<List<Player>> getPlayersFromTeam(int team_id);


    @Query("SELECT player.playerId, player.name, dateOfBirth, position, playerRating, playerValue from player\n" +
            "inner join squads on player.playerId = squads.playerId\n" +
            "inner join team on team.team_id = squads.teamId\n" +
            "where team.name = :team_name AND player.position = :position\n" +
            "order by player.playerRating DESC")
    LiveData<List<Player>> getPlayersTransferQuery(String team_name, String position);


    @Query("SELECT player.playerId, player.name, dateOfBirth, position, playerRating, playerValue from player\n" +
            "inner join squads on player.playerId = squads.playerId\n" +
            "inner join team on team.team_id = squads.teamId\n" +
            "where team.name IN (SELECT team.name from team) AND player.position = :position\n" +
            "order by player.playerRating DESC")
    LiveData<List<Player>> transferQueryOnlyPositionSet(String position);

    @Query("SELECT player.playerId, player.name, dateOfBirth, position, playerRating, playerValue from player\n" +
            "inner join squads on player.playerId = squads.playerId\n" +
            "inner join team on team.team_id = squads.teamId\n" +
            "where team.name = :team_name AND player.position IN (SELECT player.position from player)" +
            "order by player.playerRating DESC")
    LiveData<List<Player>> transferQueryOnlyNameSet(String team_name);

    @Query("SELECT * FROM Player WHERE playerId IN (:playerIds)")
    LiveData<List<Player>> userPlayers(List<Integer> playerIds);

    @Query("SELECT player.playerId, player.name, dateOfBirth, position, playerRating, playerValue from player\n" +
            "            inner join squads on player.playerId = squads.playerId\n" +
            "            inner join team on team.team_id = squads.teamId\n" +
            "            where team.team_id = :team_id AND player.position = \"\"\n" +
            "            order by player.playerRating DESC")
    Player getManager(int team_id);

}