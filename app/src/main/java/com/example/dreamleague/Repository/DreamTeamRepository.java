package com.example.dreamleague.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.dreamleague.DAOs.DreamTeamDao;
import com.example.dreamleague.DataModels.Database;
import com.example.dreamleague.DataModels.DreamTeam;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DreamTeamRepository {
    private DreamTeamDao dreamTeamDao;
    private LiveData<List<DreamTeam>> dreamTeam;

    public DreamTeamRepository(Application application){
        Database database = Database.getInstance(application);
        dreamTeamDao = database.dreamTeamDao();
        dreamTeam = dreamTeamDao.getDreamTeam();
    }

    //Room ne dopušta operacije Inserta i Deleta na glavnom threadu (glavni thread je zaslužen za crtanje UI-a)
    //te operacije je potrebno prebaciti na off-thread
    //executor postiže to
    public void insertDreamTeam(DreamTeam dreamTeam){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(()->{
            //background thread
            dreamTeamDao.insert(dreamTeam);
            handler.post(()->{
                //glavni thread
            });
        });
        executor.shutdown();

    }

    public LiveData<List<DreamTeam>> getDreamTeam(){
        return this.dreamTeam;
    }

    public void sellGoalie(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellGoalie());
        executor.shutdown();
    };

    public void sellDefenderLeft(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellDefenderLeft());
        executor.shutdown();
    };

    public void sellDefenderMidFirst(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellDefenderMidFirst());
        executor.shutdown();
    };

    public void sellDefenderMidSecond(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellDefenderMidSecond());
        executor.shutdown();
    };

    public void sellDefenderRight(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellDefenderRight());
        executor.shutdown();
    };

    public void sellMidLeft(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellMidLeft());
        executor.shutdown();
    };

    public void sellMidMidFirst(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellMidMidFirst());
        executor.shutdown();
    };

    public void sellMidMidSecond(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellMidMidSecond());
        executor.shutdown();
    };

    public void sellMidRight(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellMidRight());
        executor.shutdown();
    };

    public void sellAttackerLeft(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellAttackerLeft());
        executor.shutdown();
    };

    public void sellAttackerRight(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> dreamTeamDao.sellAttackerRight());
        executor.shutdown();
    };

}
