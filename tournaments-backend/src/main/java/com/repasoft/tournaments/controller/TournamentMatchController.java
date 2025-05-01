package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.TournamentMatchDto;
import com.repasoft.tournaments.mapper.TournamentMatchMapper;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.service.TournamentMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class TournamentMatchController {

    private final TournamentMatchService matchService;
    private final TournamentMatchMapper matchMapper = TournamentMatchMapper.INSTANCE;

    @Autowired
    public TournamentMatchController(TournamentMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentMatchDto>> getAllMatches() {
        List<TournamentMatch> matches = matchService.getAllTournamentMatches();
        List<TournamentMatchDto> matchDtos = matches.stream().map(matchMapper::tournamentMatchToTournamentMatchDto).collect(Collectors.toList());
        return ResponseEntity.ok(matchDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentMatchDto> getMatchById(@PathVariable Long id) {
        return matchService.getTournamentMatchById(id)
                .map(match -> ResponseEntity.ok(matchMapper.tournamentMatchToTournamentMatchDto(match)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TournamentMatchDto> createMatch(@RequestBody TournamentMatchDto matchDto) {
        TournamentMatch match = matchMapper.tournamentMatchDtoToTournamentMatch(matchDto);
        TournamentMatch savedMatch = matchService.createTournamentMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchMapper.tournamentMatchToTournamentMatchDto(savedMatch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentMatchDto> updateMatch(@PathVariable Long id, @RequestBody TournamentMatchDto matchDto) {
        TournamentMatch match = matchMapper.tournamentMatchDtoToTournamentMatch(matchDto);
        match.setId(id);
        TournamentMatch updatedMatch = matchService.updateTournamentMatch(id, match);
        return ResponseEntity.ok(matchMapper.tournamentMatchToTournamentMatchDto(updatedMatch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteTournamentMatch(id);
        return ResponseEntity.noContent().build();
    }
}