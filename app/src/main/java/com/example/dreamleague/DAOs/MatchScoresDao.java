package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.NumberOfGoalsTuple;

import java.util.List;

@Dao
public interface MatchScoresDao {

    @Insert
    void insert(MatchScores matchScores);

    @Query("SELECT * FROM MatchScores where gameId = :gameId")
    List<MatchScores> matchScoresForGame(int gameId);

    @Query("SELECT sum(numberOfGoals) from MatchScores where playerId = :playerId")
    LiveData<Integer> countPlayerNumberOfGoals(int playerId);

    @Query("SELECT sum(numberOfGoals) from MatchScores where playerId = :playerId and gameId = :gameId")
    int returnNumberOfGoals(int playerId, int gameId);

    @Query("DELETE FROM MatchScores")
    void deleteAllMatchScores();

    @Query("SELECT * FROM matchscores WHERE playerId IN (:playerIds)")
    LiveData<List<MatchScores>> numberOfGoals(List<Integer> playerIds);

    @Query("SELECT sum(numberOfGoals), playerId from MatchScores WHERE playerId IN (:playerIds) group by playerId")
    LiveData<List<NumberOfGoalsTuple>> getNumberOfGoals(List<Integer> playerIds);
}
