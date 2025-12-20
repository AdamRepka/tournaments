package com.repasoft.tournaments.controller;

import com.repasoft.tournaments.dto.*;
import com.repasoft.tournaments.mapper.TournamentGroupMapper;
import com.repasoft.tournaments.model.MatchPaper;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.print.MatchPaperPdfGenerator;
import com.repasoft.tournaments.service.TournamentGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class TournamentGroupController {

    private final TournamentGroupService tournamentGroupService;
    private final TournamentGroupMapper tournamentGroupMapper;
    private MatchPaperPdfGenerator pdfGenerator;

    @Autowired
    public TournamentGroupController(TournamentGroupService tournamentGroupService, TournamentGroupMapper tournamentGroupMapper) {
        this.tournamentGroupService = tournamentGroupService;
        this.tournamentGroupMapper = tournamentGroupMapper;
    }

    @GetMapping
    public ResponseEntity<List<TournamentGroupDto>> getAllTournamentGroups() {
        List<TournamentGroup> tournamentGroups = tournamentGroupService.getAllTournamentGroups();
        List<TournamentGroupDto> tournamentGroupDtos = tournamentGroups.stream().map(tournamentGroupMapper::tournamentGroupToTournamentGroupDto).collect(Collectors.toList());
        return ResponseEntity.ok(tournamentGroupDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentGroupDto> getTournamentGroupById(@PathVariable Long id) {
        TournamentGroup group;
        try {
            group = tournamentGroupService.getTournamentGroupById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tournamentGroupMapper.tournamentGroupToTournamentGroupDto(group));
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveGroupAssignments(@RequestBody List<TournamentGroupDto> groups) {
        tournamentGroupService.saveGroupAssignments(groups);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/generate")
    public ResponseEntity<List<TournamentGroupDto>> generateGroups(@RequestBody GroupGenerationRequestDto request) {
        List<TournamentGroupDto> generatedGroups = tournamentGroupService.generateGroups(
                request.getCategory(),
                request.getGroupCount(),
                request.getPlayersPerGroup()
        );
        return ResponseEntity.ok(generatedGroups);
    }

    @PostMapping
    public ResponseEntity<TournamentGroupDto> createTournamentGroup(@RequestBody TournamentGroupDto tournamentGroupDto) {
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

    // TODO to by tady nemelo byt, udelat pres tournament
    @GetMapping("/match-papers")
    public ResponseEntity<byte[]> generateMatchPapers() throws IOException {
        pdfGenerator = new MatchPaperPdfGenerator();
        List<MatchPaper> papers = tournamentGroupService.getAllTournamentGroups().stream()
                .flatMap(group -> group.getMatches().stream()
                        .map(match -> new MatchPaper(match.getPlayer1(), match.getPlayer2(), group, 5)))
                .collect(Collectors.toList());

        byte[] pdf = pdfGenerator.generate(papers);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=match-papers.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}