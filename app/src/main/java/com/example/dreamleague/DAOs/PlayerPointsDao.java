package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerPoints;

import java.util.List;

@Dao
public interface PlayerPointsDao {

    @Query("UPDATE PlayerPoints SET points = points + :points WHERE playerId = :playerId")
    void updatePlayerPoints(int points, int playerId);

    @Query("DELETE FROM PlayerPoints WHERE playerId = :playerId")
    void deletePlayer(int playerId);

    @Query("INSERT INTO PlayerPoints (playerId, points) VALUES (:playerId, 0)")
    void insertPlayer(int playerId);

    @Query("SELECT points FROM PlayerPoints WHERE playerId = :playerId")
    LiveData<Integer> getPlayerPoints(int playerId);

    @Query("SELECT points FROM PlayerPoints WHERE playerId = :playerId")
    int getPlayerPointsInt(int playerId);

    @Query("SELECT * FROM PlayerPoints")
    LiveData<List<PlayerPoints>> getAllPlayerPoints();

    @Query("SELECT sum(points) FROM playerpoints")
    int getAllPointsSum();

    @Query("DELETE FROM PlayerPoints")
    void deleteAllPlayerPoints();

}
