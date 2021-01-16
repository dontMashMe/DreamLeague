package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.dreamleague.DataModels.Squads;

import java.util.List;
@Dao
public interface SquadsDao {

    @Query("SELECT * FROM Squads")
    LiveData<List<Squads>> getAllSquads();

}
