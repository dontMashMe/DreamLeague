package com.example.dreamleague.DataModels;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dreamleague.DAOs.DreamTeamDao;
import com.example.dreamleague.DAOs.MatchScoresDao;
import com.example.dreamleague.DAOs.MatchesDao;
import com.example.dreamleague.DAOs.PlayerDao;
import com.example.dreamleague.DAOs.PlayerPointsDao;
import com.example.dreamleague.DAOs.SquadsDao;
import com.example.dreamleague.DAOs.TeamDao;

@androidx.room.Database(entities = {Player.class, Team.class, Squads.class, DreamTeam.class, Match.class, MatchScores.class, PlayerPoints.class}, version=2)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract PlayerDao playerDao();
    public abstract TeamDao teamDao();
    public abstract SquadsDao squadsDao();
    public abstract DreamTeamDao dreamTeamDao();
    public abstract MatchesDao matchesDao();
    public abstract MatchScoresDao matchScoresDao();
    public abstract PlayerPointsDao playerPointsDao();
    public static synchronized Database getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "Manager.db")
                    .createFromAsset("manager1.db")
                    //ugl pri instalaciji aplikacije room stvori schemu baze i svaki put kad se aplikacija pokreće
                    //room gleda u schemu i bazu da bi potvrdio integritet
                    //kad god nest promijenimo u bazi, schema i baza su drugačije i aplikacija se neće pokrenut
                    //pravilan način da se to odradi su migracije, ovo je samo hack around
                    .fallbackToDestructiveMigration() //makni prije obrane
                    .build();

        }
        return instance;
    }

}


