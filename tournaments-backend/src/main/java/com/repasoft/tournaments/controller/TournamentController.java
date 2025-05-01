package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.TournamentDto;
import com.repasoft.tournaments.mapper.TournamentMapper;
import com.repasoft.tournaments.model.Tournament;
import com.repasoft.tournaments.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper = TournamentMapper.INSTANCE;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentDto>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        List<TournamentDto> tournamentDtos = tournaments.stream().map(tournamentMapper::tournamentToTournamentDto).collect(Collectors.toList());
        return ResponseEntity.ok(tournamentDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(tournament -> ResponseEntity.ok(tournamentMapper.tournamentToTournamentDto(tournament)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TournamentDto> createTournament(@RequestBody TournamentDto tournamentDto) {
        Tournament tournament = tournamentMapper.tournamentDtoToTournament(tournamentDto);
        Tournament savedTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentMapper.tournamentToTournamentDto(savedTournament));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentDto> updateTournament(@PathVariable Long id, @RequestBody TournamentDto tournamentDto) {
        Tournament tournament = tournamentMapper.tournamentDtoToTournament(tournamentDto);
        tournament.setId(id);
        Tournament updatedTournament = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(tournamentMapper.tournamentToTournamentDto(updatedTournament));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}