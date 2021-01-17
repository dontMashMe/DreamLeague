package com.example.dreamleague.DataModels;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dreamleague.DAOs.DreamTeamDao;
import com.example.dreamleague.DAOs.MatchScoresDao;
import com.example.dreamleague.DAOs.MatchesDao;
import com.example.dreamleague.DAOs.PlayerDao;
import com.example.dreamleague.DAOs.SquadsDao;
import com.example.dreamleague.DAOs.TeamDao;

@androidx.room.Database(entities = {Player.class, Team.class, Squads.class, DreamTeam.class, Match.class, MatchScores.class}, version=2)
public abstract class Database extends RoomDatabase {
    private static Database instance;
    public abstract PlayerDao playerDao();
    public abstract TeamDao teamDao();
    public abstract SquadsDao squadsDao();
    public abstract DreamTeamDao dreamTeamDao();
    public abstract MatchesDao matchesDao();
    public abstract MatchScoresDao matchScoresDao();
    public static synchronized Database getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "Manager.db")
                    .createFromAsset("manager1.db")
                    .build();

        }
        return instance;
    }

}


