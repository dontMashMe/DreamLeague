package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dreamleague.DataModels.Team;

import java.util.List;

@Dao
public interface TeamDao {
    @Insert
    void insert(Team team);

    @Update
    void update(Team team);

    @Delete
    void delete(Team team);

    @Query("SELECT * FROM team ORDER BY points DESC")
    LiveData<List<Team>> getAllTeams();

    @Query("UPDATE TEAM SET points = points + 3 WHERE team_id = :team_id")
    void updateWinnerPoints(int team_id);

    @Query("UPDATE TEAM SET points = points + 1 WHERE team_id = :team_id")
    void updateDrawPoints(int team_id);


}
