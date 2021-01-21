package com.example.dreamleague.Adapters.SeasonAdapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerSingleton;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.Listeners.TransferListener;
import com.example.dreamleague.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class TransfersAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Player> players;
    private final int  USER_TEAM_FLAG;
    private final TransferListener listener;
    public TransfersAdapter(List<Player> players, TransferListener listener , int USER_TEAM_FLAG){
        this.players = players;
        this.listener = listener;
        this.USER_TEAM_FLAG = USER_TEAM_FLAG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_crteam_rec, parent, false);
        return new RowViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Player player = players.get(position);
        RowViewHolder viewHolder = (RowViewHolder) holder;

        viewHolder.txt_name.setText(formatName(player.getName()));
        viewHolder.txt_PR.setText(String.valueOf(player.getPlayerRating()));
        viewHolder.txt_value.setText(String.format("%.2fM$", player.getPlayerValue() / 1000000.0));
        viewHolder.img_kit.setImageResource(player.getTeam().getTeamKit());
        viewHolder.txt_pos.setText(player.getPosition());
        //prodaj
        if(USER_TEAM_FLAG == 0){
            viewHolder.buy_sell_button.setImageResource(R.drawable.sell);
            viewHolder.buy_sell_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                    playerSingleton.SetPlayer(player);
                    viewHolder.transferListenerWeakReference.get().onPositionClicked(position);
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#fc2c03"));
                    Toast.makeText(v.getContext(), "Uspješno prodan!", Toast.LENGTH_SHORT).show();
                    players.remove(player);
                    notifyItemRangeChanged(position, players.size());
                }
            });
        }
        //kupi
        else if(USER_TEAM_FLAG == 1){
            viewHolder.buy_sell_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(player.getPlayerValue() < Utils.getBalance(v.getContext())){
                        PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                        playerSingleton.SetPlayer(player);
                        viewHolder.transferListenerWeakReference.get().onPositionClicked(position);
                        viewHolder.itemView.setBackgroundColor(Color.parseColor("#61eb34"));
                        players.remove(player);
                        notifyItemRangeChanged(position, players.size());
                    }else{
                        Toast.makeText(v.getContext(), "Nemate dovoljno novca!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    static class RowViewHolder extends RecyclerView.ViewHolder {

        ImageButton buy_sell_button;
        ImageView img_kit;
        TextView txt_name, txt_PR, txt_value, txt_pos;
        //weakreference je istao kao da detachamo listener u on destroy, sprjecava memory leakove
        private final WeakReference<TransferListener> transferListenerWeakReference;

        public RowViewHolder(@NonNull View itemView, TransferListener listener) {
            super(itemView);
            transferListenerWeakReference = new WeakReference<>(listener);
            txt_name = itemView.findViewById(R.id.text_name);
            txt_PR = itemView.findViewById(R.id.text_PR);
            txt_value = itemView.findViewById(R.id.txt_value);
            img_kit = itemView.findViewById(R.id.img_kit);
            txt_pos = itemView.findViewById(R.id.txt_player_position);
            buy_sell_button = itemView.findViewById(R.id.buy_button);
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
