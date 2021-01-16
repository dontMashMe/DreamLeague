package com.example.dreamleague.Adapters.SeasonAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Team> teams;
    public LeaderboardAdapter(List<Team> teams){
        this.teams = teams;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leaderboard,parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Team team = teams.get(position);
        RowViewHolder viewHolder = (RowViewHolder) holder;
        viewHolder.txt_position.setText(String.valueOf(position + 1));
        viewHolder.txt_team_points.setText(String.valueOf(team.getPoints()));
        viewHolder.txt_team_name.setText(team.getName());
        viewHolder.img_team.setImageResource(team.getTeamLogo());
        viewHolder.txt_games.setText(team.getLast5games());

    }

    static class RowViewHolder extends  RecyclerView.ViewHolder{
        ImageView img_team;
        TextView txt_position, txt_team_name, txt_team_points, txt_games;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            img_team = itemView.findViewById(R.id.img_leaderboard);
            txt_position = itemView.findViewById(R.id.txt_position);
            txt_team_name = itemView.findViewById(R.id.txt_team_name_leaderboard);
            txt_team_points = itemView.findViewById(R.id.txt_points);
            txt_games = itemView.findViewById(R.id.txt_scoreboard_lastgames);
        }
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }
}
