package com.example.dreamleague.Activities;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerSingleton;
import com.example.dreamleague.R;
import java.text.NumberFormat;


public class PopPlayerInfo extends Activity {

    TextView txt_player_name, txt_player_dob, txt_player_pr, txt_player_val, txt_player_team, txt_player_points;
    ImageView img_player_team_logo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_layout);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        getWindow().setLayout((int)(screenWidth * .9), (int)(screenHeight *.3));

        //zamracuje ekran iza activitya
        WindowManager.LayoutParams wp = getWindow().getAttributes();
        wp.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(wp);
        //-------------------------------------
        PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
        Player player = playerSingleton.returnPlayer();
        setupVars(player);
    }

    void setupVars(Player player){
        txt_player_name = findViewById(R.id.txt_player_name_pop);
        txt_player_name.setText(player.getName());
        txt_player_name.setPaintFlags(txt_player_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_player_dob = findViewById(R.id.txt_player_dob_pop);
        txt_player_dob.setText(String.format("Datum rođenja: %s", formatDate(player.getDateOfBirth())));

        txt_player_pr = findViewById(R.id.txt_player_pr_pop);
        txt_player_pr.setText(String.valueOf(player.getPlayerRating()));

        txt_player_val = findViewById(R.id.txt_player_val_pop);
        txt_player_val.setText(String.format(getString(R.string.vrijednost), NumberFormat.getInstance().format(player.getPlayerValue())));

        txt_player_team = findViewById(R.id.txt_team_name_pop);
        txt_player_team.setText(player.getTeam().getName());

        txt_player_points = findViewById(R.id.txt_player_points_pop);
        txt_player_points.setText(String.format("Bodovi: %s", 32));

        img_player_team_logo = findViewById(R.id.img_team_logo_pop);
        img_player_team_logo.setImageResource(player.getTeam().getTeamLogo());

    }

    private String formatDate(String date){
        StringBuilder year = new StringBuilder();
        StringBuilder month = new StringBuilder();
        StringBuilder day = new StringBuilder();
        for(int i =0; i < date.length(); i++){
            if(i<4){
                year.append(date.charAt(i));
            }else if(i>=5 && i< 7){
                month.append(date.charAt(i));
            }else if(i>=8 && i<10){
                day.append(date.charAt(i));
            }
        }
        return day + "/" + month + "/" + year;
    }
}
