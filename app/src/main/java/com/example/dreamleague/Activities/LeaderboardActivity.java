package com.example.dreamleague.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Adapters.SeasonAdapters.LeaderboardAdapter;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    ImageButton img_backButton;
    RecyclerView recyclerView;
    SeasonViewModel seasonViewModel;
    List<Team> teamsStatic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards_layout);
        seasonViewModel = new ViewModelProvider(this).get(SeasonViewModel.class);

        setupVars();
    }


    void setupVars(){
        teamsStatic = new ArrayList<>();
        img_backButton = findViewById(R.id.img_back);
        img_backButton.setOnClickListener(v-> {
            startActivity(new Intent(this, SeasonActivity.class));
            finish();
        });
        recyclerView = findViewById(R.id.recycler_leaderboard);
        LiveData<List<Team>> getAllTeamsByPoints = seasonViewModel.getAllTeams();
        List<Team> adapterList = new ArrayList<>();
        LeaderboardAdapter adapter = new LeaderboardAdapter(adapterList);
        int currentWeek = Utils.getCurrentWeek(this);
        getAllTeamsByPoints.observe(this, teams -> {
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            for(Team a : teams){
                StringBuilder last5games = new StringBuilder();
                LiveData<List<Match>> last5matches = seasonViewModel.last5Matches(currentWeek, a.getTeam_id());
                last5matches.observe(this, matches->{
                    if(matches.size() > 0){
                        for(Match b : matches){
                            if(b.getTeamAway() == a.getTeam_id()){
                                if(b.getTeamAwayScore() > b.getTeamHomeScore()) last5games.append("W-");
                                else if(b.getTeamAwayScore() < b.getTeamHomeScore()) last5games.append("L-");
                                else if(b.getTeamAwayScore() == b.getTeamHomeScore()) last5games.append("D-");
                            }
                            else if(b.getTeamHome() == a.getTeam_id()) {
                                if(b.getTeamHomeScore() > b.getTeamAwayScore()) last5games.append("W-");
                                else if(b.getTeamHomeScore() < b.getTeamAwayScore()) last5games.append("L-");
                                else if(b.getTeamHomeScore() == b.getTeamAwayScore()) last5games.append("D-");
                            }
                        }
                        a.setLast5games(last5games.toString().substring(0, last5games.length() - 1));
                        adapter.notifyDataSetChanged();
                    }
                });
                adapterList.add(a);
                adapter.notifyDataSetChanged();
            }
            seasonViewModel.setTeamLogos(adapterList);

        });

    }
}
