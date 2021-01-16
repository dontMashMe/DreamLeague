package com.example.dreamleague.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "UserTeam")
public class DreamTeam {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int goalie;
    @NonNull
    private int defenderLeft;
    @NonNull
    private int defenderMidFirst;
    @NonNull
    private int defenderMidSecond;
    @NonNull
    private int defenderRight;
    @NonNull
    private int midLeft;
    @NonNull
    private int midMidFirst;
    @NonNull
    private int midMidSecond;
    @NonNull
    private int midRight;
    @NonNull
    private int attackLeft;
    @NonNull
    private int attackRight;


    public DreamTeam(int id, @NonNull String name, int goalie, int defenderLeft, int defenderMidFirst, int defenderMidSecond, int defenderRight, int midLeft, int midMidFirst, int midMidSecond, int midRight, int attackLeft, int attackRight) {
        this.id = id;
        this.name = name;
        this.goalie = goalie;
        this.defenderLeft = defenderLeft;
        this.defenderMidFirst = defenderMidFirst;
        this.defenderMidSecond = defenderMidSecond;
        this.defenderRight = defenderRight;
        this.midLeft = midLeft;
        this.midMidFirst = midMidFirst;
        this.midMidSecond = midMidSecond;
        this.midRight = midRight;
        this.attackLeft = attackLeft;
        this.attackRight = attackRight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getGoalie() {
        return goalie;
    }

    public void setGoalie(int goalie) {
        this.goalie = goalie;
    }

    public int getDefenderLeft() {
        return defenderLeft;
    }

    public void setDefenderLeft(int defenderLeft) {
        this.defenderLeft = defenderLeft;
    }

    public int getDefenderMidFirst() {
        return defenderMidFirst;
    }

    public void setDefenderMidFirst(int defenderMidFirst) {
        this.defenderMidFirst = defenderMidFirst;
    }

    public int getDefenderMidSecond() {
        return defenderMidSecond;
    }

    public void setDefenderMidSecond(int defenderMidSecond) {
        this.defenderMidSecond = defenderMidSecond;
    }

    public int getDefenderRight() {
        return defenderRight;
    }

    public void setDefenderRight(int defenderRight) {
        this.defenderRight = defenderRight;
    }

    public int getMidLeft() {
        return midLeft;
    }

    public void setMidLeft(int midLeft) {
        this.midLeft = midLeft;
    }

    public int getMidMidFirst() {
        return midMidFirst;
    }

    public void setMidMidFirst(int midMidFirst) {
        this.midMidFirst = midMidFirst;
    }

    public int getMidMidSecond() {
        return midMidSecond;
    }

    public void setMidMidSecond(int midMidSecond) {
        this.midMidSecond = midMidSecond;
    }

    public int getMidRight() {
        return midRight;
    }

    public void setMidRight(int midRight) {
        this.midRight = midRight;
    }

    public int getAttackLeft() {
        return attackLeft;
    }

    public void setAttackLeft(int attackLeft) {
        this.attackLeft = attackLeft;
    }

    public void setAttackRight(int attackRight) {
        this.attackRight = attackRight;
    }

    public int getAttackRight() {
        return attackRight;
    }
    public List<Integer> getAllIds(){
        List<Integer> intsList = Arrays.asList(getGoalie(), getDefenderLeft(), getDefenderMidFirst(), getDefenderMidSecond(), getDefenderRight(), getMidLeft(), getMidMidFirst(), getMidMidSecond(), getMidRight(), getAttackLeft(), getAttackRight());
        return new ArrayList<>(intsList);
    }



}
