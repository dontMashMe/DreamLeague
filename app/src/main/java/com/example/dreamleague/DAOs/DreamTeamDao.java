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


    //kad bolje razmislim tablica je trebala izgledati drugacije
    //trebala je sadrzavati samo playerId, realPosition id
    //npr playerId 234 - realPositionId 1 -> golman.. itd.

    //i am sorry
    @Query("UPDATE UserTeam SET goalie = 0")
    void sellGoalie();

    @Query("UPDATE UserTeam SET defenderLeft = 0")
    void sellDefenderLeft();

    @Query("UPDATE UserTeam SET defenderMidFirst = 0")
    void sellDefenderMidFirst();

    @Query("UPDATE UserTeam SET defenderMidSecond = 0")
    void sellDefenderMidSecond();

    @Query("UPDATE UserTeam SET defenderRight = 0")
    void sellDefenderRight();

    @Query("UPDATE UserTeam SET midLeft = 0")
    void sellMidLeft();

    @Query("UPDATE UserTeam SET midMidFirst = 0")
    void sellMidMidFirst();

    @Query("UPDATE UserTeam SET midMidSecond = 0")
    void sellMidMidSecond();

    @Query("UPDATE UserTeam SET midRight = 0")
    void sellMidRight();

    @Query("UPDATE UserTeam SET attackLeft = 0")
    void sellAttackerLeft();

    @Query("UPDATE UserTeam SET attackRight = 0")
    void sellAttackerRight();



    @Query("UPDATE UserTeam SET goalie = :playerId")
    void buyGoalie(int playerId);

    @Query("UPDATE UserTeam SET defenderLeft = :playerId")
    void buydefenderLeft(int playerId);

    @Query("UPDATE UserTeam SET defenderMidFirst = :playerId")
    void buydefenderMidFirst(int playerId);

    @Query("UPDATE UserTeam SET defenderMidSecond = :playerId")
    void buydefenderMidSecond(int playerId);

    @Query("UPDATE UserTeam SET defenderRight = :playerId")
    void buydefenderRight(int playerId);

    @Query("UPDATE UserTeam SET midLeft = :playerId")
    void buymidLeft (int playerId);

    @Query("UPDATE UserTeam SET midMidFirst = :playerId")
    void buymidMidFirst(int playerId);

    @Query("UPDATE UserTeam SET midMidSecond = :playerId")
    void buymidMidSecond (int playerId);

    @Query("UPDATE UserTeam SET midRight = :playerId")
    void buymidRight(int playerId);

    @Query("UPDATE UserTeam SET attackLeft = :playerId")
    void buyAttackerLeft(int playerId);

    @Query("UPDATE UserTeam SET attackRight = :playerId")
    void buyAttackerRight(int playerId);


}
