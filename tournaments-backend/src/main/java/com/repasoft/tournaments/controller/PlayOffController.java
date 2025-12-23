package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.PlayOffDto;
import com.repasoft.tournaments.dto.PlayOffSetupDto;
import com.repasoft.tournaments.model.PlayOffMatch;
import com.repasoft.tournaments.repository.PlayOffMatchRepository;
import com.repasoft.tournaments.service.PlayOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playoff")
public class PlayOffController {
    private final PlayOffService playOffService;
    private final PlayOffMatchRepository matchRepository;

    @Autowired
    public PlayOffController(PlayOffService playOffService, PlayOffMatchRepository matchRepository) {
        this.playOffService = playOffService;
        this.matchRepository = matchRepository;
    }

    @PostMapping("/setup")
    public ResponseEntity<Void> loadPlayOff(@RequestBody PlayOffSetupDto settingsDto) {
        playOffService.generatePlayOff(settingsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/game")
    public ResponseEntity<List<PlayOffMatch>> getPlayOffMatches() {
        return ResponseEntity.ok(matchRepository.findAll());
    }
}
