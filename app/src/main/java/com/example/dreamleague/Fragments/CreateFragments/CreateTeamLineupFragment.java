package com.example.dreamleague.Fragments.CreateFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.dreamleague.Activities.CreateTeamFragmentHolderActivity;
import com.example.dreamleague.Activities.SeasonActivity;
import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.Listeners.PositionListener;
import com.example.dreamleague.R;
import com.example.dreamleague.ViewModels.CreateTeamViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTeamLineupFragment extends Fragment {

    /********************* BOILER PLATE *****************/
    //image buttons igraca
    ImageButton goalie, defender1,defender2,defender3,defender4, midfielder1,  midfielder2,  midfielder3,  midfielder4, attacker1, attacker2;
    //imena igraca
    TextView txt_goalie, txt_defender1, txt_defender2, txt_defender3, txt_defender4, txt_midfield1, txt_midfield2, txt_midfield3,txt_midfield4, txt_attack1, txt_attack2;
    //ostalo
    TextView lastRememberedPosition, txt_balance;
    //player scorevi
    TextView txt_goalie_playerScore, txt_defender1_playerScore, txt_defender2_playerScore, txt_defender3_playerScore, txt_defender4_playerScore, txt_midfielder1_playerScore,txt_midfielder2_playerScore,txt_midfielder3_playerScore,txt_midfielder4_playerScore, txt_attacker1_playerScore, txt_attacker2_playerScore;
    //vrijednost igraca
    TextView txt_goalie_value, txt_defender1_value,txt_defender2_value,txt_defender3_value,txt_defender4_value, txt_midfielder1_value,txt_midfielder2_value,txt_midfielder3_value,txt_midfielder4_value, txt_attacker1_value, txt_attacker2_value;
    ArrayList<PlayerViews> playerViews;
    Button btn_complete;
    ImageButton btn_autoFill;
    /********************* BOILER PLATE *****************/


    String teamName = "";
    int userBalance = 90000000;
    Map<ImageButton, TextView> playerPositions = new HashMap<>(); //older solution, TO-DO kasnije -> obavi sve u klasi PlayerViewsm, |mozda ipak ne|
    CreateTeamViewModel createTeamViewModel;
    //dummy objekt, problem je jer ukoliko ga ne inicijaliziram kada god pokusam setat pozicije baca null object reference error
    DreamTeam dreamTeam = new DreamTeam(0, "-", 0,0,0,0,0,0,0,0,0,0,0);
    //klasa koja služi za grupiranje view-ova u logičku cjelinu, privremeno
    //todo->preko realPosition
    static class PlayerViews {
        private Player player;
        private TextView position;
        private TextView playerValue;
        private TextView playerRating;
        private ImageButton playerKit;
        private final int realPosition;


        public PlayerViews(TextView position, TextView pv, TextView pr, int realPosition, ImageButton imageButton){
            this.position =position;
            this.playerValue = pv;
            this.playerRating = pr;
            this.realPosition = realPosition;
            this.playerKit = imageButton;
        }

        public ImageButton getPlayerKit() {
            return playerKit;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public TextView getPosition() {
            return position;
        }

        public void setPosition(TextView position) {
            this.position = position;
        }

        public TextView getPlayerValue() {
            return playerValue;
        }

        public TextView getPlayerRating() {
            return playerRating;
        }

        public int getRealPosition() {
            return realPosition;
        }
    }

    public PositionListener positionListener;
    @NonNull
    public static CreateTeamLineupFragment newInstance(){
        Bundle args = new Bundle();
        CreateTeamLineupFragment fragment = new CreateTeamLineupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_team_lineup, container,false);
        assert getArguments() != null;
        teamName = getArguments().getString("TEAM_NAME");
        setupViews(view);
        setupClickListeners();
        createTeamViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()))
                .get(CreateTeamViewModel.class);

        return view;

    }


    void setupViews(View v){
        btn_complete = (Button)v.findViewById(R.id.btn_startSeason);

        playerViews = new ArrayList<>();
        txt_balance = (TextView)v.findViewById(R.id.txt_balance);
        txt_balance.setText(String.format("Konto: %.2fM$", userBalance /1000000.0));
        goalie = (ImageButton) v.findViewById(R.id.imb_goalie);
        defender1= (ImageButton)v.findViewById(R.id.imb_defend1);
        defender2= (ImageButton)v.findViewById(R.id.imb_defend2);
        defender3= (ImageButton)v.findViewById(R.id.imb_defend3);
        defender4= (ImageButton)v.findViewById(R.id.imb_defend4);
        //midfielders
        midfielder1=(ImageButton) v.findViewById(R.id.imb_midfield1);
        midfielder2= (ImageButton)v.findViewById(R.id.imb_midfield2);
        midfielder3=(ImageButton) v.findViewById(R.id.imb_midfield3);
        midfielder4= (ImageButton)v.findViewById(R.id.imb_midfield4);
        //attackers
        attacker1= (ImageButton)v.findViewById(R.id.imb_attack1);
        attacker2= (ImageButton)v.findViewById(R.id.imb_attack2);

        btn_autoFill = v.findViewById(R.id.imb_autoFill);

        //goalie and defenders
        txt_goalie = v.findViewById(R.id.txt_goalie_name);
        txt_goalie_playerScore= v.findViewById(R.id.txt_goalie_pr);
        txt_goalie_value = v.findViewById(R.id.txt_goalie_val);
        PlayerViews p0 = new PlayerViews(txt_goalie, txt_goalie_value, txt_goalie_playerScore, 1, goalie);

        playerViews.add(p0);
        txt_defender1 = (TextView)v.findViewById(R.id.txt_defender1_name);
        txt_defender1_playerScore = (TextView)v.findViewById(R.id.txt_defender1_pr);
        txt_defender1_value = (TextView)v.findViewById(R.id.txt_defender1_val);
        PlayerViews p1 = new PlayerViews(txt_defender1, txt_defender1_value, txt_defender1_playerScore, 2, defender1);
        playerViews.add(p1);

        txt_defender2 = (TextView)v.findViewById(R.id.txt_defender2_name);
        txt_defender2_playerScore = (TextView)v.findViewById(R.id.txt_defender2_pr);
        txt_defender2_value = (TextView)v.findViewById(R.id.txt_defender2_val);
        PlayerViews p2 = new PlayerViews(txt_defender2, txt_defender2_value, txt_defender2_playerScore, 3, defender2);
        playerViews.add(p2);

        txt_defender3 = (TextView)v.findViewById(R.id.txt_defender3_name);
        txt_defender3_playerScore = (TextView)v.findViewById(R.id.txt_defender3_pr);
        txt_defender3_value = (TextView)v.findViewById(R.id.txt_defender3_val);
        PlayerViews p3 = new PlayerViews(txt_defender3, txt_defender3_value, txt_defender3_playerScore,4 , defender3);
        playerViews.add(p3);

        txt_defender4 = (TextView)v.findViewById(R.id.txt_defender4_name);
        txt_defender4_playerScore = (TextView)v.findViewById(R.id.txt_defender4_pr);
        txt_defender4_value = (TextView)v.findViewById(R.id.txt_defender4_val);
        PlayerViews p4 = new PlayerViews(txt_defender4, txt_defender4_value, txt_defender4_playerScore, 5, defender4);
        playerViews.add(p4);

        //MIDFIELD
        txt_midfield1 = (TextView)v.findViewById(R.id.txt_midfielder1_name);
        txt_midfielder1_playerScore= (TextView)v.findViewById(R.id.txt_midfielder1_pr);
        txt_midfielder1_value = (TextView)v.findViewById(R.id.txt_midfielder1_val);
        PlayerViews p5 = new PlayerViews(txt_midfield1, txt_midfielder1_value,  txt_midfielder1_playerScore, 6, midfielder1);
        playerViews.add(p5);

        txt_midfield2 = (TextView)v.findViewById(R.id.txt_midfielder2_name);
        txt_midfielder2_playerScore= (TextView)v.findViewById(R.id.txt_midfielder2_pr);
        txt_midfielder2_value = (TextView)v.findViewById(R.id.txt_midfielder2_val);
        PlayerViews p6 = new PlayerViews(txt_midfield2, txt_midfielder2_value, txt_midfielder2_playerScore, 7, midfielder2);
        playerViews.add(p6);

        txt_midfield3 = (TextView)v.findViewById(R.id.txt_midfielder3_name);
        txt_midfielder3_playerScore= (TextView)v.findViewById(R.id.txt_midfielder3_pr);
        txt_midfielder3_value = (TextView)v.findViewById(R.id.txt_midfielder3_val);
        PlayerViews p7 = new PlayerViews(txt_midfield3, txt_midfielder3_value, txt_midfielder3_playerScore, 8, midfielder3);
        playerViews.add(p7);

        txt_midfield4 = (TextView)v.findViewById(R.id.txt_midfielder4_name);
        txt_midfielder4_playerScore= (TextView)v.findViewById(R.id.txt_midfielder4_pr);
        txt_midfielder4_value = (TextView)v.findViewById(R.id.txt_midfielder4_val);
        PlayerViews p8 = new PlayerViews(txt_midfield4, txt_midfielder4_value, txt_midfielder4_playerScore, 9, midfielder4);
        playerViews.add(p8);

        //attackers
        txt_attack1 = (TextView)v.findViewById(R.id.txt_attacker1_name);
        txt_attacker1_playerScore = (TextView)v.findViewById(R.id.txt_attacker1_pr);
        txt_attacker1_value = (TextView)v.findViewById(R.id.txt_attacker1_val);
        PlayerViews p9 = new PlayerViews(txt_attack1, txt_attacker1_value, txt_attacker1_playerScore, 10, attacker1);
        playerViews.add(p9);

        txt_attack2 = (TextView)v.findViewById(R.id.txt_attacker2_name);
        txt_attacker2_playerScore = (TextView)v.findViewById(R.id.txt_attacker2_pr);
        txt_attacker2_value = (TextView)v.findViewById(R.id.txt_attacker2_val);
        PlayerViews p10 = new PlayerViews(txt_attack2, txt_attacker2_value, txt_attacker2_playerScore, 11, attacker2);
        playerViews.add(p10);




    }

    void setupClickListeners(){
        ArrayList<ImageButton> defenders = new ArrayList<>();
        ArrayList<ImageButton> midfielders = new ArrayList<>();
        ArrayList<ImageButton> attackers = new ArrayList<>();

        playerPositions.put(goalie, txt_goalie);
        defenders.add(defender1);
        playerPositions.put(defender1, txt_defender1);
        defenders.add(defender2);
        playerPositions.put(defender2, txt_defender2);
        defenders.add(defender3);
        playerPositions.put(defender3, txt_defender3);
        defenders.add(defender4);
        playerPositions.put(defender4, txt_defender4);

        //midfielders
        midfielders.add(midfielder1);
        playerPositions.put(midfielder1,txt_midfield1 );
        midfielders.add(midfielder2);
        playerPositions.put(midfielder2,txt_midfield2 );
        midfielders.add(midfielder3);
        playerPositions.put(midfielder3,txt_midfield3 );
        midfielders.add(midfielder4);
        playerPositions.put(midfielder4,txt_midfield4 );


        //attackers
        attackers.add(attacker1);
        playerPositions.put(attacker1,txt_attack1 );
        attackers.add(attacker2);
        playerPositions.put(attacker2,txt_attack2 );

        for(ImageButton a : defenders){
            a.setOnClickListener(defendersClickListener);
        }
        for(ImageButton a : midfielders){
            a.setOnClickListener(midfieldersClickListener);
        }
        for(ImageButton a : attackers){
            a.setOnClickListener(attackersClickListener);
        }
        goalie.setOnClickListener(goalieClickListener);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createTeamViewModel.checkCompletion(dreamTeam)){
                    dreamTeam.setName(teamName);
                    dreamTeam.setId(0);
                    createTeamViewModel.insertDreamTeam(dreamTeam);
                    Intent intent = new Intent(getActivity(), SeasonActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
        });
        btn_autoFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<List<Player>> players = createTeamViewModel.getAllPlayers();
                LiveData<List<Team>> teams = createTeamViewModel.getAllTeams();
                LiveData<List<Squads>> squads = createTeamViewModel.getAllSquads();
                squads.observe(getViewLifecycleOwner(), squads1 -> {
                    teams.observe(getViewLifecycleOwner(), teams1 -> {
                        players.observe(getViewLifecycleOwner(), players1 -> {
                            for(PlayerViews a : playerViews){
                                if(a.getPlayer() == null){
                                    a.setPlayer(createTeamViewModel.returnRandPlayer(players1, a.getRealPosition()));
                                    a.getPlayer().setTeam(createTeamViewModel.setPlayerTeam(a.getPlayer(), squads1, teams1));
                                    a.getPosition().setText(createTeamViewModel.formatName(a.getPlayer()));
                                    a.getPosition().setVisibility(View.VISIBLE);
                                    a.getPlayerRating().setText(a.getPlayer().getPlayerRating());
                                    a.getPlayerValue().setText(String.format("%.2fM$", a.getPlayer().getPlayerValue()/1000000.0));
                                    a.getPlayer().setRealPosition(a.getRealPosition()); //postavi realnu poziciju na igraca
                                    createTeamViewModel.setRealPosOfDreamTeam(a.getPlayer(), dreamTeam);
                                    a.getPlayerKit().setImageResource(createTeamViewModel.setPlayerKit(a.getPlayer()));
                                    userBalance = createTeamViewModel.calcRemainingBalance(a.getPlayer(), userBalance);
                                    txt_balance.setText(String.format("Konto: %.2fM$", userBalance /1000000.0));
                                    LineupSingleton lineupSingleton = LineupSingleton.getInstance();
                                    List<Player> boughtPlayers = lineupSingleton.ReturnList();
                                    lastRememberedTeamSize = boughtPlayers.size(); // potrebno ponovno postaviti kontrolnu varijablu jer u suprotnom
                                                                                    //se desava isto sta se desavalo u onResume, fragment ne zna da niti jedan igrac nije kupljen, pa se onResume dio koda ponovno izvrsava

                                }
                            }
                        });
                    });
                });


            }
        });

    }

    private final View.OnClickListener defendersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(positionListener != null){
                positionListener.setPosition("Defender");
                positionListener.setBalance(userBalance);

            }

            ((CreateTeamFragmentHolderActivity)getActivity()).selectIndex(1);
            for (Map.Entry<ImageButton, TextView> entry : playerPositions.entrySet()) {
                if(v == entry.getKey()){
                    lastRememberedPosition = entry.getValue();
                    //Log.i("POZICIJA KLIKNUTA:", " "+ entry.getValue().getId());
                }

            }

        }
    };
    private final View.OnClickListener midfieldersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(positionListener != null){
                positionListener.setPosition("Midfielder");
                positionListener.setBalance(userBalance);

            }
            ((CreateTeamFragmentHolderActivity)getActivity()).selectIndex(1);
            for (Map.Entry<ImageButton, TextView> entry : playerPositions.entrySet()) {
                if(v == entry.getKey()){
                    lastRememberedPosition = entry.getValue();
                    //Log.i("POZICIJA KLIKNUTA:", " "+ entry.getValue().getId());
                }

            }

        }
    };
    private final View.OnClickListener attackersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(positionListener != null){
                positionListener.setPosition("Attacker");
                positionListener.setBalance(userBalance);
            }
            ((CreateTeamFragmentHolderActivity)getActivity()).selectIndex(1);

            for (Map.Entry<ImageButton, TextView> entry : playerPositions.entrySet()) {
                if(v == entry.getKey()){
                    lastRememberedPosition = entry.getValue();
                    //Log.i("POZICIJA KLIKNUTA:", " "+ entry.getValue().getId());
                }

            }

        }
    };
    private final View.OnClickListener goalieClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(positionListener != null){
                positionListener.setPosition("Goalkeeper");
                positionListener.setBalance(userBalance);
            }
            ((CreateTeamFragmentHolderActivity)getActivity()).selectIndex(1);

            for (Map.Entry<ImageButton, TextView> entry : playerPositions.entrySet()) {
                if(v == entry.getKey()){
                    lastRememberedPosition = entry.getValue();
                    //Log.i("POZICIJA KLIKNUTA:", " "+ entry.getValue().getId());
                }

            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();

    }
    int lastRememberedTeamSize = 0;
    @Override
    public void onResume() {
        LineupSingleton lineupSingleton = LineupSingleton.getInstance();
        ArrayList<Player> players = (ArrayList<Player>) lineupSingleton.ReturnList();

        if(players.size()!=0){
            if(!(lastRememberedTeamSize == players.size())){ //ako je kupnja uspješna u drugom fragmentu, veličina liste će biti različita od zadnje zapamćene
                                                            //ugl bez ove provjere cudni bugovi gdje se zapamćena pozicija igraca ljepila posvuda
                int max = players.size()-1;
                userBalance = createTeamViewModel.calcRemainingBalance(players.get(max), userBalance);
                txt_balance.setText(String.format("Konto: %.2fM$", userBalance /1000000.0));
                lastRememberedPosition.setText(createTeamViewModel.formatName(players.get(max)));
                lastRememberedPosition.setVisibility(View.VISIBLE);
                for(PlayerViews a : playerViews){
                    if(a.getPosition() == lastRememberedPosition){
                        a.setPlayer(players.get(max));
                        a.getPlayerRating().setText(a.getPlayer().getPlayerRating());
                        a.getPlayerValue().setText(String.format("%.2fM$", a.getPlayer().getPlayerValue()/1000000.0));
                        a.getPlayer().setRealPosition(a.getRealPosition()); //postavi realnu poziciju na igraca
                        createTeamViewModel.setRealPosOfDreamTeam(a.getPlayer(), dreamTeam);
                        a.getPlayerKit().setImageResource(createTeamViewModel.setPlayerKit(a.getPlayer()));
                        break;
                        //Log.i("REAL POS:", "" + players.get(max).getRealPosition());
                    }
                }
                lastRememberedTeamSize = players.size();
            }

        }
        super.onResume();
    }
    @Override
    public void onDetach() {
        positionListener = null;
        super.onDetach();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
