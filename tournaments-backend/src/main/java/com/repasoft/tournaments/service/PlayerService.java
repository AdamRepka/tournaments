package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(player -> {
                    player.setFirstName(updatedPlayer.getFirstName());
                    player.setLastName(updatedPlayer.getLastName());
                    player.setCategory(updatedPlayer.getCategory());
                    player.setSkillLevel(updatedPlayer.getSkillLevel());
                    player.setEntryFeePaid(updatedPlayer.isEntryFeePaid());
                    // Statistiky, pokud budou editovatelné, se mohou také aktualizovat tady
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new RuntimeException("Player not found with id " + id));
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
