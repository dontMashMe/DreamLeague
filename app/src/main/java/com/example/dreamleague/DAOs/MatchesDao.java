package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dreamleague.DataModels.Match;

import java.util.List;

@Dao
public interface MatchesDao {
    @Insert
    void insert(Match match);

    @Update
    void update(Match match);

    @Query("SELECT * FROM Matches")
    LiveData<List<Match>> getAllMatches();

    @Query("SELECT * FROM Matches WHERE week = :week")
    LiveData<List<Match>> getMatchesFromWeek(int week);

    @Query("UPDATE Matches SET teamAwayScore = :awayScore, teamHomeScore = :homeScore WHERE gameId = :gameId")
    void updateMatchWhere(int awayScore, int homeScore, int gameId);

    @Query("SELECT * FROM Matches WHERE week < :currentWeek AND teamAway = :teamId OR week < :currentWeek AND teamHome = :teamId order by week desc limit 5")
    LiveData<List<Match>> last5Matches(int currentWeek, int teamId);

    @Query("SELECT * FROM Matches WHERE teamAway = :teamId AND week < :currentWeek OR teamHome =:teamId AND week < :currentWeek")
    LiveData<List<Match>> allMatchesFromPlayersTeam (int teamId, int currentWeek);

    @Query("DELETE FROM Matches")
    void deleteAllMatches();

    @Query("SELECT * FROM Matches WHERE teamAway = :teamId OR teamHome =:teamId ORDER BY week ASC")
    LiveData<List<Match>> getAllMatchesFromTeam(int teamId);
}
