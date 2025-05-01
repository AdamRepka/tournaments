package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentMatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public TournamentMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<TournamentMatch> getAllTournamentMatches() {
        return matchRepository.findAll();
    }

    public Optional<TournamentMatch> getTournamentMatchById(Long id) {
        return matchRepository.findById(id);
    }

    public TournamentMatch createTournamentMatch(TournamentMatch match) {
        return matchRepository.save(match);
    }

    public TournamentMatch updateTournamentMatch(Long id, TournamentMatch updatedMatch) {
        return matchRepository.findById(id)
                .map(match -> {
                    match.setPlayer1(updatedMatch.getPlayer1());
                    match.setPlayer2(updatedMatch.getPlayer2());
                    match.setTournamentGroup(updatedMatch.getTournamentGroup());
                    match.setPlayer1SetsWon(updatedMatch.getPlayer1SetsWon());
                    match.setPlayer2SetsWon(updatedMatch.getPlayer2SetsWon());
                    return matchRepository.save(match);
                })
                .orElseThrow(() -> new RuntimeException("TournamentMatch not found with id " + id));
    }

    public void deleteTournamentMatch(Long id) {
        matchRepository.deleteById(id);
    }
}
