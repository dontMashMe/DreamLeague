package com.example.dreamleague.Adapters.SeasonAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Adapters.CreateAdapters.CreateTeamRecViewAdapter;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.R;

import java.util.List;

public class TransfersAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Player> players;
    public TransfersAdapter(List<Player> players){
        this.players = players;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_crteam_rec, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Player player = players.get(position);
        RowViewHolder viewHolder = (RowViewHolder) holder;

        viewHolder.txt_name.setText(formatName(player.getName()));
        viewHolder.txt_PR.setText(String.valueOf(player.getPlayerRating()));
        viewHolder.txt_value.setText(String.format("%.2fM$", player.getPlayerValue() / 1000000.0));
        viewHolder.img_kit.setImageResource(player.getTeam().getTeamKit());
    }
    static class RowViewHolder extends RecyclerView.ViewHolder{

        ImageView img_kit;
        TextView txt_name, txt_PR, txt_value;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.text_name);
            txt_PR = itemView.findViewById(R.id.text_PR);
            txt_value = itemView.findViewById(R.id.txt_value);
            img_kit = itemView.findViewById(R.id.img_kit);
        }
    }
    String formatName(String name){
        if(name.length() <= 12) return name;
        else{
            String[] name_surname = name.split(" ");
            return name_surname[1];
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
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
