package com.example.dreamleague.Fragments.CreateFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Adapters.CreateAdapters.CreateTeamRecViewAdapter;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.Listeners.PositionGetter;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.CreateTeamViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateTeamListFragment extends Fragment {
    private CreateTeamViewModel newModel;
    RecyclerView recyclerView;
    TextView txt_positionDesc;
    String position;
    int balance;
    ImageButton btn_sortPr;
    public PositionGetter positionGetter;


    @NonNull
    public static CreateTeamListFragment newInstance() {
        Bundle args = new Bundle();
        CreateTeamListFragment fragment = new CreateTeamListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_team_players, container, false);
        newModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(CreateTeamViewModel.class);
        setupVariables(view);

        return view;
    }


    void setupVariables(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.rview);
        txt_positionDesc = v.findViewById(R.id.txt_position_desc);
        btn_sortPr = v.findViewById(R.id.imb_sortPr);
        List<Player> playersStaticAsc = new ArrayList<>();
        List<Player> playerStaticDesc = new ArrayList<>();
        LiveData<List<Player>> playersAsc = newModel.getAllPlayersAsc();
        LiveData<List<Player>> playersDesc = newModel.getAllPlayersDesc();
        LiveData<List<Team>> teams = newModel.getAllTeams();
        LiveData<List<Squads>> squads = newModel.getAllSquads();
        squads.observe(getViewLifecycleOwner(), squads1 -> {
            teams.observe(getViewLifecycleOwner(), teams1 -> {
                playersAsc.observe(getViewLifecycleOwner(), players -> {
                    for (Player a : players) {
                        a.setTeam(newModel.setPlayerTeam(a, squads1, teams1));
                        a.getTeam().setTeamKit(newModel.setPlayerKit(a));
                    }
                    playersStaticAsc.addAll(players);
                });
                playersDesc.observe(getViewLifecycleOwner(), players2 -> {
                    for (Player a : players2) {
                        a.setTeam(newModel.setPlayerTeam(a, squads1, teams1));
                        a.getTeam().setTeamKit(newModel.setPlayerKit(a));
                    }
                    playerStaticDesc.addAll(players2);
                });
            });
        });
        btn_sortPr.setOnClickListener(new View.OnClickListener() {
            int counter = 0;
            @Override
            public void onClick(View v) {
                counter++;
                if (counter % 2 != 0) {
                    CreateTeamRecViewAdapter adapter = new CreateTeamRecViewAdapter(newModel.filterPlayerListToPosition(playersStaticAsc, position), balance, getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    btn_sortPr.setImageResource(R.drawable.arrow_up);
                } else {
                    CreateTeamRecViewAdapter adapter = new CreateTeamRecViewAdapter(newModel.filterPlayerListToPosition(playerStaticDesc, position), balance, getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    btn_sortPr.setImageResource(R.drawable.arrow_down);
                }
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        positionGetter.getPositon();
        position = Utils.position;
        balance = Utils.balance;
        if (getView() != null && position != null) {
            switch (position) {
                case "Goalkeeper":
                    txt_positionDesc.setText(getResources().getString(R.string.list_goalie));
                    break;
                case "Defender":
                    txt_positionDesc.setText(getResources().getString(R.string.list_defend));
                    break;
                case "Midfielder":
                    txt_positionDesc.setText(getResources().getString(R.string.list_mid));
                    break;
                default:
                    txt_positionDesc.setText(getResources().getString(R.string.list_attack));
                    break;
            }
            LiveData<List<Player>> players = newModel.getAllPlayers();
            LiveData<List<Team>> teams = newModel.getAllTeams();
            LiveData<List<Squads>> squads = newModel.getAllSquads();
            squads.observe(getViewLifecycleOwner(), squads1 -> teams.observe(getViewLifecycleOwner(), teams1 -> players.observe(getViewLifecycleOwner(), players1 -> {
                for (Player a : players1) {
                    a.setTeam(newModel.setPlayerTeam(a, squads1, teams1));
                    a.getTeam().setTeamKit(newModel.setPlayerKit(a));
                }
                CreateTeamRecViewAdapter adapter = new CreateTeamRecViewAdapter(newModel.filterPlayerListToPosition(players1, position), balance, getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            })));
        }
    }

    @Override
    public void onDetach() {
        positionGetter = null;
        super.onDetach();
    }
}
