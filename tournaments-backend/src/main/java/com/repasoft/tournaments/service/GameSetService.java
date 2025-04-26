package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.GameSet;
import com.repasoft.tournaments.repository.GameSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameSetService {

    private final GameSetRepository gameSetRepository;

    @Autowired
    public GameSetService(GameSetRepository gameSetRepository) {
        this.gameSetRepository = gameSetRepository;
    }

    public List<GameSet> getAllSets() {
        return gameSetRepository.findAll();
    }

    public Optional<GameSet> getSetById(Long id) {
        return gameSetRepository.findById(id);
    }

    public GameSet createSet(GameSet gameSet) {
        return gameSetRepository.save(gameSet);
    }

    public void deleteSet(Long id) {
        gameSetRepository.deleteById(id);
    }
}
