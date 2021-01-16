package com.example.dreamleague.Adapters.CreateAdapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.R;

import java.util.List;

public class CreateTeamRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Player> players;
    int balance;
    Context context;

    public CreateTeamRecViewAdapter(List<Player> players, int balance, Context context){
        this.players = players;
        this.balance = balance;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_crteam_rec, parent, false);
        return new RowViewHolder(view);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Player player = players.get(position);
        RowViewHolder viewHolder = (RowViewHolder) holder;

        viewHolder.txt_name.setText(formatName(player.getName()));
        viewHolder.txt_PR.setText(String.valueOf(player.getPlayerRating()));
        viewHolder.txt_value.setText(String.format("%.2fM$", player.getPlayerValue() / 1000000.0));
        viewHolder.img_kit.setImageResource(player.getTeam().getTeamKit());
        viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(balance < player.getPlayerValue()){
                    Toast.makeText(v.getContext(), context.getResources().getString(R.string.dovoljno_novca), Toast.LENGTH_SHORT).show();
                }else{
                    LineupSingleton lineupSingleton = LineupSingleton.getInstance();
                    lineupSingleton.AddPlayer(player);
                    holder.itemView.setBackgroundColor(Color.parseColor("#74eb34"));
                    Toast.makeText(v.getContext(), context.getResources().getString(R.string.igrac) + player.getName() + context.getResources().getString(R.string.uspjesno_kupljen), Toast.LENGTH_SHORT).show();
                    players.remove(player);
                    notifyItemRangeChanged(position, players.size());
                }

            }
        });

    }
    static class RowViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton buyButton;
        TextView txt_name, txt_PR, txt_value;
        ImageView img_kit;
        public RowViewHolder(@NonNull View itemView){
            super(itemView);
            txt_name = itemView.findViewById(R.id.text_name);
            txt_PR = itemView.findViewById(R.id.text_PR);
            buyButton = itemView.findViewById(R.id.buy_button);
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
