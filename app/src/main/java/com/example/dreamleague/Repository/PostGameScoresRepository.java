package com.example.dreamleague.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dreamleague.DAOs.PostGameScoresDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PostGameScores;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostGameScoresRepository {
    private final PostGameScoresDao postGameScoresDao;

    public PostGameScoresRepository(Application application){
        Database database = Database.getInstance(application);
        postGameScoresDao = database.postGameScoresDao();
    }

    public void insertPostGameScore(PostGameScores postGameScores){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> {
            postGameScoresDao.insert(postGameScores);
        });
        executor.shutdown();
    }
    public LiveData<List<PostGameScores>> getAllPostGameScores(){
        return postGameScoresDao.getAllPostGameScores();
    }

    public void deleteAllPostGameScores(){
        postGameScoresDao.deleteAllPostGameScores();
    }
}
