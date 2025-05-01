package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.repository.MatchSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchSetService {

    private final MatchSetRepository matchSetRepository;

    @Autowired
    public MatchSetService(MatchSetRepository matchSetRepository) {
        this.matchSetRepository = matchSetRepository;
    }

    public List<MatchSet> getAllMatchSets() {
        return matchSetRepository.findAll();
    }

    public Optional<MatchSet> getMatchSetById(Long id) {
        return matchSetRepository.findById(id);
    }

    public MatchSet createMatchSet(MatchSet matchSet) {
        return matchSetRepository.save(matchSet);
    }

    public MatchSet updateMatchSet(Long id, MatchSet matchSet) {
        matchSetRepository.findById(id)
                .map(set -> {
                    set.setMatch(matchSet.getMatch());
                    set.setPlayer1Points(matchSet.getPlayer1Points());
                    set.setPlayer2Points(matchSet.getPlayer2Points());
                    return matchSetRepository.save(set);
                }).orElseThrow(() -> new RuntimeException("MatchSet not found with id " + id));
        return matchSet;
    }

    public void deleteMatchSet(Long id) {
        matchSetRepository.deleteById(id);
    }
}
