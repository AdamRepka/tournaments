package com.repasoft.tournaments.service;

import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentGroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public TournamentGroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<TournamentGroup> getAllTournamentGroups() {
        return groupRepository.findAll();
    }

    public Optional<TournamentGroup> getTournamentGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public TournamentGroup createTournamentGroup(TournamentGroup tournamentGroup) {
        return groupRepository.save(tournamentGroup);
    }

    public TournamentGroup updateTournamentGroup(Long id, TournamentGroup updatedTournamentGroup) {
        return groupRepository.findById(id)
                .map(group -> {
                    group.setName(updatedTournamentGroup.getName());
                    group.setTournament(updatedTournamentGroup.getTournament());
                    return groupRepository.save(group);
                })
                .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    }

    public void deleteTournamentGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
