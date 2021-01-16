package com.example.dreamleague.ViewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.R;
import com.example.dreamleague.Repository.DreamTeamRepository;
import com.example.dreamleague.Repository.PlayerRepository;
import com.example.dreamleague.Repository.SquadsRepository;
import com.example.dreamleague.Repository.TeamRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreateTeamViewModel extends AndroidViewModel {
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private SquadsRepository squadsRepository;
    private DreamTeamRepository dreamTeamRepository;

    private LiveData<List<Team>> allTeams;
    private LiveData<List<Player>> allPlayers;
    private LiveData<List<Squads>> allSquads;


    public CreateTeamViewModel(@NonNull Application application){
        super(application);
        playerRepository = new PlayerRepository(application);
        teamRepository = new TeamRepository(application);
        squadsRepository = new SquadsRepository(application);
        dreamTeamRepository = new DreamTeamRepository(application);
        allPlayers = playerRepository.getAllPlayers();
        allTeams = teamRepository.getAllTeams();
        allSquads = squadsRepository.getAllSquads();
    }

    /********GETTERS**********/
    public LiveData<List<Player>> getAllPlayersAsc(){
        return playerRepository.getAllPlayersASC();
    }
    public LiveData<List<Player>> getAllPlayersDesc(){
        return playerRepository.getAllPlayersDESC();
    }
    public LiveData<List<Player>> getAllPlayers(){
        return allPlayers;
    }
    public LiveData<List<Team>> getAllTeams(){
        return allTeams;
    }
    public LiveData<List<Squads>> getAllSquads() {
        return allSquads;
    }
    public LiveData<List<Player>> getPlayersSearch(String name){
        return playerRepository.getPlayersSearch(name);
    }

    public String formatName(Player player){
        String[] name_surname = player.getName().split(" ");
        if(name_surname.length != 1){
            String surname = name_surname[1];
            if(surname.length() <= 9){
                return surname;
            }else{
                StringBuilder formated_lastName = new StringBuilder();
                for(int i = 0; i < 7; i++){
                    formated_lastName.append(surname.charAt(i));
                }
                formated_lastName.append(" ..");
                return formated_lastName.toString();
            }
        }
        else{
            return name_surname[0];
        }
    }
    public Team setPlayerTeam(Player player, List<Squads> squads, List<Team> teams){
        int temp = 0;
        for(Squads a : squads){
            if(a.getPlayerId() == player.getPlayerId()){
                temp = a.getTeamId();
                break;
            }
        }
        for(Team a : teams){
            if(a.getTeam_id() == temp){
               return a;
            }
        }

        return null;
    }
    public void insertDreamTeam(DreamTeam dreamTeam){
        dreamTeamRepository.insertDreamTeam(dreamTeam);
    }
    public void setRealPosOfDreamTeam(Player player, DreamTeam dreamTeam){
        switch(player.getRealPosition()){
            case 1:
                dreamTeam.setGoalie(player.getPlayerId());
                break;
            case 2:
                dreamTeam.setDefenderLeft(player.getPlayerId());
                break;
            case 3:
                dreamTeam.setDefenderMidFirst(player.getPlayerId());
                break;
            case 4:
                dreamTeam.setDefenderMidSecond(player.getPlayerId());
                break;
            case 5:
                dreamTeam.setDefenderRight(player.getPlayerId());
                break;
            case 6:
                dreamTeam.setMidLeft(player.getPlayerId());
                break;
            case 7:
                dreamTeam.setMidMidFirst(player.getPlayerId());
                break;
            case 8:
                dreamTeam.setMidMidSecond(player.getPlayerId());
                break;
            case 9:
                dreamTeam.setMidRight(player.getPlayerId());
                break;
            case 10:
                dreamTeam.setAttackLeft(player.getPlayerId());
                break;
            case 11:
                dreamTeam.setAttackRight(player.getPlayerId());
                break;
        }
    }

    public int calcRemainingBalance(Player player, int balance){
        return balance - player.getPlayerValue();
    }

    public Boolean checkCompletion(DreamTeam dreamTeam){
        if(dreamTeam!= null){
            for(int a : dreamTeam.getAllIds()){
                if(a == 0){
                    Toast.makeText(getApplication().getApplicationContext(), "Tim nije kompletan!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    Map<String, Integer> map = new HashMap<>();
    void fillMap(){
        map.put("Arsenal FC", R.drawable.arsenal_kit);
        map.put("Aston Villa FC", R.drawable.aston_kit);
        map.put("Everton FC", R.drawable.everton_kit);
        map.put("Fulham FC", R.drawable.fulham_kit);
        map.put("Liverpool FC", R.drawable.liverpool_kit);
        map.put("Manchester City FC", R.drawable.mnc_kit);
        map.put("Manchester United FC", R.drawable.mnu_kit);
        map.put("Tottenham Hotspur FC", R.drawable.totenham_kit);
        map.put("Wolverhampton Wanderers FC", R.drawable.wolves_kit);
        map.put("Burnley FC", R.drawable.burnley_kit);
        map.put("Leicester City FC", R.drawable.leicester_kit);
        map.put("Southampton FC", R.drawable.southamp_kit);
        map.put("Leeds United FC", R.drawable.leeds);
        map.put("Crystal Palace FC", R.drawable.cpa_kit);
        map.put("Sheffield United FC", R.drawable.sheffield);
        map.put("Brighton & Hove Albion FC", R.drawable.brighton_kit);
        map.put("West Ham United FC", R.drawable.wham_kit);
        map.put("Newcastle United FC", R.drawable.newcas_kit);
        map.put("Chelsea FC", R.drawable.chelsea_kit);
        map.put("West Bromwich Albion FC", R.drawable.wba_fc);
    }
    public int setPlayerKit(Player player){
        fillMap();
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            if(player.getTeam().getName().equals(entry.getKey())){
                return entry.getValue();
            }
        }
        return 0;
    }

    public List<Player> filterPlayerListToPosition(List<Player> players, String position_str){
        List<Player> returnList = new ArrayList<>();
        LineupSingleton lineupSingleton = LineupSingleton.getInstance();
        List<Player> boughtPlayers = lineupSingleton.ReturnList();
        for(Player a : players){
            if(a.getPosition().equals(position_str)){
                returnList.add(a);
            }
        }
        for(Player a : boughtPlayers){
            returnList.remove(a);
        }

        return returnList;
    }

    public Player returnRandPlayer(List<Player> players, Integer realPos){
        LineupSingleton lineupSingleton = LineupSingleton.getInstance();
        List<Player> boughtPlayers = lineupSingleton.ReturnList();
        List<Player> tempList = new ArrayList<>(players);
        for(Player a : boughtPlayers){
            tempList.remove(a);
        }
        if(realPos == 1){
            //for-each petlja daje ConcurrentModificationException???
            //standardna for petlja radi
            for(int i = 0; i < tempList.size(); i++){
                Player currPlayer = tempList.get(i);
                if(!tempList.get(i).getPosition().equals("Goalkeeper")){
                    tempList.remove(currPlayer);
                    i--; //izbjegavanje siftanog elementa
                }
            }

        }else if(isBetween(realPos, 2,5)){
            for(int i = 0; i < tempList.size(); i++){
                Player currPlayer = tempList.get(i);
                if(!tempList.get(i).getPosition().equals("Defender")){
                    tempList.remove(currPlayer);
                    i--; //izbjegavanje siftanog elementa
                }
            }
        }else if(isBetween(realPos,6,9)){
            for(int i = 0; i < tempList.size(); i++){
                Player currPlayer = tempList.get(i);
                if(!tempList.get(i).getPosition().equals("Midfielder")){
                    tempList.remove(currPlayer);
                    i--; //izbjegavanje siftanog elementa
                }
            }
        }else if(realPos > 9){
            for(int i = 0; i < tempList.size(); i++){
                Player currPlayer = tempList.get(i);
                if(!tempList.get(i).getPosition().equals("Attacker")){
                    tempList.remove(currPlayer);
                    i--; //izbjegavanje siftanog elementa
                }
            }
        }
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(tempList.size());
        boughtPlayers.add(tempList.get(index));
        return tempList.get(index);
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }




}
