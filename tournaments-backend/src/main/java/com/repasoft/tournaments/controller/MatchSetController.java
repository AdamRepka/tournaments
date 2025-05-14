package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.MatchSetDto;
import com.repasoft.tournaments.mapper.MatchSetMapper;
import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.service.MatchSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matchsets")
public class MatchSetController {

    private final MatchSetService matchSetService;
    private final MatchSetMapper matchSetMapper;

    @Autowired
    public MatchSetController(MatchSetService matchSetService, MatchSetMapper matchSetMapper) {
        this.matchSetService = matchSetService;
        this.matchSetMapper = matchSetMapper;
    }

    @GetMapping
    public ResponseEntity<List<MatchSetDto>> getAllMatchSets() {
        List<MatchSet> matchSets = matchSetService.getAllMatchSets();
        List<MatchSetDto> dtos = matchSets.stream()
                .map(matchSetMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchSetDto> getMatchSetById(@PathVariable Long id) {
        return matchSetService.getMatchSetById(id)
                .map(matchSet -> ResponseEntity.ok(matchSetMapper.toDto(matchSet)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MatchSetDto> createMatchSet(@RequestBody MatchSetDto matchSetDto) {
        MatchSet matchSet = matchSetMapper.toMatchSet(matchSetDto);
        MatchSet saved = matchSetService.createMatchSet(matchSet);
        return ResponseEntity.status(201).body(matchSetMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchSetDto> updateMatchSet(@PathVariable Long id, @RequestBody MatchSetDto matchSetDto) {
        MatchSet matchSet = matchSetMapper.toMatchSet(matchSetDto);
        MatchSet updated = matchSetService.updateMatchSet(id, matchSet);
        return ResponseEntity.ok(matchSetMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatchSet(@PathVariable Long id) {
        matchSetService.deleteMatchSet(id);
        return ResponseEntity.noContent().build();
    }
}
