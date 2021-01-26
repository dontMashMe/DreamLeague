package com.example.dreamleague.Adapters.SeasonAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.DataModels.PostGameScores;
import com.example.dreamleague.R;

import java.util.List;

public class PostGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<PostGameScores> postGameScoresList;

    public PostGameAdapter(List<PostGameScores> postGameScores) {
        this.postGameScoresList = postGameScores;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_game_row, parent, false);
        return new PostRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostGameScores postGameScore = postGameScoresList.get(position);
        PostRowViewHolder viewHolder = (PostRowViewHolder) holder;
        viewHolder.txt_name.setText(postGameScore.getTeamName());
        viewHolder.txt_points.setText(String.valueOf(postGameScore.getTotalScore()));
        viewHolder.txt_pos.setText(String.valueOf(position));
    }
    static class PostRowViewHolder extends RecyclerView.ViewHolder{

        TextView txt_pos,txt_name, txt_points;
        public PostRowViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_points = itemView.findViewById(R.id.txt_points_total_post);
            txt_name = itemView.findViewById(R.id.txt_name_post);
            txt_pos = itemView.findViewById(R.id.txt_pos_total_post);
        }
    }

    @Override
    public int getItemCount() {
        return postGameScoresList.size();
    }
}
