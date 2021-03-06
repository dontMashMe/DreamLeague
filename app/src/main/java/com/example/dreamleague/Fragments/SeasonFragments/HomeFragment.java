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
    TextView txt_goalie_cap, txt_defenderLeft_cap, txt_defenderMidFirst_cap, txt_defenderMidSecond_cap, txt_defenderRight_cap, txt_midLeft_cap, txt_midMidFirst_cap, txt_midMidSecond_cap, txt_midRight_cap, txt_attackLeft_cap, txt_attackRight_cap;
    SeasonViewModel seasonViewModel;
    List<Player> currentUserTeam = new ArrayList<>();
    Button kickOff;
    List<Team> allTeamsWithPlayers = new ArrayList<>();
    List<Match> allMatches = new ArrayList<>();
    int captainId = 0;


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
                    if (allTeamsWithPlayers.isEmpty()) {
                        allTeamsWithPlayers.addAll(teams);
                    }
                    setupVars(view);
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
                        txt_team_name.setText(dreamTeams.get(dreamTeams.size() - 1).getName());
                        String text = getResources().getString(R.string.week) + " <font color='#6300ee'>" + Utils.getCurrentWeek(getContext()) + "</font>";
                        txt_current_week.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                        cleanText();
                        int captainId = Utils.getCaptainId(getContext());
                        currentUserTeam = seasonViewModel.initialTeamSetup(dreamTeams.get(dreamTeams.size() - 1), players, squads, teams);
                        for (Player a : currentUserTeam) {
                            switch (a.getRealPosition()) {
                                case 1:
                                    ((ImageButton) imb_goalie).setImageResource(a.getTeam().getTeamKit());
                                    imb_goalie.setTag("filled");
                                    txt_goalie_name.setText(seasonViewModel.formatName(a));
                                    txt_goalie_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_goalie_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_goalie_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_goalie_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 2:
                                    (imb_defenderLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderLeft.setTag("filled");
                                    txt_defenderLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderLeft_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_defenderLeft_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_defenderLeft_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 3:
                                    (imb_defenderMidFirst).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderMidFirst.setTag("filled");
                                    txt_defenderMidFirst_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderMidFirst_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderMidFirst_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_defenderMidFirst_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_defenderMidFirst_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 4:
                                    (imb_defenderMidSecond).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderMidSecond.setTag("filled");
                                    txt_defenderMidSecond_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderMidSecond_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderMidSecond_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_defenderMidSecond_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_defenderMidSecond_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 5:
                                    (imb_defenderRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_defenderRight.setTag("filled");
                                    txt_defenderRight_name.setText(seasonViewModel.formatName(a));
                                    txt_defenderRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_defenderRight_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_defenderRight_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_defenderRight_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 6:
                                    (imb_midLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_midLeft.setTag("filled");
                                    txt_midLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_midLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midLeft_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_midLeft_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_midLeft_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 7:
                                    (imb_midMidFirst).setImageResource(a.getTeam().getTeamKit());
                                    imb_midMidFirst.setTag("filled");
                                    txt_midMidFirst_name.setText(seasonViewModel.formatName(a));
                                    txt_midMidFirst_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midMidFirst_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_midMidFirst_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_midMidFirst_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 8:
                                    (imb_midMidSecond).setImageResource(a.getTeam().getTeamKit());
                                    imb_midMidSecond.setTag("filled");
                                    txt_midMidSecond_name.setText(seasonViewModel.formatName(a));
                                    txt_midMidSecond_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midMidSecond_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_midMidSecond_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_midMidSecond_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 9:
                                    (imb_midRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_midRight.setTag("filled");
                                    txt_midRight_name.setText(seasonViewModel.formatName(a));
                                    txt_midRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_midRight_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_midRight_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_midRight_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 10:
                                    (imb_attackLeft).setImageResource(a.getTeam().getTeamKit());
                                    imb_attackLeft.setTag("filled");
                                    txt_attackLeft_name.setText(seasonViewModel.formatName(a));
                                    txt_attackLeft_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_attackLeft_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_attackLeft_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_attackLeft_cap.setVisibility(View.INVISIBLE);
                                    }
                                    break;
                                case 11:
                                    (imb_attackRight).setImageResource(a.getTeam().getTeamKit());
                                    imb_attackRight.setTag("filled");
                                    txt_attackRight_name.setText(seasonViewModel.formatName(a));
                                    txt_attackRight_val.setText(String.format("%.2fM$", a.getPlayerValue() / 1000000.0));
                                    txt_attackRight_pr.setText(a.getPlayerRating());
                                    if (a.getPlayerId() == captainId) {
                                        txt_attackRight_cap.setVisibility(View.VISIBLE);
                                    } else {
                                        txt_attackRight_cap.setVisibility(View.INVISIBLE);
                                    }
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
        captainId = Utils.getCaptainId(getContext());
        return view;
    }



    //kul fadeout animacija
    //iz nekog razloga ako stavim theme activitya na NoActionBar, buttoni izgube material design
    //a s njim i pre-built animacije
    private final AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1f);
    List<GameResults> gameResults = new ArrayList<>();
    /**
     * Povuci sve utakmice za ovaj tjedan iz baze
     * metodi advanceWeek proslijedi utakmice i timove sa igračima
     * metoda izračuna rezultat i strijelce i vraća listu objekata GameResult
     * gameResult objekt se sastoji od id-eva oba tima, broja pogotka za svaki tim Map<Player, Integer> gdje je key igrač, a value broj pogodaka
     * loopaj kroz listu na off threadu, upiši MatchScores podatke (tablica za strijelce)
     * upiši broj ostvarenih bodova svakog igrača u tablicu PlayerPoints
     * upiši rezultate utakmica u tablicu Matches
     * upiši broj bodova za tim u tablicu Teams (bodovi 3 ili 1, za pobjedu i neriješeno)
     * pokreni activity PopWeekResults koja prikazuje broj ostvrenih bodova ovaj tjedan
     * ------------------------------------------------------
     * lineup singleton se koristi za slanje liste igrača PopWeekResults activitiyu koji prikazuje broj bodova
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
                //mapa koja sadrži broj bodova svakog igrača ostvarenog do ovog tjedna
                //služi za izračun bodova igrača
                HashMap<Integer, Integer> playerScoresOld = new HashMap<>();
                int captainId = Utils.getCaptainId(getActivity());
                executor.execute(() -> {
                    for (Player a : currentUserTeam) {
                        a.setPointsAcquired(seasonViewModel.getPlayerPointsInt(a.getPlayerId()));
                        playerScoresOld.put(a.getPlayerId(), a.getPointsAcquired());
                        lineupSingleton.AddPlayer(a);
                        if (a.getPlayerId() == captainId) a.setCaptain(true); //pronađi kapetana
                    }
                    for (GameResults a : gameResults) {
                        //strijelci doma
                        for (Map.Entry<Player, Integer> entry : a.getAwayTeamScorers().entrySet()) {
                            MatchScores matchScores = new MatchScores(0, a.getGameId(), entry.getKey().getPlayerId(), entry.getValue(), a.getTeamAwayId());
                            seasonViewModel.insertMatchScores(matchScores);
                        }
                        //strijelci gosti
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
                    int sum = seasonViewModel.getAllPointsSum();
                    //handler omogućava obaviti nešto na glavnom threadu iz off threada pomoću getMainLooper metode
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(()->{
                        String text = getResources().getString(R.string.points) + ": <font color='#6300ee'>" + sum + "</font>";
                        txt_current_points.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                    });
                });
                executor.shutdown();
                //ažuriraj tjedan
                Utils.putCurrentWeekSharedPreference(getContext(), currentWeek + 1);
                //ako je 20 tjedan, makni sve observere, aplikacija ulazi u novo stanje
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

        /*MISC*/
        kickOff = view.findViewById(R.id.btn_kickOff);
        kickOff.setOnClickListener(kickOffClick);
        txt_team_name = view.findViewById(R.id.txt_create_teamname);
        txt_current_week = view.findViewById(R.id.txt_current_week);
        txt_current_points = view.findViewById(R.id.txt_current_points);

        /*GOALIE*/
        imb_goalie = (ImageButton) view.findViewById(R.id.s_imb_goalie);
        imb_goalie.setTag("empty");
        txt_goalie_name = (TextView) view.findViewById(R.id.s_txt_goalie_name);
        txt_goalie_pr = (TextView) view.findViewById(R.id.s_txt_goalie_pr);
        txt_goalie_val = (TextView) view.findViewById(R.id.s_txt_goalie_val);
        txt_goalie_cap = (TextView) view.findViewById(R.id.txt_goalie_c);

        /*DEFENDER LEFT*/
        imb_defenderLeft = (ImageButton) view.findViewById(R.id.s_imb_defenderLeft);
        imb_defenderLeft.setTag("empty");
        txt_defenderLeft_name = view.findViewById(R.id.s_txt_defenderLeft_name);
        txt_defenderLeft_pr = view.findViewById(R.id.s_txt_defenderLeft_pr);
        txt_defenderLeft_val = view.findViewById(R.id.s_txt_defenderLeft_val);
        txt_defenderLeft_cap = view.findViewById(R.id.txt_defender_left_c);

        /*DEFENDER MID FIRST*/
        imb_defenderMidFirst = (ImageButton) view.findViewById(R.id.s_imb_defenderMidFirst);
        imb_defenderMidFirst.setTag("empty");
        txt_defenderMidFirst_name = view.findViewById(R.id.s_txt_defenderMidFirst_name);
        txt_defenderMidFirst_pr = view.findViewById(R.id.s_txt_defenderMidFirst_pr);
        txt_defenderMidFirst_val = view.findViewById(R.id.s_txt_defenderMidFirst_val);
        txt_defenderMidFirst_cap = view.findViewById(R.id.txt_defender_mid_first_c);

        /*DEFENDER MID SECOND*/
        imb_defenderMidSecond = (ImageButton) view.findViewById(R.id.s_imb_defenderMidSecond);
        imb_defenderMidSecond.setTag("empty");
        txt_defenderMidSecond_name = view.findViewById(R.id.s_txt_defenderMidSecond_name);
        txt_defenderMidSecond_pr = view.findViewById(R.id.s_txt_defenderMidSecond_pr);
        txt_defenderMidSecond_val = view.findViewById(R.id.s_txt_defenderMidSecond_val);
        txt_defenderMidSecond_cap = view.findViewById(R.id.txt_defender_mid_second_c);

        /*DEFENDER RIGHT*/
        imb_defenderRight = (ImageButton) view.findViewById(R.id.s_imb_defenderRight);
        imb_defenderRight.setTag("empty");
        txt_defenderRight_name = view.findViewById(R.id.s_txt_defenderRight_name);
        txt_defenderRight_pr = view.findViewById(R.id.s_txt_defenderRight_pr);
        txt_defenderRight_val = view.findViewById(R.id.s_txt_defenderRight_val);
        txt_defenderRight_cap = view.findViewById(R.id.txt_defender_right_c);

        /*MID LEFT*/
        imb_midLeft = (ImageButton) view.findViewById(R.id.s_imb_midLeft);
        imb_midLeft.setTag("empty");
        txt_midLeft_name = view.findViewById(R.id.s_txt_midLeft_name);
        txt_midLeft_pr = view.findViewById(R.id.s_txt_midLeft_pr);
        txt_midLeft_val = view.findViewById(R.id.s_txt_midLeft_val);
        txt_midLeft_cap = view.findViewById(R.id.txt_mid_left_c);

        /*MID MID FIRST*/
        imb_midMidFirst = (ImageButton) view.findViewById(R.id.s_imb_midMidFirst);
        imb_midMidFirst.setTag("empty");
        txt_midMidFirst_name = view.findViewById(R.id.s_txt_midMidFirst_name);
        txt_midMidFirst_pr = view.findViewById(R.id.s_txt_midMidFirst_pr);
        txt_midMidFirst_val = view.findViewById(R.id.s_txt_midMidFirst_val);
        txt_midMidFirst_cap = view.findViewById(R.id.txt_mid_mid_left_c);


        /*MID MID SECOND*/
        imb_midMidSecond = (ImageButton) view.findViewById(R.id.s_imb_midMidSecond);
        imb_midMidSecond.setTag("empty");
        txt_midMidSecond_name = view.findViewById(R.id.s_txt_midMidSecond_name);
        txt_midMidSecond_pr = view.findViewById(R.id.s_txt_midMidSecond_pr);
        txt_midMidSecond_val = view.findViewById(R.id.s_txt_midMidSecond_val);
        txt_midMidSecond_cap = view.findViewById(R.id.txt_mid_mid_right_c);

        /*MID RIGHT*/
        imb_midRight = (ImageButton) view.findViewById(R.id.s_imb_midRight);
        imb_midRight.setTag("empty");
        txt_midRight_name = view.findViewById(R.id.s_txt_midRight_name);
        txt_midRight_pr = view.findViewById(R.id.s_txt_midRight_pr);
        txt_midRight_val = view.findViewById(R.id.s_txt_midRight_val);
        txt_midRight_cap = view.findViewById(R.id.txt_mid_right_c);

        /*ATTACK LEFT*/
        imb_attackLeft = view.findViewById(R.id.s_imb_attackerLeft);
        imb_attackLeft.setTag("empty");
        txt_attackLeft_name = view.findViewById(R.id.s_txt_attackerLeft_name);
        txt_attackLeft_pr = view.findViewById(R.id.s_txt_attackerLeft_pr);
        txt_attackLeft_val = view.findViewById(R.id.s_txt_attackerLeft_val);
        txt_attackLeft_cap = view.findViewById(R.id.txt_attacker_left_c);

        /*ATTACK RIGHT*/
        imb_attackRight = view.findViewById(R.id.s_imb_attackerRight);
        imb_attackRight.setTag("empty");
        txt_attackRight_name = view.findViewById(R.id.s_txt_attackerRight_name);
        txt_attackRight_pr = view.findViewById(R.id.s_txt_attackerRight_pr);
        txt_attackRight_val = view.findViewById(R.id.s_txt_attackerRight_val);
        txt_attackRight_cap = view.findViewById(R.id.txt_attacker_right_c);

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

    void updateCaptain() {
        int captainId = Utils.getCaptainId(getContext());
        txt_goalie_cap.setVisibility(View.INVISIBLE);
        txt_defenderLeft_cap.setVisibility(View.INVISIBLE);
        txt_defenderMidFirst_cap.setVisibility(View.INVISIBLE);
        txt_defenderMidSecond_cap.setVisibility(View.INVISIBLE);
        txt_defenderRight_cap.setVisibility(View.INVISIBLE);
        txt_midLeft_cap.setVisibility(View.INVISIBLE);
        txt_midMidFirst_cap.setVisibility(View.INVISIBLE);
        txt_midMidSecond_cap.setVisibility(View.INVISIBLE);
        txt_midRight_cap.setVisibility(View.INVISIBLE);
        txt_attackLeft_cap.setVisibility(View.INVISIBLE);
        txt_attackRight_cap.setVisibility(View.INVISIBLE);
        for (Player a : currentUserTeam) {
            if (captainId == a.getPlayerId()) {
                switch (a.getRealPosition()) {
                    case 1:
                        txt_goalie_cap.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        txt_defenderLeft_cap.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        txt_defenderMidFirst_cap.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        txt_defenderMidSecond_cap.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        txt_defenderRight_cap.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        txt_midLeft_cap.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        txt_midMidFirst_cap.setVisibility(View.VISIBLE);
                        break;
                    case 8:
                        txt_midMidSecond_cap.setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        txt_midRight_cap.setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        txt_attackLeft_cap.setVisibility(View.VISIBLE);
                        break;
                    case 11:
                        txt_attackRight_cap.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(captainId != Utils.getCaptainId(getContext())){
            updateCaptain();
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        captainId = Utils.getCaptainId(getContext());

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
