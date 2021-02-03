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
    startactivity, odlucuje koja faza aplikacije će se pokrenuti.
    ukoliko ne postoji trenutan dreamteam redak u tablici, pokreni kreaciju tima
    ako postoji, pokreni sezonu.
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