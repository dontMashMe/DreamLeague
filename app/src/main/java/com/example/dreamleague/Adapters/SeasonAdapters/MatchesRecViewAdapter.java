package com.example.dreamleague.Adapters.SeasonAdapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.R;


import java.util.List;
import java.util.Objects;

public class MatchesRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Match> matches;
    private static List<Player> players;

    public MatchesRecViewAdapter(List<Match> matches, List<Player> players){
        this.matches = matches;
        MatchesRecViewAdapter.players = players;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_games, parent, false);
        return new RowViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Match match = matches.get(position);
        RowViewHolder viewHolder = (RowViewHolder) holder;


        viewHolder.txt_team1.setText(match.getObjTeamHome().getName());
        viewHolder.txt_team2.setText(match.getObjTeamAway().getName());
        viewHolder.txt_team1_score.setText(String.valueOf(match.getTeamHomeScore()));
        viewHolder.txt_team2_score.setText(String.valueOf(match.getTeamAwayScore()));
        viewHolder.img_team1.setImageResource(match.getObjTeamHome().getTeamLogo());
        viewHolder.img_team2.setImageResource(match.getObjTeamAway().getTeamLogo());

        viewHolder.bind(match);
        viewHolder.itemView.setOnClickListener(v->{
            boolean expanded = match.isExpanded();
            match.setExpanded(!expanded);
            notifyItemChanged(position);
        });
    }

    //lokalna privatna metoda jer nisam htio proslijeđivati cijeli view model
    //a opet nemam način kako dobiti ime igrača samo iz ID-a
    private static String returnPlayerName(int id){
        for(Player a : players){
            if(id == a.getPlayerId()) return a.getName();
        }
        return "";
    }

    static class RowViewHolder extends RecyclerView.ViewHolder{
        ImageView img_team1, img_team2;
        TextView txt_team1, txt_team2, txt_team1_score, txt_team2_score, txt_scorersHome, txt_scorersAway;
        View subitem;
        public RowViewHolder(@NonNull View itemView){
            super(itemView);
            img_team1 = itemView.findViewById(R.id.img_team1);
            img_team2 = itemView.findViewById(R.id.img_team2);
            txt_team1 = itemView.findViewById(R.id.txt_team1);
            txt_team2 = itemView.findViewById(R.id.txt_team_2);
            subitem = itemView.findViewById(R.id.sub_item);
            txt_team1_score = itemView.findViewById(R.id.txt_team1_score);
            txt_team2_score = itemView.findViewById(R.id.txt_team2_score);
            txt_scorersHome = itemView.findViewById(R.id.txt_scorersHome);
            txt_scorersAway = itemView.findViewById(R.id.txt_scorersAway);
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void bind(Match match){
            StringBuilder scorerBuilderHome = new StringBuilder();
            StringBuilder scorerBuilderAway = new StringBuilder();
            boolean expanded = match.isExpanded();
            subitem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            //streams, slicno kao LINQ u c#
            //arraylist.stream().noneMatch(object -> object != null);
            if(match.getMatchScores().stream().allMatch(Objects::nonNull)){
                for(MatchScores a : match.getMatchScores()){
                    if(a.getTeam() == match.getTeamHome()){
                        scorerBuilderHome.append(returnPlayerName(a.getPlayerId())).append(" (").append(a.getNumberOfGoals()).append(")").append("\n");
                    }else if (a.getTeam() == match.getTeamAway()){
                        scorerBuilderAway.append(returnPlayerName(a.getPlayerId())).append(" (").append(a.getNumberOfGoals()).append(")").append("\n");
                    }
                }
                txt_scorersHome.setText(scorerBuilderHome.toString());
                txt_scorersAway.setText(scorerBuilderAway.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
