package com.example.dreamleague.Fragments.SeasonFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Activities.LeaderboardActivity;
import com.example.dreamleague.Adapters.CreateAdapters.CreateTeamRecViewAdapter;
import com.example.dreamleague.Adapters.SeasonAdapters.MatchesRecViewAdapter;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.MatchResult;

public class MatchesFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    SeasonViewModel seasonViewModel;
    RecyclerView recyclerView;
    Spinner spinner;
    List<Player> players = new ArrayList<>();
    ImageButton imb_leaderBoards;


    @NonNull
    public static MatchesFragment newInstance() {
        Bundle args = new Bundle();
        MatchesFragment fragment = new MatchesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(SeasonViewModel.class);
        setupData(view);
        return view;
    }

    void setupData(View view) {
        imb_leaderBoards = view.findViewById(R.id.imb_leaderboards);
        imb_leaderBoards.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), LeaderboardActivity.class));
            getActivity().finish();
        });
        players = new ArrayList<>();


        spinner = view.findViewById(R.id.spinner_week);
        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        LiveData<List<Player>> allPlayers = seasonViewModel.getAllPlayers();
        allPlayers.observe(getViewLifecycleOwner(), players1 -> players.addAll(players1));

        recyclerView = view.findViewById(R.id.rec_view_matches);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LiveData<List<Match>> matchesFromWeek = seasonViewModel.getMatchesFromWeek((Integer) parent.getItemAtPosition(position));
        LiveData<List<Team>> teams = seasonViewModel.getAllTeams();
        matchesFromWeek.observe(getViewLifecycleOwner(), matches -> {
            teams.observe(getViewLifecycleOwner(), teams1 -> {
                for (Match a : matches) {
                    a.setMatchScores(seasonViewModel.getMatchScoresForGame(a.getGameId()));
                }
                List<Match> matchesSetupd = seasonViewModel.setTeamsAndLogos(matches, teams1);
                //nazalost moram poslat i listu igrača kako bi prikazao strijelce
                MatchesRecViewAdapter matchesRecViewAdapter = new MatchesRecViewAdapter(matchesSetupd, players);
                recyclerView.setAdapter(matchesRecViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            });
        });

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
