package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dreamleague.DataModels.DreamTeam;

import java.util.List;

@Dao
public interface DreamTeamDao {

    @Insert
    void insert(DreamTeam dreamTeam);

    @Update
    void update(DreamTeam dreamTeam);

    @Delete
    void delete(DreamTeam dreamTeam);

    @Query("SELECT * FROM UserTeam")
    LiveData<List<DreamTeam>> getDreamTeam();


}
