package com.example.dreamleague.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

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

}
