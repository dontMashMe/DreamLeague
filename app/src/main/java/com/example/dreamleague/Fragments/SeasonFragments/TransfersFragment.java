package com.example.dreamleague.Fragments.SeasonFragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamleague.Adapters.SeasonAdapters.TransfersAdapter;
import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerSingleton;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.Listeners.TransferListener;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransfersFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner spinnerTeams, spinnerPositions;
    RecyclerView recyclerView;
    ImageButton imbSort, imbSearch;
    SeasonViewModel seasonViewModel;
    RadioGroup radioGroup;
    RadioButton buy, sell;
    TextView balance;
    //1 - kupi, 0 - prodaj
    private static int USER_TEAM_FLAG = 1;
    private static String TEAM_FLAG = "";
    private static String POSITION_FLAG = "";

    @NonNull
    public static TransfersFragment newInstance() {
        Bundle args = new Bundle();
        TransfersFragment fragment = new TransfersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfers, container, false);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(SeasonViewModel.class);
        setupVars(view);
        return view;
    }


    public static class CustLinearLayoutManager extends LinearLayoutManager {

        public CustLinearLayoutManager(Context context) {
            super(context);
        }

        public CustLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

// Something is happening here

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    void setupVars(View view) {
        //misc
        List<Integer> userPlayers = new ArrayList<>();
        LiveData<List<DreamTeam>> dreamTeam = seasonViewModel.getDreamTeam();
        //da se ne pokazuju igrači koje smo već kupili
        List<DreamTeam> currentDreamTeam = new ArrayList<>();
        dreamTeam.observe(getViewLifecycleOwner(), new Observer<List<DreamTeam>>() {
            @Override
            public void onChanged(List<DreamTeam> dreamTeams) {
                for (DreamTeam a : dreamTeams) {
                    userPlayers.addAll(a.getAllIds());
                    currentDreamTeam.add(a);
                }
                dreamTeam.removeObservers(getViewLifecycleOwner());
            }
        });

        balance = view.findViewById(R.id.txt_current_balance);
        String balanceS = getResources().getString(R.string.current_balance) + " <font color='#1b5e20'>" + String.format("%.2fM", Utils.getBalance(getContext()) / 1000000.0) + " $</font>";
        balance.setText(Html.fromHtml(balanceS), TextView.BufferType.SPANNABLE);

        //radio group - sell, buy
        sell = view.findViewById(R.id.radio_sell);
        buy = view.findViewById(R.id.radio_buy);
        buy.setChecked(true);
        radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_buy) {
                    buy.setChecked(true);
                    sell.setChecked(false);
                    USER_TEAM_FLAG = 1;
                } else {
                    USER_TEAM_FLAG = 0;
                    buy.setChecked(false);
                    sell.setChecked(true);
                }
            }
        });
        //spinner za timove
        spinnerTeams = view.findViewById(R.id.spinnr_team);
        LiveData<List<Team>> teamList = seasonViewModel.getAllTeams();
        List<String> teamNames = new ArrayList<>();
        teamNames.add("All");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinnerTeams.setAdapter(adapter);
        spinnerTeams.setOnItemSelectedListener(this);
        teamList.observe(getViewLifecycleOwner(), new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                for (Team a : teams) {
                    teamNames.add(a.getName());
                }
                adapter.notifyDataSetChanged();

            }
        });
        //spinner za pozicije
        spinnerPositions = view.findViewById(R.id.spinner_position);
        List<String> positions = Arrays.asList("All", "Goalkeeper", "Defender", "Midfielder", "Attacker");
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, positions);
        spinnerPositions.setAdapter(positionAdapter);
        spinnerPositions.setOnItemSelectedListener(this);

        //recycler view
        recyclerView = view.findViewById(R.id.recycler_transfers);

        //searchbutton
        imbSearch = view.findViewById(R.id.imb_search);
        imbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<List<Squads>> squads = seasonViewModel.getAllSquads();
                LiveData<List<Team>> teams = seasonViewModel.getAllTeams();
                List<Player> playerList = new ArrayList<>();

                //kupi
                if (USER_TEAM_FLAG == 1) {
                    //team flag and position flag set
                    if (!TEAM_FLAG.equals("") && !POSITION_FLAG.equals("")) {
                        LiveData<List<Player>> players = seasonViewModel.getPlayersTransferQuery(TEAM_FLAG, POSITION_FLAG);
                        players.observe(getViewLifecycleOwner(), players1 -> {
                            squads.observe(getViewLifecycleOwner(), squads1 -> {
                                teams.observe(getViewLifecycleOwner(), teams1 -> {
                                    for (Player a : players1) {
                                        a.setTeam(seasonViewModel.setPlayerTeam(a, squads1, teams1));
                                        a.getTeam().setTeamKit(seasonViewModel.setPlayerKit(a));
                                        if (!userPlayers.contains(a.getPlayerId())) {
                                            playerList.add(a);
                                        }
                                    }

                                    TransfersAdapter adapter = new TransfersAdapter(playerList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            Toast.makeText(getContext(), "" + player.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                });
                            });
                        });
                    }
                    //team flag set, position not
                    else if (!TEAM_FLAG.equals("") && POSITION_FLAG.equals("")) {
                        LiveData<List<Player>> players = seasonViewModel.transferQueryOnlyNameSet(TEAM_FLAG);
                        players.observe(getViewLifecycleOwner(), players1 -> {
                            squads.observe(getViewLifecycleOwner(), squads1 -> {
                                teams.observe(getViewLifecycleOwner(), teams1 -> {
                                    for (Player a : players1) {
                                        a.setTeam(seasonViewModel.setPlayerTeam(a, squads1, teams1));
                                        a.getTeam().setTeamKit(seasonViewModel.setPlayerKit(a));
                                        if (!userPlayers.contains(a.getPlayerId())) {
                                            playerList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(playerList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            Toast.makeText(getContext(), "" + player.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                });
                            });
                        });
                    }
                    //team flag not set, position set
                    else if (TEAM_FLAG.equals("") && !POSITION_FLAG.equals("")) {
                        LiveData<List<Player>> players = seasonViewModel.transferQueryOnlyPositionSet(POSITION_FLAG);
                        players.observe(getViewLifecycleOwner(), players1 -> {
                            squads.observe(getViewLifecycleOwner(), squads1 -> {
                                teams.observe(getViewLifecycleOwner(), teams1 -> {
                                    for (Player a : players1) {
                                        a.setTeam(seasonViewModel.setPlayerTeam(a, squads1, teams1));
                                        a.getTeam().setTeamKit(seasonViewModel.setPlayerKit(a));
                                        if (!userPlayers.contains(a.getPlayerId())) {
                                            playerList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(playerList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            Toast.makeText(getContext(), "" + player.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                });
                            });
                        });
                    }
                    //team flag not set position not set
                    else if (TEAM_FLAG.equals("") && POSITION_FLAG.equals("")) {
                        LiveData<List<Player>> players = seasonViewModel.getAllPlayers();
                        players.observe(getViewLifecycleOwner(), players1 -> {
                            squads.observe(getViewLifecycleOwner(), squads1 -> {
                                teams.observe(getViewLifecycleOwner(), teams1 -> {
                                    for (Player a : players1) {
                                        a.setTeam(seasonViewModel.setPlayerTeam(a, squads1, teams1));
                                        a.getTeam().setTeamKit(seasonViewModel.setPlayerKit(a));
                                        if (!userPlayers.contains(a.getPlayerId())) {
                                            playerList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(playerList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            Toast.makeText(getContext(), "" + player.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();

                                });
                            });
                        });
                    }
                }
                //prodaj
                else if (USER_TEAM_FLAG == 0) {
                    LiveData<List<Player>> players = seasonViewModel.getAllPlayers();
                    currentDreamTeam.clear();
                    dreamTeam.observe(getViewLifecycleOwner(), currentDreamTeam::addAll);
                    players.observe(getViewLifecycleOwner(), players1 -> {
                        squads.observe(getViewLifecycleOwner(), squads1 -> {
                            teams.observe(getViewLifecycleOwner(), teams1 -> {
                                List<Player> usersPlayers = new ArrayList<>();
                                usersPlayers = seasonViewModel.initialTeamSetup(currentDreamTeam.get(0), players1, squads1, teams1);
                                //team flag and position flag set
                                if (!TEAM_FLAG.equals("") && !POSITION_FLAG.equals("")) {
                                    List<Player> filteredList = new ArrayList<>();
                                    for (Player a : usersPlayers) {
                                        if (a.getTeam().getName().equals(TEAM_FLAG) && a.getPosition().equals(POSITION_FLAG)) {
                                            filteredList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(filteredList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            userPlayers.remove(player);
                                            Utils.putBalance(getContext(), Utils.getBalance(getContext()) + player.getPlayerValue());
                                            String balanceS = getResources().getString(R.string.current_balance) + " <font color='#1b5e20'>" + String.format("%.2fM", Utils.getBalance(getContext()) / 1000000.0) + " $</font>";
                                            balance.setText(Html.fromHtml(balanceS), TextView.BufferType.SPANNABLE);
                                            seasonViewModel.sellPlayer(player.getRealPosition());
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new CustLinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();

                                }
                                //team flag set, position not
                                else if (!TEAM_FLAG.equals("") && POSITION_FLAG.equals("")) {
                                    List<Player> filteredList = new ArrayList<>();
                                    for (Player a : usersPlayers) {
                                        if (a.getTeam().getName().equals(TEAM_FLAG)) {
                                            filteredList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(filteredList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            seasonViewModel.sellPlayer(player.getRealPosition());
                                            userPlayers.remove(player);
                                            Utils.putBalance(getContext(), Utils.getBalance(getContext()) + player.getPlayerValue());

                                            String balanceS = getResources().getString(R.string.current_balance) + " <font color='#1b5e20'>" + String.format("%.2fM", Utils.getBalance(getContext()) / 1000000.0) + " $</font>";
                                            balance.setText(Html.fromHtml(balanceS), TextView.BufferType.SPANNABLE);

                                            Toast.makeText(getContext(), "" + player.getName() + "REAL POS:" + player.getRealPosition(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new CustLinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                }
                                //team flag not set, position set
                                else if (TEAM_FLAG.equals("") && !POSITION_FLAG.equals("")) {
                                    List<Player> filteredList = new ArrayList<>();
                                    for (Player a : usersPlayers) {
                                        if (a.getPosition().equals(POSITION_FLAG)) {
                                            filteredList.add(a);
                                        }
                                    }
                                    TransfersAdapter adapter = new TransfersAdapter(filteredList, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            seasonViewModel.sellPlayer(player.getRealPosition());
                                            userPlayers.remove(player);
                                            Utils.putBalance(getContext(), Utils.getBalance(getContext()) + player.getPlayerValue());
                                            String balanceS = getResources().getString(R.string.current_balance) + " <font color='#1b5e20'>" + String.format("%.2fM", Utils.getBalance(getContext()) / 1000000.0) + " $</font>";
                                            balance.setText(Html.fromHtml(balanceS), TextView.BufferType.SPANNABLE);
                                            Toast.makeText(getContext(), "" + player.getName() + "REAL POS:" + player.getRealPosition(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new CustLinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                }
                                //team flag not set position not set
                                else if (TEAM_FLAG.equals("") && POSITION_FLAG.equals("")) {
                                    TransfersAdapter adapter = new TransfersAdapter(usersPlayers, new TransferListener() {
                                        @Override
                                        public void onPositionClicked(int position) {
                                            PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                                            Player player = playerSingleton.returnPlayer();
                                            seasonViewModel.sellPlayer(player.getRealPosition());
                                            userPlayers.remove(player);
                                            Utils.putBalance(getContext(), Utils.getBalance(getContext()) + player.getPlayerValue());
                                            String balanceS = getResources().getString(R.string.current_balance) + " <font color='#1b5e20'>" + String.format("%.2fM", Utils.getBalance(getContext()) / 1000000.0) + " $</font>";
                                            balance.setText(Html.fromHtml(balanceS), TextView.BufferType.SPANNABLE);
                                            Toast.makeText(getContext(), ""+ player.getName()+ "REAL POS:" + player.getRealPosition(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, USER_TEAM_FLAG);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new CustLinearLayoutManager(getContext()));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        });
                    });
                }
            }
        });
        imbSearch.performClick();
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
        //spinner za pozicije
        if (parent.getId() == R.id.spinner_position) {
            String selection = String.valueOf(parent.getItemAtPosition(position));
            if (selection.equals("All")) {
                POSITION_FLAG = "";
            } else {
                POSITION_FLAG = selection;
            }
        }
        //spinner za timove
        else if (parent.getId() == R.id.spinnr_team) {
            String selection = String.valueOf(parent.getItemAtPosition(position));
            if (selection.equals("All")) {
                TEAM_FLAG = "";
            } else {
                TEAM_FLAG = selection;
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

