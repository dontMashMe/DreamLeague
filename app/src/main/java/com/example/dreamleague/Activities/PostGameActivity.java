package com.example.dreamleague.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Adapters.SeasonAdapters.PostGameAdapter;
import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.PostGameScores;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.List;

public class PostGameActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SeasonViewModel seasonViewModel;
    TextView points_acquired;
    Button btn_newSeason;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_game_layout);
        btn_newSeason = findViewById(R.id.btn_start_new_season);
        intent = new Intent(this, MainActivity.class);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(SeasonViewModel.class);
        recyclerView = findViewById(R.id.post_game_recycler);
        points_acquired = findViewById(R.id.txt_points_acquired_season);
        LiveData<List<PostGameScores>> postGameScoresLiveData = seasonViewModel.getAllPostGameScores();
        postGameScoresLiveData.observe(this, postGameScores -> {
            points_acquired.append(String.valueOf(postGameScores.get(postGameScores.size() - 1).getTotalScore()));
            PostGameAdapter postGameAdapter = new PostGameAdapter(postGameScores);
            recyclerView.setAdapter(postGameAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        List<DreamTeam> dreamTeams = new ArrayList<>();
        LiveData<List<DreamTeam>> dreamTeamLiveData = seasonViewModel.getDreamTeam();
        dreamTeamLiveData.observe(this, new Observer<List<DreamTeam>>() {
            @Override
            public void onChanged(List<DreamTeam> dreamTeams) {
                for(DreamTeam a : dreamTeams){
                    seasonViewModel.deleteDreamTeam(a);
                }
            }
        });
        btn_newSeason.setOnClickListener(kickOffClick);
    }

    View.OnClickListener kickOffClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(intent);

        }
    };
}
