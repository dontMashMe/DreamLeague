package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

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


}
