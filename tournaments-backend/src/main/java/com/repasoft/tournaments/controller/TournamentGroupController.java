package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.mapper.TournamentGroupMapper;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.service.TournamentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tournamentGroups")
public class TournamentGroupController {

    private final TournamentGroupService tournamentGroupService;
    private final TournamentGroupMapper tournamentGroupMapper = TournamentGroupMapper.INSTANCE;

    @Autowired
    public TournamentGroupController(TournamentGroupService tournamentGroupService) {
        this.tournamentGroupService = tournamentGroupService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentGroupDto>> getAllTournamentGroups() {
        List<TournamentGroup> tournamentGroups = tournamentGroupService.getAllTournamentGroups();
        List<TournamentGroupDto> tournamentGroupDtos = tournamentGroups.stream().map(tournamentGroupMapper::tournamentGroupToTournamentGroupDto).collect(Collectors.toList());
        return ResponseEntity.ok(tournamentGroupDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentGroupDto> getTournamentGroupById(@PathVariable Long id) {
        return tournamentGroupService.getTournamentGroupById(id)
                .map(tournamentGroup -> ResponseEntity.ok(tournamentGroupMapper.tournamentGroupToTournamentGroupDto(tournamentGroup)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TournamentGroupDto> createtournamentGroup(@RequestBody TournamentGroupDto tournamentGroupDto) {
        TournamentGroup tournamentGroup = tournamentGroupMapper.tournamentGroupDtoToTournamentGroup(tournamentGroupDto);
        TournamentGroup savedtournamentGroup = tournamentGroupService.createTournamentGroup(tournamentGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentGroupMapper.tournamentGroupToTournamentGroupDto(savedtournamentGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentGroupDto> updatetournamentGroup(@PathVariable Long id, @RequestBody TournamentGroupDto tournamentGroupDto) {
        TournamentGroup tournamentGroup = tournamentGroupMapper.tournamentGroupDtoToTournamentGroup(tournamentGroupDto);
        tournamentGroup.setId(id);
        TournamentGroup updatedTournamentGroup = tournamentGroupService.updateTournamentGroup(id, tournamentGroup);
        return ResponseEntity.ok(tournamentGroupMapper.tournamentGroupToTournamentGroupDto(updatedTournamentGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournamentGroup(@PathVariable Long id) {
        tournamentGroupService.deleteTournamentGroup(id);
        return ResponseEntity.noContent().build();
    }
}