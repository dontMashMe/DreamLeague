package com.example.dreamleague.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dreamleague.Adapters.SeasonAdapters.MatchesRecViewAdapter;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.NumberOfGoalsTuple;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerPoints;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SeasonViewModel seasonViewModel;
    List<Player> allPlayers = new ArrayList<>();
    List<Player> playersFromTeam = new ArrayList<>();
    List<Integer> playerIds = new ArrayList<>();

    int teamId = 0;
    double teamWinrate = 0.0;
    TextView txt_team_name, txt_points, txt_most_val_player, txt_team_wr, txt_manager, txt_highest_scorer;
    ImageView img_teamKit, img_teamLogo;
    View underLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        Intent intent = getIntent();
        teamId = intent.getIntExtra("TEAM_ID", 0);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(SeasonViewModel.class);
        initializeViews();
        setupMatches(teamId);

    }

    void setupMatches(int teamId) {
        LiveData<List<Match>> allMatchesFromTeam = seasonViewModel.getAllMatchesFromTeam(teamId);
        LiveData<List<Team>> teams = seasonViewModel.getAllTeams();
        LiveData<List<Player>> allAllPlayers = seasonViewModel.getAllPlayers();
        allAllPlayers.observe(this, players -> {
            allMatchesFromTeam.observe(this, matches -> {
                teams.observe(this, teams1 -> {
                    for (Match a : matches) {
                        a.setMatchScores(seasonViewModel.getMatchScoresForGame(a.getGameId()));
                    }
                    for (Team a : teams1) {
                        if (a.getTeam_id() == teamId) {
                            teamWinrate = seasonViewModel.calculateTeamWinRate(matches, teamId, Utils.getCurrentWeek(this));
                            setupViews(a);
                            break;
                        }
                    }
                    List<Match> matchesSetupd = seasonViewModel.setTeamsAndLogos(matches, teams1);
                    MatchesRecViewAdapter matchesRecViewAdapter = new MatchesRecViewAdapter(matchesSetupd, players);
                    recyclerView.setAdapter(matchesRecViewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                });
            });
        });

    }

    void initializeViews() {
        recyclerView = findViewById(R.id.rec_view_team_info);
        txt_team_name = findViewById(R.id.txt_team_name_ti);
        txt_points = findViewById(R.id.txt_points_ti);
        underLine = findViewById(R.id.view_underline);
        img_teamLogo = findViewById(R.id.img_team_logo_ti);
        img_teamKit = findViewById(R.id.img_team_kit_ti);
        txt_most_val_player = findViewById(R.id.txt_most_val_ti);
        txt_team_wr = findViewById(R.id.txt_wpr_ti);
        txt_manager = findViewById(R.id.txt_manager_ti);
        txt_highest_scorer = findViewById(R.id.txt_most_goals_ti);


    }

    void setupViews(Team team) {
        txt_team_name.setText(team.getName());
        if (txt_team_name.getWidth() > underLine.getWidth()) {
            underLine.setMinimumWidth(txt_team_name.getWidth());
        }
        txt_points.append(" " + team.getPoints());
        img_teamKit.setImageResource(seasonViewModel.setTeamKit(team));
        img_teamLogo.setImageResource(seasonViewModel.setTeamLogo(team));

        LiveData<List<Player>> playersTeam = seasonViewModel.getPlayersFromTeam(teamId);
        playersTeam.observe(this, players -> {
            for (Player a : players) {
                allPlayers.add(a);
                playerIds.add(a.getPlayerId());
            }
            txt_most_val_player.append(" " + players.get(players.size() - 1).getName());
            if (seasonViewModel.findManager(allPlayers) != null) {
                txt_manager.append(" " + seasonViewModel.findManager(allPlayers).getName());
            } else txt_manager.append(" ? ");

            LiveData<List<NumberOfGoalsTuple>> numberOfGoals = seasonViewModel.getNumberOfGoals(playerIds);
            numberOfGoals.observe(this, new Observer<List<NumberOfGoalsTuple>>() {
                @Override
                public void onChanged(List<NumberOfGoalsTuple> numberOfGoalsTuples) {
                    if(numberOfGoalsTuples.size() > 0){ //ako se klikne na team info prije nego ijedna utakmica je odigrana
                        int max = numberOfGoalsTuples.get(0).getNumberOfGoals();
                        int playerId = numberOfGoalsTuples.get(0).getPlayerId();
                        for (NumberOfGoalsTuple a : numberOfGoalsTuples) {
                            if (a.getNumberOfGoals() > max) {
                                max = a.getNumberOfGoals();
                                playerId = a.getPlayerId();
                            }

                        }
                        for (Player a : allPlayers) {
                            if (a.getPlayerId() == playerId) {
                                txt_highest_scorer.append(" " + a.getName() + " (" + max + ")");
                            }
                        }
                    }


                }

            });


        });

        txt_team_wr.append(" " + teamWinrate * 100 + "%");


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}