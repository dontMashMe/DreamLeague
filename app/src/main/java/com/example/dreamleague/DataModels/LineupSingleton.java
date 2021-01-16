package com.example.dreamleague.DataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineupSingleton {
    private static LineupSingleton single_instance = null;
    private ArrayList<Player> players = new ArrayList<>();

    private LineupSingleton(){};

    public static LineupSingleton getInstance()
    {
        if(single_instance == null){
            single_instance = new LineupSingleton();
        }
        return single_instance;
    }

    public void AddPlayer(Player player){
        this.players.add(player);
    }
    public List<Player> ReturnList(){return this.players;}

}
