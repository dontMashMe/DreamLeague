package com.example.dreamleague.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dreamleague.DataModels.DreamTeam;
import com.example.dreamleague.DataModels.GameResults;
import com.example.dreamleague.DataModels.LineupSingleton;
import com.example.dreamleague.DataModels.Match;
import com.example.dreamleague.DataModels.MatchScores;
import com.example.dreamleague.DataModels.NumberOfGoalsTuple;
import com.example.dreamleague.DataModels.Player;
import com.example.dreamleague.DataModels.PlayerPoints;
import com.example.dreamleague.DataModels.PostGameScores;
import com.example.dreamleague.DataModels.Squads;
import com.example.dreamleague.DataModels.Team;
import com.example.dreamleague.DataModels.Utils;
import com.example.dreamleague.R;
import com.example.dreamleague.Repository.DreamTeamRepository;
import com.example.dreamleague.Repository.MatchScoresRepository;
import com.example.dreamleague.Repository.MatchesRepository;
import com.example.dreamleague.Repository.PlayerPointsRepository;
import com.example.dreamleague.Repository.PlayerRepository;
import com.example.dreamleague.Repository.PostGameScoresRepository;
import com.example.dreamleague.Repository.SquadsRepository;
import com.example.dreamleague.Repository.TeamRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeasonViewModel extends AndroidViewModel {

    private final DreamTeamRepository dreamTeamRepository;
    private final PlayerRepository playerRepository;
    private final SquadsRepository squadsRepository;
    private final TeamRepository teamRepository;
    private final MatchesRepository matchesRepository;
    private final MatchScoresRepository matchScoresRepository;
    private final PlayerPointsRepository playerPointsRepository;
    private final PostGameScoresRepository postGameScoresRepository;

    private final LiveData<List<DreamTeam>> dreamTeam;
    private final LiveData<List<Player>> allPlayers;
    private final LiveData<List<Squads>> allSquads;
    private final LiveData<List<Team>> allTeams;
    private final LiveData<List<Match>> allMatches;

    public SeasonViewModel(@NonNull Application application) {
        super(application);
        dreamTeamRepository = new DreamTeamRepository(application);
        dreamTeam = dreamTeamRepository.getDreamTeam();

        playerRepository = new PlayerRepository(application);
        allPlayers = playerRepository.getAllPlayers();

        squadsRepository = new SquadsRepository(application);
        allSquads = squadsRepository.getAllSquads();

        teamRepository = new TeamRepository(application);
        allTeams = teamRepository.getAllTeams();

        matchesRepository = new MatchesRepository(application);
        allMatches = matchesRepository.getAllMatches();

        matchScoresRepository = new MatchScoresRepository(application);

        playerPointsRepository = new PlayerPointsRepository(application);
        postGameScoresRepository = new PostGameScoresRepository(application);
        fillLogoMap();
        fillMap();
    }

    public LiveData<List<NumberOfGoalsTuple>> getNumberOfGoals(List<Integer> playerIds){
        return matchScoresRepository.getNumberOfGoals(playerIds);
    }
    public void resetAllTeamPoints() {
        teamRepository.resetAllTeamPoints();
    }

    public LiveData<List<Match>> getAllMatchesFromTeam(int teamId) {
        return matchesRepository.getAllMatchesFromTeam(teamId);
    }

    public void deleteAllMatches() {
        matchesRepository.deleteAllMatches();
    }

    public void deleteAllMatchScores() {
        matchScoresRepository.deleteAllMatchScores();
    }

    public void deleteAllPlayerPoints() {
        playerPointsRepository.deleteAllPlayerPoints();
    }

    public void deleteDreamTeam(DreamTeam dreamTeam) {
        dreamTeamRepository.deleteDreamTeam(dreamTeam);
    }

    public void insertPostGameScore(PostGameScores postGameScores) {
        postGameScoresRepository.insertPostGameScore(postGameScores);
    }

    public LiveData<List<PostGameScores>> getAllPostGameScores() {
        return postGameScoresRepository.getAllPostGameScores();
    }

    public int getAllPointsSum() {
        return playerPointsRepository.getAllPointsSum();
    }

    public LiveData<List<Player>> transferQueryOnlyPositionSet(String position) {
        return playerRepository.transferQueryOnlyPositionSet(position);
    }

    public LiveData<List<Player>> transferQueryOnlyNameSet(String teamName) {
        return playerRepository.transferQueryOnlyNameSet(teamName);
    }


    public LiveData<List<Player>> getPlayersTransferQuery(String team_name, String pos) {
        return playerRepository.getPlayersTransferQuery(team_name, pos);
    }

    public void updateWinner(int team_id) {
        teamRepository.updateWinner(team_id);
    }

    public void updateDraw(int team_id) {
        teamRepository.updateDraw(team_id);
    }

    public void insertMatchScores(MatchScores matchScores) {
        matchScoresRepository.InsertMatchScores(matchScores);
    }

    public int getPlayerPointsInt(int playerId) {
        return playerPointsRepository.getPlayerPointsInt(playerId);
    }

    public LiveData<List<PlayerPoints>> getAllPlayerPoints() {
        return playerPointsRepository.getAllPlayerPoints();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return this.allPlayers;
    }

    public LiveData<List<DreamTeam>> getDreamTeam() {
        return this.dreamTeam;
    }

    public LiveData<List<Squads>> getAllSquads() {
        return this.allSquads;
    }

    public LiveData<List<Team>> getAllTeams() {
        return this.allTeams;
    }

    public LiveData<List<Match>> getAllMatches() {
        return this.allMatches;
    }

    public LiveData<List<Player>> getPlayersFromTeam(int team_id) {
        return playerRepository.getPlayersFromTeam(team_id);
    }

    public LiveData<List<Match>> last5Matches(int currentWeek, int teamId) {
        return matchesRepository.last5Matches(currentWeek, teamId);
    }

    public LiveData<List<Match>> getMatchesFromWeek(int week) {
        return matchesRepository.getMatchesFromWeek(week);
    }

    public void updateMatchesWhere(int gameId, int homeScore, int awayScore) {
        matchesRepository.updateMatchWhere(gameId, homeScore, awayScore);
    }

    public List<MatchScores> getMatchScoresForGame(int gameId) {
        return matchScoresRepository.getMatchScoresForGame(gameId);
    }

    public LiveData<Integer> getPlayerPoints(int playerId) {
        return playerPointsRepository.getPlayerPoints(playerId);
    }

    public Player getPlayerFromId(int playerId, List<Player> players){
        for(Player a : players){
            if(a.getPlayerId() == playerId) return a;
        }
        return null;
    }



    public LiveData<List<MatchScores>> numberOfGoals(List<Integer> playerIds){
        return matchScoresRepository.numberOfGoals(playerIds);
    }

    public List<Match> getMatchesFromWeekStatic(List<Match> matches, int week) {
        List<Match> returnList = new ArrayList<>();
        for (Match a : matches) {
            if (a.getWeek() == week) returnList.add(a);
        }
        return returnList;
    }

    public Player findManager(List<Player> players) {
        for (Player a : players) {
            if (a.getPosition().equals("")) return a;
        }
        return null;
    }

    public double calculateTeamWinRate(List<Match> matches, int teamId, int week) {
        double wins = 0, draws = 0, counter = 0;
        for (Match a : matches) {
            if (a.getWeek() < week) {
                counter++;
                if (a.getTeamHome() == teamId) {
                    if (a.getTeamHomeScore() > a.getTeamAwayScore()) {
                        wins++;
                    } else if (a.getTeamHomeScore() == a.getTeamAwayScore()) draws++;
                } else if (a.getTeamAway() == teamId) {
                    if (a.getTeamAwayScore() > a.getTeamHomeScore()) {
                        wins++;
                    } else if (a.getTeamAwayScore() == a.getTeamHomeScore()) draws++;
                }
            }
        }
        return Math.round((2 * wins + draws) / (2 * counter) * 100) / 100.0;
    }

    //početno generiranje utakmica
    public void InitialMatchesGenerator(List<Team> ListTeam) {
        int numDays = (ListTeam.size() - 1);
        int halfSize = ListTeam.size() / 2;
        List<Team> teams = new ArrayList<>(ListTeam);
        teams.remove(0); //0ti element ostaje fiksan, a ostali elementi se permutiraju (vidi dolje)
        //ako zelimo zadrzati prvi element lista mora biti specificno soritrana, nisam bas skuzio; ovo je jednostavnije
        Collections.shuffle(teams); //shufflaj listu, rezultat su različita kola
        int teamSize = teams.size();
        for (int day = 0; day < numDays; day++) {
            int week = day + 1;
            int teamIdx = day % teamSize;
            //utakmice sa 0tim timom
            Match matchWith0thTeam = new Match(0, week, teams.get(teamIdx).getTeam_id(), ListTeam.get(0).getTeam_id(), 0, 0);
            matchesRepository.insertMatch(matchWith0thTeam);
            for (int idx = 1; idx < halfSize; idx++) {
                //permutacije elemenata liste
                //n/2-> polu sezona; zamijeni domace i gostujuce timove
                //1 -> 2 -> 3 -> 4 ... -> n/2 - 1 -> n - 1 -> n - 2 -> n - 3 -> ... -> n/2 -> 1
                int firstTeam = (day + idx) % teamSize;
                int secondTeam = (day + teamSize - idx) % teamSize;
                Match match = new Match(0, week, teams.get(firstTeam).getTeam_id(), teams.get(secondTeam).getTeam_id(), 0, 0);
                matchesRepository.insertMatch(match);
            }
        }

    }

    Map<String, Integer> map = new HashMap<>();
    Map<String, Integer> logo_map = new HashMap<>();

    /**
     * početno postavljanje korsničkog tima i njihovih realnih pozicija te postavljanje dresova i logo-a za timove
     */
    public List<Player> initialTeamSetup(DreamTeam dreamTeam, List<Player> players, List<Squads> squads, List<Team> teams) {
        List<Player> tempList = new ArrayList<>();
        for (Player a : players) {
            if (dreamTeam.getAllIds().contains(a.getPlayerId())) {
                if (a.getPlayerId() == dreamTeam.getGoalie()) {
                    a.setRealPosition(1);
                } else if (a.getPlayerId() == dreamTeam.getDefenderLeft()) {
                    a.setRealPosition(2);
                } else if (a.getPlayerId() == dreamTeam.getDefenderMidFirst()) {
                    a.setRealPosition(3);
                } else if (a.getPlayerId() == dreamTeam.getDefenderMidSecond()) {
                    a.setRealPosition(4);
                } else if (a.getPlayerId() == dreamTeam.getDefenderRight()) {
                    a.setRealPosition(5);
                } else if (a.getPlayerId() == dreamTeam.getMidLeft()) {
                    a.setRealPosition(6);
                } else if (a.getPlayerId() == dreamTeam.getMidMidFirst()) {
                    a.setRealPosition(7);
                } else if (a.getPlayerId() == dreamTeam.getMidMidSecond()) {
                    a.setRealPosition(8);
                } else if (a.getPlayerId() == dreamTeam.getMidRight()) {
                    a.setRealPosition(9);
                } else if (a.getPlayerId() == dreamTeam.getAttackLeft()) {
                    a.setRealPosition(10);
                } else if (a.getPlayerId() == dreamTeam.getAttackRight()) {
                    a.setRealPosition(11);
                }
                a.setTeam(setPlayerTeam(a, squads, teams));
                tempList.add(a);
            }
        }
        for (Player a : tempList) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (a.getTeam().getName().equals(entry.getKey())) {
                    a.getTeam().setTeamKit(entry.getValue());
                }
            }
            for (Map.Entry<String, Integer> entry : logo_map.entrySet()) {
                if (a.getTeam().getName().equals(entry.getKey())) {
                    a.getTeam().setTeamLogo(entry.getValue());
                }
            }
        }
        return tempList;
    }

    void fillMap() {
        map.put("Arsenal FC", R.drawable.arsenal_kit);
        map.put("Aston Villa FC", R.drawable.aston_kit);
        map.put("Everton FC", R.drawable.everton_kit);
        map.put("Fulham FC", R.drawable.fulham_kit);
        map.put("Liverpool FC", R.drawable.liverpool_kit);
        map.put("Manchester City FC", R.drawable.mnc_kit);
        map.put("Manchester United FC", R.drawable.mnu_kit);
        map.put("Tottenham Hotspur FC", R.drawable.totenham_kit);
        map.put("Wolverhampton Wanderers FC", R.drawable.wolves_kit);
        map.put("Burnley FC", R.drawable.burnley_kit);
        map.put("Leicester City FC", R.drawable.leicester_kit);
        map.put("Southampton FC", R.drawable.southamp_kit);
        map.put("Leeds United FC", R.drawable.leeds);
        map.put("Crystal Palace FC", R.drawable.cpa_kit);
        map.put("Sheffield United FC", R.drawable.sheffield);
        map.put("Brighton & Hove Albion FC", R.drawable.brighton_kit);
        map.put("West Ham United FC", R.drawable.wham_kit);
        map.put("Newcastle United FC", R.drawable.newcas_kit);
        map.put("Chelsea FC", R.drawable.chelsea_kit);
        map.put("West Bromwich Albion FC", R.drawable.wba_fc);
    }

    void fillLogoMap() {
        logo_map.put("Arsenal FC", R.drawable.arsenal_logo);
        logo_map.put("Aston Villa FC", R.drawable.aston_logo);
        logo_map.put("Everton FC", R.drawable.everton_logo);
        logo_map.put("Fulham FC", R.drawable.fulham_logo);
        logo_map.put("Liverpool FC", R.drawable.liverpool_logo);
        logo_map.put("Manchester City FC", R.drawable.mnc_logo);
        logo_map.put("Manchester United FC", R.drawable.mnu_logo);
        logo_map.put("Tottenham Hotspur FC", R.drawable.totenham_logo);
        logo_map.put("Wolverhampton Wanderers FC", R.drawable.wolves_logo);
        logo_map.put("Burnley FC", R.drawable.burnley_logo);
        logo_map.put("Leicester City FC", R.drawable.leicester_logo);
        logo_map.put("Southampton FC", R.drawable.southamp_logo);
        logo_map.put("Leeds United FC", R.drawable.leeds_logo);
        logo_map.put("Crystal Palace FC", R.drawable.cpa_logo);
        logo_map.put("Sheffield United FC", R.drawable.sheffield_logo);
        logo_map.put("Brighton & Hove Albion FC", R.drawable.brighton_logo);
        logo_map.put("West Ham United FC", R.drawable.wham_logo);
        logo_map.put("Newcastle United FC", R.drawable.newcas_logo);
        logo_map.put("Chelsea FC", R.drawable.chelsea_logo);
        logo_map.put("West Bromwich Albion FC", R.drawable.wba_logo);
    }

    public int setPlayerKit(Player player) {
        fillMap();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (player.getTeam().getName().equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public Team setPlayerTeam(Player player, List<Squads> squads, List<Team> teams) {
        int temp = 0;
        for (Squads a : squads) {
            if (a.getPlayerId() == player.getPlayerId()) {
                temp = a.getTeamId();
                break;
            }
        }
        for (Team a : teams) {
            if (a.getTeam_id() == temp) {
                return a;
            }
        }
        return null;
    }

    public String formatName(Player player) {
        String[] name_surname = player.getName().split(" ");
        if (name_surname.length != 1) {
            String surname = name_surname[1];
            if (surname.length() <= 9) {
                return surname;
            } else {
                StringBuilder formated_lastName = new StringBuilder();
                for (int i = 0; i < 7; i++) {
                    formated_lastName.append(surname.charAt(i));
                }
                formated_lastName.append("...");
                return formated_lastName.toString();
            }
        } else {
            return name_surname[0];
        }
    }

    public Team getTeamById(int id, List<Team> teams) {
        for (Team a : teams) {
            if (id == a.getTeam_id()) {
                return a;
            }
        }
        return null;
    }

    public int setTeamKit(Team team) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (team.getName().equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public int setTeamLogo(Team team) {
        for (Map.Entry<String, Integer> entry : logo_map.entrySet()) {
            if (team.getName().equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public void setTeamLogos(List<Team> teams) {
        for (Team team : teams) {
            for (Map.Entry<String, Integer> entry : logo_map.entrySet()) {
                if (team.getName().equals(entry.getKey())) {
                    team.setTeamLogo(entry.getValue());
                }
            }
        }

    }


    public List<Match> setTeamsAndLogos(List<Match> matches, List<Team> teams) {
        List<Match> returnList = new ArrayList<>();
        for (Match a : matches) {
            a.setObjTeamHome(getTeamById(a.getTeamHome(), teams));
            a.getObjTeamHome().setTeamLogo(setTeamLogo(a.getObjTeamHome()));
            a.setObjTeamAway(getTeamById(a.getTeamAway(), teams));
            a.getObjTeamAway().setTeamLogo(setTeamLogo(a.getObjTeamAway()));
            returnList.add(a);
        }
        return returnList;
    }

    public Player getPlayerFromRealPos(int realPos, List<Player> players) {
        for (Player a : players) {
            if (a.getRealPosition() == realPos) {
                return a;
            }
        }
        return null;
    }

    public Float calcAvgTeamRating(List<Player> players) {
        float sum = 0;
        for (Player a : players) {
            sum += Integer.parseInt(a.getPlayerRating());
        }
        return sum / players.size();
    }

    private static final double baseChance = 0.5;

    /**
     * Postavlja "težinu" igrača kod izračuna vjerojatnosti zabijanja pogotka
     * napadači imaju veću težinsku vrijednost, centarfori manje i obrana najmanju (golmani nemaju, nebi se trebalo desit da golman zabije)
     * uz poziciju dodaj player rating / 1000
     * npr Cavani 93 PR -> Napadač
     * 0.70 + 0.093 = 0.793 -> 79.3% šanse da je Cavani zabio pogodak, ukoliko se isti dogodi
     **/
    private void weightSetter(Player player) {
        switch (player.getPosition()) {
            case "Goalkeeper":
                player.setProbabilityWeight(0.05); //shouldnt happen
                break;
            case "Defender":
                player.setProbabilityWeight(0.20 + (Double.parseDouble(player.getPlayerRating()) / 1000));
                break;
            case "Midfielder":
                player.setProbabilityWeight(0.40 + (Double.parseDouble(player.getPlayerRating())) / 1000);
                break;
            case "Attacker":
                player.setProbabilityWeight(0.60 + (Double.parseDouble(player.getPlayerRating())) / 1000);
                break;
        }
    }

    public List<GameResults> advanceWeek(List<Match> upcomingWeekMatches, List<Team> teamsWithPlayers) {

        List<GameResults> gameResults = new ArrayList<>();
        for (Match a : upcomingWeekMatches) {
            //posloži team home i team away te njihov team rating
            Team home = getTeamById(a.getTeamHome(), teamsWithPlayers);
            float homeTeamRating = calcAvgTeamRating(home.getPlayerList());
            Team away = getTeamById(a.getTeamAway(), teamsWithPlayers);
            float awayTeamRating = calcAvgTeamRating(away.getPlayerList());

            boolean flag = false;
            int teamHomeScore = 0;
            int teamAwayScore = 0;
            double flagIncrementer = 0.10;
            //postavi težine
            for (Player b : home.getPlayerList()) {
                weightSetter(b);
            }
            for (Player c : away.getPlayerList()) {
                weightSetter(c);
            }
            //strijelci oba tima
            List<Player> awayScorers = new ArrayList<>();
            List<Player> homeScorers = new ArrayList<>();
            while (!flag) { //kontrolna varijabla
                if (new Random().nextDouble() <= baseChance - (homeTeamRating - awayTeamRating)) {
                    teamAwayScore++;
                    boolean scoredFlag = false;
                    while (!scoredFlag) { //dešavalo se da se na niti jednom igraču ne triggera random event, ovo osigurava da svaki gol ima strijelca
                        for (Player player : away.getPlayerList()) {
                            if (new Random().nextDouble() <= player.getProbabilityWeight()) {
                                awayScorers.add(player);
                                scoredFlag = true;
                                break;
                            }
                        }
                    }
                }
                if (new Random().nextDouble() <= baseChance - (awayTeamRating - homeTeamRating)) {
                    teamHomeScore++;
                    boolean scoredFlag = false;
                    while (!scoredFlag) {
                        for (Player player : home.getPlayerList()) {
                            if (new Random().nextDouble() <= player.getProbabilityWeight()) {
                                homeScorers.add(player);
                                scoredFlag = true;
                                break;
                            }
                        }
                    }
                }
                if (new Random().nextDouble() <= flagIncrementer) {
                    flag = true; //ako se ovdje triggera odma izađi iz petlje, zanimljiviji rezultati jer je broj golova nasumičan
                } else {
                    flagIncrementer += 0.10; //inkrementiraj kako bi se izbjegla beskonačna petlja
                }

            }
            //pobroji golove po igraču
            Map<Player, Integer> countAway = new HashMap<>();
            for (Player player : awayScorers) {
                if (!countAway.containsKey(player)) {
                    countAway.put(player, 1);
                } else {
                    countAway.put(player, countAway.get(player) + 1);
                }
            }
            Map<Player, Integer> countHome = new HashMap<>();
            for (Player player : homeScorers) {
                if (!countHome.containsKey(player)) {
                    countHome.put(player, 1);
                } else {
                    countHome.put(player, countHome.get(player) + 1);
                }
            }
            gameResults.add(new GameResults(a.getGameId(), a.getTeamHome(), a.getTeamAway(), teamHomeScore, teamAwayScore, countHome, countAway));
        }
        return gameResults;
    }

    public void updatePoints(GameResults gameResults) {
        if (gameResults.getAwayTeamScore() > gameResults.getHomeTeamScore()) {
            updateWinner(gameResults.getTeamAwayId());
        } else if (gameResults.getHomeTeamScore() > gameResults.getAwayTeamScore()) {
            updateWinner(gameResults.getHomeTeamId());
        } else {
            updateDraw(gameResults.getHomeTeamId());
            updateDraw(gameResults.getTeamAwayId());
        }
    }

    public void sellPlayer(int realPosition, int playerId) {
        playerPointsRepository.deletePlayer(playerId);
        switch (realPosition) {
            case 1:
                dreamTeamRepository.sellGoalie();
                break;
            case 2:
                dreamTeamRepository.sellDefenderLeft();
                break;
            case 3:
                dreamTeamRepository.sellDefenderMidFirst();
                break;
            case 4:
                dreamTeamRepository.sellDefenderMidSecond();
                break;
            case 5:
                dreamTeamRepository.sellDefenderRight();
                break;
            case 6:
                dreamTeamRepository.sellMidLeft();
                break;
            case 7:
                dreamTeamRepository.sellMidMidFirst();
                break;
            case 8:
                dreamTeamRepository.sellMidMidSecond();
                break;
            case 9:
                dreamTeamRepository.sellMidRight();
                break;
            case 10:
                dreamTeamRepository.sellAttackerLeft();
                break;
            case 11:
                dreamTeamRepository.sellAttackerRight();
                break;
        }
    }

    public void buyPlayer(Player player, DreamTeam dreamTeam) {
        switch (player.getRealPosition()) {
            case 1:
                dreamTeamRepository.buyGoalie(player.getPlayerId());
                dreamTeam.setGoalie(player.getPlayerId());
                break;
            case 2:
                dreamTeamRepository.buyDefenderLeft(player.getPlayerId());
                dreamTeam.setDefenderLeft(player.getPlayerId());
                break;
            case 3:
                dreamTeamRepository.buyDefenderMidFirst(player.getPlayerId());
                dreamTeam.setDefenderMidFirst(player.getPlayerId());
                break;
            case 4:
                dreamTeamRepository.buyDefenderMidSecond(player.getPlayerId());
                dreamTeam.setDefenderMidSecond(player.getPlayerId());
                break;
            case 5:
                dreamTeamRepository.buyDefenderRight(player.getPlayerId());
                dreamTeam.setDefenderRight(player.getPlayerId());
                break;
            case 6:
                dreamTeamRepository.buyMidLeft(player.getPlayerId());
                dreamTeam.setMidLeft(player.getPlayerId());
                break;
            case 7:
                dreamTeamRepository.buyMidMidFirst(player.getPlayerId());
                dreamTeam.setMidMidFirst(player.getPlayerId());
                break;
            case 8:
                dreamTeamRepository.buyMidMidSecond(player.getPlayerId());
                dreamTeam.setMidMidSecond(player.getPlayerId());
                break;
            case 9:
                dreamTeamRepository.buyMidRight(player.getPlayerId());
                dreamTeam.setMidRight(player.getPlayerId());
                break;
            case 10:
                dreamTeamRepository.buyAttackerLeft(player.getPlayerId());
                dreamTeam.setAttackLeft(player.getPlayerId());
                break;
            case 11:
                dreamTeamRepository.buyAttackerRight(player.getPlayerId());
                dreamTeam.setAttackRight(player.getPlayerId());
                break;
        }
        playerPointsRepository.insertPlayer(player.getPlayerId());
        Utils.putBalance(getApplication(), Utils.getBalance(getApplication()) - player.getPlayerValue());
    }

    //nisam pretjerano ponosan na ovo ali ne znam kak drugacije da to napravim
    public int inferFreePosition(String position, DreamTeam dreamTeam) {
        switch (position) {
            case "Goalkeeper":
                if (dreamTeam.getGoalie() == 0) {
                    return 1;
                }
                break;
            case "Defender":
                if (dreamTeam.getDefenderLeft() == 0) {
                    return 2;
                } else if (dreamTeam.getDefenderMidFirst() == 0) {
                    return 3;
                } else if (dreamTeam.getDefenderMidSecond() == 0) {
                    return 4;
                } else if (dreamTeam.getDefenderRight() == 0) {
                    return 5;
                }
                break;
            case "Midfielder":
                if (dreamTeam.getMidLeft() == 0) {
                    return 6;
                } else if (dreamTeam.getMidMidFirst() == 0) {
                    return 7;
                } else if (dreamTeam.getMidMidSecond() == 0) {
                    return 8;
                } else if (dreamTeam.getMidRight() == 0) {
                    return 9;
                }
                break;
            case "Attacker":
                if (dreamTeam.getAttackLeft() == 0) {
                    return 10;
                } else if (dreamTeam.getAttackRight() == 0) {
                    return 11;
                }
                break;
        }

        return 0;
    }

    public void updatePlayerPoints(List<Player> players, Match match) {
        for (Player a : players) {
            int numberOfGoals = matchScoresRepository.returnNumberOfGoals(a.getPlayerId(), match.getGameId());
            a.setPointsAcquired(calculatePlayerPoints(match, a, numberOfGoals));
        }
        playerPointsRepository.updatePlayerPoints(players);
    }

    public int calculatePlayerPoints(Match match, Player player, int numberOfGoals) {
        int pointsSum = 0;
        //performanse tima
        if (match.getTeamHome() == player.getTeam().getTeam_id()) {
            if (match.getTeamHomeScore() > match.getTeamAwayScore()) {
                pointsSum += 2;
                //clean sheet
                if (match.getTeamAwayScore() == 0) {
                    switch (player.getPosition()) {
                        case "Goalkeeper":
                        case "Defender":
                            pointsSum += 4;
                            break;
                        case "Midfielder":
                            pointsSum += 1;
                            break;
                    }
                }
            } else if (match.getTeamHomeScore() == match.getTeamAwayScore()) pointsSum += 1;
        } else if (match.getTeamAway() == player.getTeam().getTeam_id()) {
            if (match.getTeamAwayScore() > match.getTeamHomeScore()) {
                pointsSum += 2;
                if (match.getTeamHomeScore() == 0) {
                    switch (player.getPosition()) {
                        case "Goalkeeper":
                        case "Defender":
                            pointsSum += 4;
                            break;
                        case "Midfielder":
                            pointsSum += 1;
                            break;
                    }
                }
            } else if (match.getTeamAwayScore() == match.getTeamHomeScore()) pointsSum += 1;
        }
        //broj golova
        switch (player.getPosition()) {
            case "Goalkeeper":
                pointsSum += numberOfGoals * 8;
                break;
            case "Defender":
                pointsSum += numberOfGoals * 6;
                break;
            case "Midfielder":
                pointsSum += numberOfGoals * 5;
                break;
            case "Attacker":
                pointsSum += numberOfGoals * 4;
                break;
        }
        return pointsSum;
    }


}
