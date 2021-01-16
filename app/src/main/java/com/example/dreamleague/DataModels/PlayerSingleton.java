package com.example.dreamleague.DataModels;

public class PlayerSingleton {
    private static PlayerSingleton single_instance = null;
    private Player player = new Player(1,"-","-","-","-",0);
    private PlayerSingleton(){};
    public static PlayerSingleton getInstance(){
        if(single_instance == null){
            single_instance = new PlayerSingleton();
        }
        return single_instance;
    }
    public void SetPlayer(Player player){
        this.player = player;
    }
    public Player returnPlayer(){
        return this.player;
    }
}
