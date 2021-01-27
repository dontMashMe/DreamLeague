package com.example.dreamleague.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dreamleague.DataModels.PostGameScores;

import java.util.List;

@Dao
public interface PostGameScoresDao {

    @Insert
    void insert(PostGameScores postGameScores);

    @Query("SELECT * FROM PostGameScores ORDER BY totalScore DESC")
    LiveData<List<PostGameScores>> getAllPostGameScores();

    @Query("DELETE FROM PostGameScores")
    void deleteAllPostGameScores();
}
