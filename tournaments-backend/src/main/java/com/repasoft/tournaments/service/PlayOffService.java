package com.repasoft.tournaments.service;

import com.repasoft.tournaments.dto.PlayOffSetupDto;
import com.repasoft.tournaments.model.PlayOffMatch;
import com.repasoft.tournaments.model.PlayOffMatchSet;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.repository.PlayOffMatchRepository;
import com.repasoft.tournaments.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayOffService {

    private final PlayerRepository playerRepository;
    private final PlayOffMatchRepository playOffMatchRepository;

    /**
     * Vygeneruje první kolo play-off podle nastavení
     */
    public List<PlayOffMatch> generatePlayOff(PlayOffSetupDto settingsDto) {

        validateSettings(settingsDto);

        List<Player> players = playerRepository.findAll();

        if (players.isEmpty()) {
            throw new IllegalStateException("Neexistují žádní hráči pro play-off");
        }

        List<Player> rankedPlayers = rankPlayers(players);

        int requiredPlayers = settingsDto.getStartRound().getRequiredPlayers();

        if (rankedPlayers.size() < requiredPlayers) {
            throw new IllegalStateException(
                    "Nedostatek hráčů pro " + settingsDto.getStartRound()
            );
        }

        List<Player> qualifiedPlayers =
                rankedPlayers.subList(0, requiredPlayers);

        List<PlayOffMatch> result = createFirstRoundMatches(qualifiedPlayers, settingsDto);

        playOffMatchRepository.saveAll(result);

        return result;
    }

    // -----------------------------------------------------
    // VALIDACE
    // -----------------------------------------------------

    private void validateSettings(PlayOffSetupDto dto) {
        if (dto.getSetsToWin() != 3 && dto.getSetsToWin() != 5) {
            throw new IllegalArgumentException("Povolené hodnoty jsou BO3 nebo BO5");
        }
    }

    // -----------------------------------------------------
    // ŘAZENÍ HRÁČŮ
    // -----------------------------------------------------

    private List<Player> rankPlayers(List<Player> players) {

        return players.stream()
                .sorted(
                        Comparator
                                .comparingInt(
                                        (Player p) -> p.getPlayerStatistics().getGroupPosition()
                                )
                                .thenComparing(
                                        p -> p.getPlayerStatistics().getPoints(),
                                        Comparator.reverseOrder()
                                )
                                .thenComparing(
                                        p -> p.getPlayerStatistics().getSetsWon()
                                                - p.getPlayerStatistics().getSetsLost(),
                                        Comparator.reverseOrder()
                                )
                                .thenComparing(
                                        p -> p.getPlayerStatistics().getBallsWon()
                                                - p.getPlayerStatistics().getBallsLost(),
                                        Comparator.reverseOrder()
                                )
                                .thenComparing(Player::getLastName)
                                .thenComparing(Player::getFirstName)
                )
                .toList();
    }

    // -----------------------------------------------------
    // TVORBA ZÁPASŮ
    // -----------------------------------------------------

    private List<PlayOffMatch> createFirstRoundMatches(
            List<Player> players,
            PlayOffSetupDto settingsDto
    ) {

        List<PlayOffMatch> matches = new ArrayList<>();

        int left = 0;
        int right = players.size() - 1;

        while (left < right) {

            Player player1 = players.get(left);
            Player player2 = players.get(right);

            PlayOffMatch match =
                    createMatch(player1, player2, settingsDto.getSetsToWin());

            matches.add(match);

            left++;
            right--;
        }

        return matches;
    }

    // -----------------------------------------------------
    // TVORBA ZÁPASU + SETŮ
    // -----------------------------------------------------

    private PlayOffMatch createMatch(Player p1, Player p2, int setsToWin) {

        PlayOffMatch match = new PlayOffMatch();
        match.setPlayer1(p1);
        match.setPlayer2(p2);
        match.setFinished(false);
        match.setPlayer1SetsWon(0);
        match.setPlayer2SetsWon(0);

        List<PlayOffMatchSet> sets = new ArrayList<>();

        int maxSets = setsToWin * 2 - 1;

        for (int i = 1; i <= maxSets; i++) {
            PlayOffMatchSet set = new PlayOffMatchSet();
            set.setMatch(match);
            set.setPlayer1Points(0);
            set.setPlayer2Points(0);

            sets.add(set);
        }

        match.setMatchSets(sets);

        return match;
    }
}
