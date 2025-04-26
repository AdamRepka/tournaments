package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.Match;
import com.repasoft.tournaments.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public Match updateMatch(Long id, Match updatedMatch) {
        return matchRepository.findById(id)
                .map(match -> {
                    match.setPlayer1(updatedMatch.getPlayer1());
                    match.setPlayer2(updatedMatch.getPlayer2());
                    match.setGroup(updatedMatch.getGroup());
                    match.setPlayer1SetsWon(updatedMatch.getPlayer1SetsWon());
                    match.setPlayer2SetsWon(updatedMatch.getPlayer2SetsWon());
                    return matchRepository.save(match);
                })
                .orElseThrow(() -> new RuntimeException("Match not found with id " + id));
    }

    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
