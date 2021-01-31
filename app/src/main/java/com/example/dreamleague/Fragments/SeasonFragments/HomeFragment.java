package com.example.dreamleague.Fragments.SeasonFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamleague.Activities.PopPlayerInfo;
import com.example.dreamleague.Activities.PopWeekResults;
import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.GameResults;
import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerSingleton;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.SeasonViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    ImageButton imb_goalie, imb_defenderLeft, imb_defenderMidFirst, imb_defenderMidSecond, imb_defenderRight, imb_midLeft, imb_midMidFirst, imb_midMidSecond, imb_midRight, imb_attackLeft, imb_attackRight;
    TextView txt_goalie_name, txt_defenderLeft_name, txt_defenderMidFirst_name, txt_defenderMidSecond_name, txt_defenderRight_name, txt_midLeft_name, txt_midMidFirst_name, txt_midMidSecond_name, txt_midRight_name, txt_attackLeft_name, txt_attackRight_name;
    TextView txt_goalie_pr, txt_defenderLeft_pr, txt_defenderMidFirst_pr, txt_defenderMidSecond_pr, txt_defenderRight_pr, txt_midLeft_pr, txt_midMidFirst_pr, txt_midMidSecond_pr, txt_midRight_pr, txt_attackLeft_pr, txt_attackRight_pr;
    TextView txt_goalie_val, txt_defenderLeft_val, txt_defenderMidFirst_val, txt_defenderMidSecond_val, txt_defenderRight_val, txt_midLeft_val, txt_midMidFirst_val, txt_midMidSecond_val, txt_midRight_val, txt_attackLeft_val, txt_attackRight_val;
    TextView txt_team_name, txt_current_points, txt_current_week;
    SeasonViewModel seasonViewModel;
    List<Player> currentUserTeam = new ArrayList<>();
    Button kickOff;
    List<Team> allTeamsWithPlayers = new ArrayList<>();

    List<Match> allMatches = new ArrayList<>();

    @NonNull
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    LiveData<List<Match>> matchesLiveData;
    LiveData<List<DreamTeam>> dreamTeamLiveData;
    LiveData<List<Player>> playerLiveData;
    LiveData<List<Squads>> squadsLiveData;
    LiveData<List<Team>> teamLiveData;

    private void runSetup(View view) {
        if (Utils.getCurrentWeek(getContext()) < 20) {
            matchesLiveData = seasonViewModel.getAllMatches();
            dreamTeamLiveData = seasonViewModel.getDreamTeam();
            playerLiveData = seasonViewModel.getAllPlayers();
            squadsLiveData = seasonViewModel.getAllSquads();
            teamLiveData = seasonViewModel.getAllTeams();
            dreamTeamLiveData.observe(getViewLifecycleOwner(), dreamTeams -> playerLiveData.observe(getViewLifecycleOwner(), players -> squadsLiveData.observe(getViewLifecycleOwner(), squads -> teamLiveData.observe(getViewLifecycleOwner(), teams -> {
                matchesLiveData.observe(getViewLifecycleOwner(), matches -> {
                    if (allMatches.isEmpty()) {
                        allMatches.addAll(matches);
                    }
                });
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    int sum = seasonViewModel.getAllPointsSum();
                    //off
                    if (allTeamsWithPlayers.isEmpty()) {
                        allTeamsWithPlayers.addAll(teams);
                    }
                    List<Player> userPlayers;
                    userPlayers = seasonViewModel.initialTeamSetup(dreamTeams.get(dreamTeams.size() - 1), players, squads, teams);
                    setupVars(view);
                    currentUserTeam.addAll(userPlayers);
                    setupClickListeners();
                    executor.shutdown();
                    handler.post(() -> {
                        for (Team a : allTeamsWithPlayers) {
                            LiveData<List<Player>> playersFromTeam = seasonViewModel.getPlayersFromTeam(a.getTeam_id());
                            playersFromTeam.observe(getViewLifecycleOwner(), teamPlayers -> {
                                a.getPlayerList().clear();
                                a.getPlayerList().addAll(teamPlayers);
                            });
                        }
                        //textviews
                        txt_team_name.setText(dreamTeams.get(dreamTeams.size() - 1).getName());
                        String text = getResources().getString(R.string.week) + " <font color='#6300ee'>" + Utils.getCurrentWeek(getContext()) + "</font>";
                        txt_current_week.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);


                        //postavljanje igrača na pozicije
                        //izgleda ružno ali switch je brza naredba i izvršava se skoro instantno
                        cleanText();
                        for (Player a : userPlayers) {
                            switch (a.getRealPosition()) {
                                case 1:
                                    ((ImageButton) imb_goalie).setImageResource(a.getTeam().getTeamKit());
                                    imb_goalie.setTag("filled");
                                    txt_goalie_name.setText(seasonViewModel.formatName(a));
                                    txt_goalie_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_goalie_pr.setText(a.getPlayerRating());
                                    imageButtons.remove(imb_goalie);
                                    break;
                                case 2:
                                    (imb_defenderLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderLeft.setTag("filled");
                                    txt_defenderLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderLeft_pr.setText(a.getPlayerRating());
                                    break;
                                case 3:
                                    (imb_defenderMidFirst).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderMidFirst.setTag("filled");
                                    txt_defenderMidFirst_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderMidFirst_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderMidFirst_pr.setText(a.getPlayerRating());
                                    break;
                                case 4:
                                    (imb_defenderMidSecond).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderMidSecond.setTag("filled");
                                    txt_defenderMidSecond_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderMidSecond_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderMidSecond_pr.setText(a.getPlayerRating());
                                    break;
                                case 5:
                                    (imb_defenderRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderRight.setTag("filled");
                                    txt_defenderRight_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderRight_pr.setText(a.getPlayerRating());
                                    break;
                                case 6:
                                    (imb_midLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_midLeft.setTag("filled");
                                    txt_midLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_midLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midLeft_pr.setText(a.getPlayerRating());
                                    break;
                                case 7:
                                    (imb_midMidFirst).setImageResource(a.getTeam().getTeamKit());
                                    imb_midMidFirst.setTag("filled");
                                    txt_midMidFirst_name.setText(seasonViewModel.formatName(a));
                                    txt_midMidFirst_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midMidFirst_pr.setText(a.getPlayerRating());
                                    break;
                                case 8:
                                    (imb_midMidSecond).setImageResource(a.getTeam().getTeamKit());
                                    imb_midMidSecond.setTag("filled");
                                    txt_midMidSecond_name.setText(seasonViewModel.formatName(a));
                                    txt_midMidSecond_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midMidSecond_pr.setText(a.getPlayerRating());
                                    break;
                                case 9:
                                    (imb_midRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_midRight.setTag("filled");
                                    txt_midRight_name.setText(seasonViewModel.formatName(a));
                                    txt_midRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midRight_pr.setText(a.getPlayerRating());

                                    break;
                                case 10:
                                    (imb_attackLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_attackLeft.setTag("filled");
                                    txt_attackLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_attackLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_attackLeft_pr.setText(a.getPlayerRating());

                                    break;
                                case 11:
                                    (imb_attackRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_attackRight.setTag("filled");
                                    txt_attackRight_name.setText(seasonViewModel.formatName(a));
                                    txt_attackRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_attackRight_pr.setText(a.getPlayerRating());
                                    break;
                            }
                        }
                        text = getResources().getString(R.string.points) + ": <font color='#6300ee'>" + sum + "</font>";
                        txt_current_points.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                        for (ImageButton a : imageButtons) {
                            if (a.getTag().equals("empty")) {
                                a.setImageResource(R.drawable.default_kit2);
                            }
                        }
                        imageButtons.clear();
                        playerLiveData.removeObservers(getViewLifecycleOwner());
                        teamLiveData.removeObservers(getViewLifecycleOwner());
                        squadsLiveData.removeObservers(getViewLifecycleOwner());
                    });
                });

            }))));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        seasonViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(SeasonViewModel.class);
        runSetup(view);
        return view;
    }


    ;
    //kul fadeout animacija
    //iz nekog razloga ako stavim theme activitya na NoActionBar, buttoni izgube material design
    //a s njim i pre-built animacije
    private final AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
    List<GameResults> gameResults = new ArrayList<>();
    /**
     * Sve je statički
     * Problem ako updateam bazu u observeru je to što je LiveData ... LiveData
     * Konstantno čita iz baze i zapne u beskonačnom loopu updateanja istih records jer LiveData konstantno vuče iz baze
     * morao sam koristit statičku listu matcheva, statičku listu igrača, timova, sve
     * app je užasno spor na prvom loadu, ali dalje radi okej jer je sva cache-d
     */
    View.OnClickListener kickOffClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alphaAnimation.setDuration(500);
            v.setAlpha(1f);
            v.startAnimation(alphaAnimation);
            int currentWeek = Utils.getCurrentWeek(getContext());
            if (allMatches.isEmpty()) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            } else {
                List<Match> currentWeekMatches = seasonViewModel.getMatchesFromWeekStatic(allMatches, currentWeek);
                gameResults.clear();
                gameResults.addAll(seasonViewModel.advanceWeek(currentWeekMatches, allTeamsWithPlayers));
                LineupSingleton lineupSingleton = LineupSingleton.getInstance();
                lineupSingleton.ReturnList().clear();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                HashMap<Integer, Integer> playerScoresOld = new HashMap<>();
                executor.execute(() -> {
                    for (Player a : currentUserTeam) {
                        a.setPointsAcquired(seasonViewModel.getPlayerPointsInt(a.getPlayerId()));
                        playerScoresOld.put(a.getPlayerId(), a.getPointsAcquired());
                        lineupSingleton.AddPlayer(a);
                    }
                    for (GameResults a : gameResults) {
                        for (Map.Entry<Player, Integer> entry : a.getAwayTeamScorers().entrySet()) {
                            MatchScores matchScores = new MatchScores(0, a.getGameId(), entry.getKey().getPlayerId(), entry.getValue(), a.getTeamAwayId());
                            seasonViewModel.insertMatchScores(matchScores);
                        }
                        for (Map.Entry<Player, Integer> entry : a.getHomeTeamScorers().entrySet()) {
                            MatchScores matchScores = new MatchScores(0, a.getGameId(), entry.getKey().getPlayerId(), entry.getValue(), a.getHomeTeamId());
                            seasonViewModel.insertMatchScores(matchScores);
                        }
                        Match playedMatch = new Match(a.getGameId(), currentWeek, a.getHomeTeamId(), a.getTeamAwayId(), a.getHomeTeamScore(), a.getAwayTeamScore());
                        seasonViewModel.updatePlayerPoints(currentUserTeam, playedMatch);
                        seasonViewModel.updateMatchesWhere(a.getGameId(), a.getHomeTeamScore(), a.getAwayTeamScore());
                        seasonViewModel.updatePoints(a); //team points
                    }
                    Intent intent = new Intent(requireActivity(), PopWeekResults.class);
                    intent.putExtra("map", playerScoresOld);
                    startActivity(intent);

                });
                executor.shutdown();
                Utils.putCurrentWeekSharedPreference(getContext(), currentWeek + 1);
                if (Utils.getCurrentWeek(getContext()) == 20) {
                    dreamTeamLiveData.removeObservers(getViewLifecycleOwner());
                    matchesLiveData.removeObservers(getViewLifecycleOwner());
                    playerLiveData.removeObservers(getViewLifecycleOwner());
                    teamLiveData.removeObservers(getViewLifecycleOwner());
                    squadsLiveData.removeObservers(getViewLifecycleOwner());
                }
                String text = getResources().getString(R.string.week) + " <font color='#6300ee'>" + Utils.getCurrentWeek(getContext()) + "</font>";
                txt_current_week.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            }

        }
    };


    void cleanText() {
        txt_goalie_name.setText("");
        txt_goalie_pr.setText("");
        txt_goalie_val.setText("");

        txt_defenderLeft_name.setText("");
        txt_defenderLeft_pr.setText("");
        txt_defenderLeft_val.setText("");

        txt_defenderMidFirst_name.setText("");
        txt_defenderMidFirst_pr.setText("");
        txt_defenderMidFirst_val.setText("");

        txt_defenderMidSecond_name.setText("");
        txt_defenderMidSecond_pr.setText("");
        txt_defenderMidSecond_val.setText("");

        txt_defenderRight_name.setText("");
        txt_defenderRight_pr.setText("");
        txt_defenderRight_val.setText("");

        txt_midLeft_name.setText("");
        txt_midLeft_pr.setText("");
        txt_midLeft_val.setText("");


        txt_midMidFirst_name.setText("");
        txt_midMidFirst_pr.setText("");
        txt_midMidFirst_val.setText("");

        txt_midMidSecond_name.setText("");
        txt_midMidSecond_pr.setText("");
        txt_midMidSecond_val.setText("");

        txt_midRight_name.setText("");
        txt_midRight_pr.setText("");
        txt_midRight_val.setText("");

        txt_attackLeft_name.setText("");
        txt_attackLeft_pr.setText("");
        txt_attackLeft_val.setText("");

        txt_attackRight_name.setText("");
        txt_attackRight_pr.setText("");
        txt_attackRight_val.setText("");

    }

    void setupVars(View view) {
        kickOff = view.findViewById(R.id.btn_kickOff);
        kickOff.setOnClickListener(kickOffClick);

        txt_team_name = view.findViewById(R.id.txt_create_teamname);
        txt_current_week = view.findViewById(R.id.txt_current_week);
        txt_current_points = view.findViewById(R.id.txt_current_points);


        imb_goalie = (ImageButton) view.findViewById(R.id.s_imb_goalie);
        imb_goalie.setTag("empty");

        txt_goalie_name = (TextView) view.findViewById(R.id.s_txt_goalie_name);
        txt_goalie_pr = (TextView) view.findViewById(R.id.s_txt_goalie_pr);
        txt_goalie_val = (TextView) view.findViewById(R.id.s_txt_goalie_val);

        imb_defenderLeft = (ImageButton) view.findViewById(R.id.s_imb_defenderLeft);
        imb_defenderLeft.setTag("empty");
        txt_defenderLeft_name = view.findViewById(R.id.s_txt_defenderLeft_name);
        txt_defenderLeft_pr = view.findViewById(R.id.s_txt_defenderLeft_pr);
        txt_defenderLeft_val = view.findViewById(R.id.s_txt_defenderLeft_val);

        imb_defenderMidFirst = (ImageButton) view.findViewById(R.id.s_imb_defenderMidFirst);
        imb_defenderMidFirst.setTag("empty");
        txt_defenderMidFirst_name = view.findViewById(R.id.s_txt_defenderMidFirst_name);
        txt_defenderMidFirst_pr = view.findViewById(R.id.s_txt_defenderMidFirst_pr);
        txt_defenderMidFirst_val = view.findViewById(R.id.s_txt_defenderMidFirst_val);

        imb_defenderMidSecond = (ImageButton) view.findViewById(R.id.s_imb_defenderMidSecond);
        imb_defenderMidSecond.setTag("empty");

        txt_defenderMidSecond_name = view.findViewById(R.id.s_txt_defenderMidSecond_name);
        txt_defenderMidSecond_pr = view.findViewById(R.id.s_txt_defenderMidSecond_pr);
        txt_defenderMidSecond_val = view.findViewById(R.id.s_txt_defenderMidSecond_val);

        imb_defenderRight = (ImageButton) view.findViewById(R.id.s_imb_defenderRight);
        imb_defenderRight.setTag("empty");

        txt_defenderRight_name = view.findViewById(R.id.s_txt_defenderRight_name);
        txt_defenderRight_pr = view.findViewById(R.id.s_txt_defenderRight_pr);
        txt_defenderRight_val = view.findViewById(R.id.s_txt_defenderRight_val);

        imb_midLeft = (ImageButton) view.findViewById(R.id.s_imb_midLeft);
        imb_midLeft.setTag("empty");

        txt_midLeft_name = view.findViewById(R.id.s_txt_midLeft_name);
        txt_midLeft_pr = view.findViewById(R.id.s_txt_midLeft_pr);
        txt_midLeft_val = view.findViewById(R.id.s_txt_midLeft_val);

        imb_midMidFirst = (ImageButton) view.findViewById(R.id.s_imb_midMidFirst);
        imb_midMidFirst.setTag("empty");

        txt_midMidFirst_name = view.findViewById(R.id.s_txt_midMidFirst_name);
        txt_midMidFirst_pr = view.findViewById(R.id.s_txt_midMidFirst_pr);
        txt_midMidFirst_val = view.findViewById(R.id.s_txt_midMidFirst_val);

        imb_midMidSecond = (ImageButton) view.findViewById(R.id.s_imb_midMidSecond);
        imb_midMidSecond.setTag("empty");

        txt_midMidSecond_name = view.findViewById(R.id.s_txt_midMidSecond_name);
        txt_midMidSecond_pr = view.findViewById(R.id.s_txt_midMidSecond_pr);
        txt_midMidSecond_val = view.findViewById(R.id.s_txt_midMidSecond_val);

        imb_midRight = (ImageButton) view.findViewById(R.id.s_imb_midRight);
        imb_midRight.setTag("empty");

        txt_midRight_name = view.findViewById(R.id.s_txt_midRight_name);
        txt_midRight_pr = view.findViewById(R.id.s_txt_midRight_pr);
        txt_midRight_val = view.findViewById(R.id.s_txt_midRight_val);

        imb_attackLeft = view.findViewById(R.id.s_imb_attackerLeft);
        imb_attackLeft.setTag("empty");

        txt_attackLeft_name = view.findViewById(R.id.s_txt_attackerLeft_name);
        txt_attackLeft_pr = view.findViewById(R.id.s_txt_attackerLeft_pr);
        txt_attackLeft_val = view.findViewById(R.id.s_txt_attackerLeft_val);

        imb_attackRight = view.findViewById(R.id.s_imb_attackerRight);
        imb_attackRight.setTag("empty");

        txt_attackRight_name = view.findViewById(R.id.s_txt_attackerRight_name);
        txt_attackRight_pr = view.findViewById(R.id.s_txt_attackerRight_pr);
        txt_attackRight_val = view.findViewById(R.id.s_txt_attackerRight_val);

        currentUserTeam = new ArrayList<>();
    }

    //služi samo kak bi na sve imagebuttone zakačio onClickListene
    //nakon što je zakačen, array ispraznim.
    List<ImageButton> imageButtons = new ArrayList<>();

    /**
     * mapira imagebutton id i realPos
     * služi kak bi znali koji igrač je kliknut, izbjegava statičku klasu kao kod kreacije (ne zaboravi promijenit to), kao i lastRememberdPosition i ostalo
     *
     * @param ImageButton id, realPos id
     */
    Map<Integer, Integer> buttonPositionMap = new HashMap<>();

    void setupClickListeners() {
        imageButtons.add(imb_goalie);
        buttonPositionMap.put(imb_goalie.getId(), 1);

        imageButtons.add(imb_defenderLeft);
        buttonPositionMap.put(imb_defenderLeft.getId(), 2);

        imageButtons.add(imb_defenderMidFirst);
        buttonPositionMap.put(imb_defenderMidFirst.getId(), 3);

        imageButtons.add(imb_defenderMidSecond);
        buttonPositionMap.put(imb_defenderMidSecond.getId(), 4);

        imageButtons.add(imb_defenderRight);
        buttonPositionMap.put(imb_defenderRight.getId(), 5);

        imageButtons.add(imb_midLeft);
        buttonPositionMap.put(imb_midLeft.getId(), 6);

        imageButtons.add(imb_midMidFirst);
        buttonPositionMap.put(imb_midMidFirst.getId(), 7);

        imageButtons.add(imb_midMidSecond);
        buttonPositionMap.put(imb_midMidSecond.getId(), 8);

        imageButtons.add(imb_midRight);
        buttonPositionMap.put(imb_midRight.getId(), 9);

        imageButtons.add(imb_attackLeft);
        buttonPositionMap.put(imb_attackLeft.getId(), 10);

        imageButtons.add(imb_attackRight);
        buttonPositionMap.put(imb_attackRight.getId(), 11);

        for (ImageButton a : imageButtons) {
            a.setOnClickListener(playerClickListener);
        }

    }

    private final View.OnClickListener playerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (Map.Entry<Integer, Integer> entry : buttonPositionMap.entrySet()) {
                if (v.getId() == entry.getKey()) {
                    ImageButton clicked = v.findViewById(v.getId());
                    if (clicked.getTag().equals("filled")) {
                        PlayerSingleton playerSingleton = PlayerSingleton.getInstance();
                        playerSingleton.SetPlayer(seasonViewModel.getPlayerFromRealPos(entry.getValue(), currentUserTeam));
                        startActivity(new Intent(requireActivity(), PopPlayerInfo.class));
                    }
                }
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
