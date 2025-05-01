package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.service.PlayerService;
import com.repasoft.tournaments.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        List<PlayerDto> playerDtos = players.stream()
                .map(playerMapper::playerToPlayerDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok(playerMapper.playerToPlayerDto(player)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        Player player = playerMapper.playerDtoToPlayer(playerDto);
        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.playerToPlayerDto(savedPlayer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayer(@PathVariable Long id, @RequestBody PlayerDto playerDto) {
        Player player = playerMapper.playerDtoToPlayer(playerDto);
        player.setId(id);
        Player updatedPlayer = playerService.updatePlayer(id, player);
        return ResponseEntity.ok(playerMapper.playerToPlayerDto(updatedPlayer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
