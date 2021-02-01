package com.example.dreamleague.Activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerSingleton;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.Listeners.CaptainListener;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.text.NumberFormat;


public class PopPlayerInfo extends AppCompatActivity {

    SeasonViewModel seasonViewModel;
    TextView txt_player_name, txt_player_pr, txt_player_val, txt_player_team, txt_player_points;
    ImageView img_player_team_logo;
    ImageButton imb_sell, imb_make_captain;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(SeasonViewModel.class);


        setContentView(R.layout.pop_playerinfo_layout);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        getWindow().setLayout((int) (screenWidth * .9), (int) (screenHeight * .3));
        //zamracuje ekran iza activitya
        WindowManager.LayoutParams wp = getWindow().getAttributes();
        wp.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(wp);
        //-------------------------------------

        PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
        Player player = playerSingleton.returnPlayer();
        LiveData<Integer> playerPoints = seasonViewModel.getPlayerPoints(player.getPlayerId());
        playerPoints.observe(this, points -> {
            setupVars(player);
            txt_player_points = findViewById(R.id.txt_player_points_pop);
            txt_player_points.setText(String.format("Bodovi: %s", points));
        });
    }

    void setupVars(Player player) {

        txt_player_name = findViewById(R.id.txt_player_name_pop);
        txt_player_name.setText(player.getName());
        txt_player_name.setPaintFlags(txt_player_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_player_pr = findViewById(R.id.txt_player_pr_pop);
        txt_player_pr.setText(String.valueOf(player.getPlayerRating()));

        txt_player_val = findViewById(R.id.txt_player_val_pop);
        txt_player_val.setText(String.format(getString(R.string.vrijednost), NumberFormat.getInstance().format(player.getPlayerValue())));

        txt_player_team = findViewById(R.id.txt_team_name_pop);
        txt_player_team.setText(player.getTeam().getName());


        img_player_team_logo = findViewById(R.id.img_team_logo_pop);
        img_player_team_logo.setImageResource(player.getTeam().getTeamLogo());

        imb_sell = findViewById(R.id.imb_sell);
        imb_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seasonViewModel.sellPlayer(player.getRealPosition(), player.getPlayerId());
                Toast.makeText(PopPlayerInfo.this, "Igrač " + player.getName() + " uspješno prodan!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        imb_make_captain = findViewById(R.id.imb_make_cap);
        imb_make_captain.setOnClickListener(v -> {
            Utils.putCaptainId(PopPlayerInfo.this, player.getPlayerId());
            Toast.makeText(PopPlayerInfo.this, "Uspjeh! " + player.getName() + " je novi kapetan!", Toast.LENGTH_SHORT).show();
        });

    }

}
