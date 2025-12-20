package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.*;
import com.repasoft.tournaments.repository.GroupRepository;
import com.repasoft.tournaments.repository.MatchRepository;
import com.repasoft.tournaments.repository.PlayerStatisticsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentMatchService {

    private final MatchRepository matchRepository;
    private final PlayerStatisticsRepository statsRepository;
    private final GroupRepository groupRepository;

    public TournamentMatch  saveMatch(TournamentMatch match) {
        // spočítat vyhrané sety
        int player1SetsWon = 0;
        int player2SetsWon = 0;
        int player1Balls = 0;
        int player2Balls = 0;

        for (MatchSet set : match.getMatchSets()) {
            if (set.getPlayer1Points() > set.getPlayer2Points()) {
                player1SetsWon++;
            } else if (set.getPlayer2Points() > set.getPlayer1Points()) {
                player2SetsWon++;
            }
            player1Balls += set.getPlayer1Points();
            player2Balls += set.getPlayer2Points();
            set.setMatch(match); // nastavit zpětnou vazbu
        }

        match.setPlayer1SetsWon(player1SetsWon);
        match.setPlayer2SetsWon(player2SetsWon);
        match.setFinished(true);

        TournamentMatch reversedMatch = createReversedMatch(match);
        TournamentMatch savedMatch = matchRepository.save(match);
        matchRepository.save(reversedMatch);

        // aktualizace statistik hráčů
        updatePlayerStatistics(match.getPlayer1());

        return savedMatch;
    }

    private TournamentMatch createReversedMatch(final TournamentMatch match) {
        int player1SetsWonReversed = match.getPlayer2SetsWon();
        int player2SetsWonReversed = match.getPlayer1SetsWon();
        TournamentMatch reversedMatch = matchRepository.findAll().stream()
                .filter(rev -> rev.getPlayer1().getId().equals(match.getPlayer2().getId()) && rev.getPlayer2().getId().equals(match.getPlayer1().getId()))
                .findFirst()
                .orElseThrow();
        reversedMatch.setPlayer1SetsWon(player1SetsWonReversed);
        reversedMatch.setPlayer2SetsWon(player2SetsWonReversed);
        reversedMatch.setFinished(true);
        for (int i = 0; i < match.getMatchSets().size(); i++) {
            MatchSet reversedSet = reversedMatch.getMatchSets().get(i);
            reversedSet.setPlayer1Points(match.getMatchSets().get(i).getPlayer2Points());
            reversedSet.setPlayer2Points(match.getMatchSets().get(i).getPlayer1Points());
        }
        return reversedMatch;
    }

    private void updatePlayerStatistics(Player player) {
        TournamentGroup currentGroup = groupRepository.findAllWithPlayersAndMatches().stream()
                .filter(group -> group.equals(player.getTournamentGroup()))
                .findFirst()
                .orElseThrow();

        currentGroup.getPlayers().forEach(tmpPlayer -> {
            List<TournamentMatch> playerMatches = currentGroup.getMatches().stream()
                    .filter(match -> match.getPlayer1().equals(tmpPlayer))
                    .toList();

            PlayerStatistics playerStatistics = statsRepository.findByPlayer(tmpPlayer)
                    .orElseGet(PlayerStatistics::new);

            playerStatistics.setPlayer(tmpPlayer);
            playerStatistics.setPoints(getPoints(playerMatches));
            playerStatistics.setSetsWon(getSetsWon(playerMatches));
            playerStatistics.setSetsLost(getSetsLost(playerMatches));
            playerStatistics.setBallsWon(getBallsWon(playerMatches));
            playerStatistics.setBallsLost(getBallsLost(playerMatches));
            playerStatistics.setMatchesWon(getMatchesWon(playerMatches));
            playerStatistics.setMatchesLost(getMatchesLost(playerMatches));
            playerStatistics.setMatchesDraw(getMatchesDraw(playerMatches));

            tmpPlayer.setPlayerStatistics(playerStatistics);
            statsRepository.save(playerStatistics);
        });

        calculateGroupPositions(currentGroup.getPlayers(), currentGroup);
    }


    public TournamentMatch getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found: " + id));
    }

    private int getPoints(final List<TournamentMatch> matchList) {
        AtomicInteger result = new AtomicInteger();
        matchList.forEach(match -> {
            if (match.isFinished()) {
                if (match.getPlayer1SetsWon() > match.getPlayer2SetsWon()) {
                    result.addAndGet(3);
                } else if (match.getPlayer1SetsWon() == match.getPlayer2SetsWon()) {
                    result.addAndGet(1);
                }
            }
        });
        return result.get();
    }

    private int getSetsWon(final List<TournamentMatch> matchList) {
        return matchList.stream()
                .filter(TournamentMatch::isFinished)
                .mapToInt(TournamentMatch::getPlayer1SetsWon)
                .sum();
    }

    private int getSetsLost(final List<TournamentMatch> matchList) {
        return matchList.stream()
                .filter(TournamentMatch::isFinished)
                .mapToInt(TournamentMatch::getPlayer2SetsWon)
                .sum();
    }

    private int getBallsWon(final List<TournamentMatch> matchList) {
        return matchList.stream()
                .mapToInt(match -> match.getMatchSets().stream()
                        .mapToInt(MatchSet::getPlayer1Points)
                        .sum())
                .sum();
    }

    private int getBallsLost(final List<TournamentMatch> matchList) {
        return matchList.stream()
                .mapToInt(match -> match.getMatchSets().stream()
                        .mapToInt(MatchSet::getPlayer2Points)
                        .sum())
                .sum();
    }

    private int getMatchesWon(final List<TournamentMatch> matchList) {
        return (int) matchList.stream()
                .filter(match -> match.getPlayer1SetsWon() > match.getPlayer2SetsWon())
                .count();
    }

    private int getMatchesLost(final List<TournamentMatch> matchList) {
        return (int) matchList.stream()
                .filter(match -> match.getPlayer1SetsWon() < match.getPlayer2SetsWon())
                .count();
    }

    private int getMatchesDraw(final List<TournamentMatch> matchList) {
        return (int) matchList.stream()
                .filter(match -> match.getPlayer1SetsWon() == match.getPlayer2SetsWon())
                .count();
    }

    public void calculateGroupPositions(Set<Player> players1, TournamentGroup group) {
        List<Player> players = new ArrayList<>(players1.stream().toList());
        Map<Long, PlayerStatistics> statsMap = players.stream()
                .collect(Collectors.toMap(Player::getId, Player::getPlayerStatistics));

        List<TournamentMatch> matches = group.getMatches().stream().toList();

        // Seřadíme hráče s pokročilým komparátorem
        players.sort((p1, p2) -> comparePlayers(p1, p2, players, matches, statsMap));


        // Přiřadíme pozice
        for (int i = 0; i < players.size(); i++) {
            Player changedPlayer = players.get(i);
            int finalI = i;
            players1.stream()
                    .filter(changedPlayer::equals)
                    .findFirst()
                    .ifPresent(originPlayer -> {
                                originPlayer.getPlayerStatistics().setGroupPosition(finalI +1);
                                statsRepository.save(originPlayer.getPlayerStatistics());
                            });
        }
    }

    private int comparePlayers(Player p1, Player p2, List<Player> allPlayers, List<TournamentMatch> matches, Map<Long, PlayerStatistics> statsMap) {
        PlayerStatistics s1 = statsMap.get(p1.getId());
        PlayerStatistics s2 = statsMap.get(p2.getId());

        // 1. Body
        int cmp = Integer.compare(s2.getPoints(), s1.getPoints());
        if (cmp != 0) return cmp;

        // 2. Vícestranný vzájemný zápas mezi hráči se stejnými body
        List<Player> tiedPlayers = allPlayers.stream()
                .filter(p -> statsMap.get(p.getId()).getPoints() == s1.getPoints())
                .collect(Collectors.toList());

        if (tiedPlayers.size() > 2) {
            Map<Long, Integer> winsMap = calculateMutualWins(tiedPlayers, matches);
            cmp = Integer.compare(winsMap.getOrDefault(p2.getId(), 0), winsMap.getOrDefault(p1.getId(), 0));
            if (cmp != 0) return cmp;
        } else {
            // Pokud jsou jen dva, porovnejme jejich vzájemný zápas
            Integer h2h = compareHeadToHead(p1, p2, matches);
            if (h2h != null) return h2h;
        }

        // 3. Sets won
        cmp = Integer.compare(s2.getSetsWon(), s1.getSetsWon());
        if (cmp != 0) return cmp;

        // 4. Balls won
        cmp = Integer.compare(s2.getBallsWon(), s1.getBallsWon());
        if (cmp != 0) return cmp;

        // 5. Abecední řazení
        cmp = p1.getLastName().compareToIgnoreCase(p2.getLastName());
        if (cmp != 0) return cmp;

        return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
    }

    private static Map<Long, Integer> calculateMutualWins(List<Player> players, List<TournamentMatch> matches) {
        Map<Long, Integer> winCounts = new HashMap<>();

        for (Player p : players) {
            winCounts.put(p.getId(), 0);
        }

        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player p1 = players.get(i);
                Player p2 = players.get(j);
                Integer result = compareHeadToHead(p1, p2, matches);
                if (result == null) continue;

                if (result < 0) {
                    winCounts.put(p1.getId(), winCounts.get(p1.getId()) + 1);
                } else if (result > 0) {
                    winCounts.put(p2.getId(), winCounts.get(p2.getId()) + 1);
                }
            }
        }

        return winCounts;
    }

    private static Integer compareHeadToHead(Player p1, Player p2, List<TournamentMatch> matches) {
        List<TournamentMatch> relevantMatches = matches.stream()
                .filter(m -> isMatchBetween(m, p1, p2))
                .toList();

        int winsP1 = 0;
        int winsP2 = 0;

        for (TournamentMatch m : relevantMatches) {
            boolean isP1 = m.getPlayer1().getId().equals(p1.getId());
            int setsWon1 = m.getPlayer1SetsWon();
            int setsWon2 = m.getPlayer2SetsWon();

            if (!isP1) {
                // Prohodíme, pokud je p1 jako player2
                int temp = setsWon1;
                setsWon1 = setsWon2;
                setsWon2 = temp;
            }

            if (setsWon1 > setsWon2) {
                winsP1++;
            } else if (setsWon1 < setsWon2) {
                winsP2++;
            } else {
                int p1Points = totalPoints(m.getMatchSets(), isP1);
                int p2Points = totalPoints(m.getMatchSets(), !isP1);
                if (p1Points > p2Points) winsP1++;
                else if (p1Points < p2Points) winsP2++;
            }
        }

        if (winsP1 > winsP2) return -1;
        if (winsP2 > winsP1) return 1;
        return null; // nerozhodnuto
    }

    private static int totalPoints(List<MatchSet> sets, boolean isPlayer1) {
        return sets.stream()
                .mapToInt(s -> isPlayer1 ? s.getPlayer1Points() : s.getPlayer2Points())
                .sum();
    }

    private static boolean isMatchBetween(TournamentMatch match, Player p1, Player p2) {
        long id1 = p1.getId();
        long id2 = p2.getId();
        long m1 = match.getPlayer1().getId();
        long m2 = match.getPlayer2().getId();
        return (m1 == id1 && m2 == id2) || (m1 == id2 && m2 == id1);
    }
}

