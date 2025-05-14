package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.TournamentMatchDto;
import com.repasoft.tournaments.mapper.TournamentMatchMapper;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.service.TournamentMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class TournamentMatchController {

    private final TournamentMatchService tournamentMatchService;
    private final TournamentMatchMapper tournamentMatchMapper;

    @PostMapping
    public TournamentMatch createMatch(@RequestBody TournamentMatch match) {
        return tournamentMatchService.saveMatch(match);
    }

    @PutMapping("/{id}")
    public TournamentMatch updateMatch(@PathVariable Long id, @RequestBody TournamentMatch match) {
        match.setId(id);
        return tournamentMatchService.saveMatch(match);
    }

    @GetMapping("/{id}")
    public TournamentMatch getMatch(@PathVariable Long id) {
        return tournamentMatchService.getMatch(id);
    }

    @PostMapping("/result")
    public ResponseEntity<Void> saveResult(@RequestBody TournamentMatchDto match) {
        tournamentMatchService.saveMatch(tournamentMatchMapper.toMatch(match));
        return ResponseEntity.ok().build();
    }
}
