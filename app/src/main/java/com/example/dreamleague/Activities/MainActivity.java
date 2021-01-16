package com.example.dreamleague.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*
    ovaj activity ce uglavnom sluziti samo za odluku koja faza igre će se pokretati
    gledat ce kroz tablicu timova i ukoliko ne uoci predefinirani ID range user created timova
    pokretat ce se activity za kreaciju tima (u pocetku, kada dodam
    leaderboard vjerojatno cu morati dodati nesto u bazu sta bi provjerilo postoje li drugi timovi ?
    */
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeasonViewModel seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(SeasonViewModel.class);
        LiveData<List<DreamTeam>> dreamTeamLiveData = seasonViewModel.getDreamTeam();
        dreamTeamLiveData.observe(this, dreamTeams -> {
            if(dreamTeams.size() > 0){
                intent = new Intent(this, SeasonActivity.class);
            }else{
                intent = new Intent(this, CreateTeamActivity.class);
                LiveData<List<Team>> allTeams = seasonViewModel.getAllTeams();
                allTeams.observe(this, seasonViewModel::InitialMatchesGenerator);
                Utils.putCurrentWeekSharedPreference(getApplicationContext(), 1);
            }
            startActivity(intent);
            finish();
        });

    }

}