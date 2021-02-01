package com.example.dreamleague.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerPoints;
import com.example.dreamleague.DataModels.PostGameScores;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopWeekResults extends AppCompatActivity {

    TextView txt_players_points, txt_total_points;
    Button btn_continue;
    SeasonViewModel seasonViewModel;
    int totalPoints = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_week_results);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(SeasonViewModel.class);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        getWindow().setLayout((int) (screenWidth * .9), (int) (screenHeight * .9));
        //zamracuje ekran iza activitya
        WindowManager.LayoutParams wp = getWindow().getAttributes();
        wp.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(wp);

        Intent i = getIntent();
        //HashMap ekstenda Seriazable interface, pa ju je moguće poslati preko intenta
        @SuppressWarnings("unchecked") //uvijek je HashMap<Integer, Integer>
        HashMap<Integer, Integer> playerScoresOld = (HashMap<Integer, Integer>)i.getSerializableExtra("map"); //**NOTE, HashMap extends Seriazable, Map ne!!!
        LineupSingleton lineupSingleton = LineupSingleton.getInstance();
        List<Player> players = lineupSingleton.ReturnList();
        LiveData<List<PlayerPoints>> allPlayerPoints = seasonViewModel.getAllPlayerPoints();
        StringBuilder stringBuilder = new StringBuilder();

        allPlayerPoints.observe(this, ppPoints -> {
            List<PlayerPoints> ppPointsSt = new ArrayList<>(ppPoints);
            totalPoints = calculateTotalPoints(ppPointsSt);
            ppPointsSt = calc(playerScoresOld, ppPointsSt);
            int sum = 0;
            //loopamo izracunatu listu
            for (PlayerPoints a : ppPointsSt) {
                for (Player b : players) {
                    //spari igrače i playerId iz PlayerPoints liste
                    if (a.getPlayerId() == b.getPlayerId()) {
                        sum+=a.getPoints(); //suma bodova
                        stringBuilder.append("-").append(b.getName()).append(": ").append(a.getPoints()).append("\n");

                    }
                }
            }
            txt_players_points.setText(String.valueOf(stringBuilder));
            txt_total_points.setText(String.format("Čestitamo, osvojili ste %s bodova!", sum));
            allPlayerPoints.removeObservers(this);
        });
        txt_players_points = findViewById(R.id.txt_player_points_acq);
        txt_total_points = findViewById(R.id.pop_week_total_congrat);
        btn_continue = findViewById(R.id.btn_weekres_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.getCurrentWeek(PopWeekResults.this) < 20){
                    finish();
                }
                else{
                    LiveData<List<DreamTeam>> dreamTeam = seasonViewModel.getDreamTeam();
                    dreamTeam.observe(PopWeekResults.this, dreamTeams -> {
                        PostGameScores postGameScore = new PostGameScores(0, dreamTeams.get(0).getName(), totalPoints);
                        seasonViewModel.insertPostGameScore(postGameScore);
                        dreamTeam.removeObservers(PopWeekResults.this);
                        Intent intent = new Intent(PopWeekResults.this, PostGameActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    });

                }

            }
        });

    }
    //svaki put kad sam preko singletona pokusao prenjet a.getPointsAcquired bi vraćalo 0
    //ideja je da se bodovi ostvareni do prošlog (trenutnog?) tjedna spreme u mapu<playerId, pointsAcquired>, a bodovi ostvareni ovaj tjedan ucitaju iz baze nakon odigravanja kola
    //zatim izracunam razliku izmedu proslog tjedna i novog tjedna i voila, dobili smo broj ostvarenih bodova ovaj tjedan
    //needlessly komplicirano ali nista drugo nije radilo
    List<PlayerPoints> calc(HashMap<Integer,Integer> map, List<PlayerPoints> playerPoints){
        List<PlayerPoints> returnList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            for(PlayerPoints a : playerPoints){
                if(entry.getKey() == a.getPlayerId()){
                    returnList.add(new PlayerPoints(a.getPlayerId(), a.getPoints()-entry.getValue()));
                }
            }
        }
        return  returnList;
    }

    int calculateTotalPoints(List<PlayerPoints> preCalc){
        int sum = 0;
        for(PlayerPoints a : preCalc){
            sum += a.getPoints();
        }
        return sum;
    }

}
