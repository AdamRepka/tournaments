package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.PlayerStatistics;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.repository.MatchRepository;
import com.repasoft.tournaments.repository.PlayerStatisticsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentMatchService {

    private final MatchRepository matchRepository;
    private final PlayerStatisticsRepository statsRepository;

    public TournamentMatch saveMatch(TournamentMatch match) {
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

        TournamentMatch savedMatch = matchRepository.save(match);
        TournamentMatch reversedMatch = createReversedMatch(match);
        matchRepository.save(reversedMatch);

        // aktualizace statistik hráčů
        // TODO kdyz se edituje a ulozi, pripoctou se statistiky znova
        updatePlayerStatistics(match.getPlayer1(), player1SetsWon, player2SetsWon, player1Balls, player2Balls, player1SetsWon > player2SetsWon);
        updatePlayerStatistics(match.getPlayer2(), player2SetsWon, player1SetsWon, player2Balls, player1Balls, player2SetsWon > player1SetsWon);

        return savedMatch;
    }

    private TournamentMatch createReversedMatch(final TournamentMatch match) {
        int player1SetsWonReversed = match.getPlayer2SetsWon();
        int player2SetsWonReversed = match.getPlayer1SetsWon();
        Player player1Reversed = match.getPlayer2();
        Player player2Reversed = match.getPlayer1();
        TournamentMatch matchReversed = TournamentMatch.builder()
                .player1(player1Reversed)
                .player2(player2Reversed)
                .player1SetsWon(player1SetsWonReversed)
                .player2SetsWon(player2SetsWonReversed)
                .tournamentGroup(match.getTournamentGroup())
                .finished(true)
                .build();
        List<MatchSet> matchSetsReversed = match.getMatchSets().stream()
                .map(matchSet -> MatchSet.builder()
                        .match(matchReversed)
                        .player1Points(matchSet.getPlayer2Points())
                        .player2Points(matchSet.getPlayer1Points())
                        .build())
                .toList();
        matchReversed.setMatchSets(matchSetsReversed);

        return matchReversed;
    }

    private void updatePlayerStatistics(Player player, int setsWon, int setsLost, int ballsWon, int ballsLost, boolean wonMatch) {
        PlayerStatistics stats = statsRepository.findByPlayer(player)
                .orElse(PlayerStatistics.builder().player(player).build());

        stats.setSetsWon(stats.getSetsWon() + setsWon);
        stats.setSetsLost(stats.getSetsLost() + setsLost);
        stats.setBallsWon(stats.getBallsWon() + ballsWon);
        stats.setBallsLost(stats.getBallsLost() + ballsLost);

        if (wonMatch) {
            stats.setMatchesWon(stats.getMatchesWon() + 1);
        } else if (setsWon == setsLost) {
            stats.setMatchesDraw(stats.getMatchesDraw() + 1);
        } else {
            stats.setMatchesLost(stats.getMatchesLost() + 1);
        }

        // body: např. 3 body za výhru, 1 za remízu, 0 za prohru
        int points = stats.getPoints();
        if (wonMatch) points += 3;
        else if (setsWon == setsLost) points += 1;
        stats.setPoints(points);

        statsRepository.save(stats);
    }

    public TournamentMatch getMatch(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found: " + id));
    }
}

