package com.repasoft.tournaments.service;

import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.mapper.TournamentGroupMapper;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.model.enums.PlayerCategory;
import com.repasoft.tournaments.model.enums.SkillLevel;
import com.repasoft.tournaments.repository.GroupRepository;
import com.repasoft.tournaments.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentGroupService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentGroupService.class);

    private final GroupRepository groupRepository;
    private final PlayerRepository playerRepository;
    private final TournamentGroupMapper groupMapper;

    public TournamentGroupService(GroupRepository groupRepository,
                                  PlayerRepository playerRepository,
                                  TournamentGroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.playerRepository = playerRepository;
        this.groupMapper = groupMapper;
    }

    public List<TournamentGroup> getAllTournamentGroups() {
        return groupRepository.findAll();
    }

    public TournamentGroup getTournamentGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id " + id));
    }

    public TournamentGroup createTournamentGroup(TournamentGroup tournamentGroup) {
        return groupRepository.save(tournamentGroup);
    }

    public TournamentGroup updateTournamentGroup(Long id, TournamentGroup updatedGroup) {
        TournamentGroup existingGroup = getTournamentGroupById(id);
        existingGroup.setName(updatedGroup.getName());
        existingGroup.setTournament(updatedGroup.getTournament());
        return groupRepository.save(existingGroup);
    }

    public List<TournamentGroupDto> generateGroups(PlayerCategory category, int groupCount, int playersPerGroup) {
        List<Player> players = playerRepository.findByCategory(category);

        if (players.size() < groupCount * playersPerGroup) {
            throw new IllegalArgumentException("Nedostatek hráčů pro požadované skupiny.");
        }

        List<Player> advancedPlayers = players.stream()
                .filter(p -> p.getSkillLevel() == SkillLevel.ADVANCED)
                .collect(Collectors.toList());

        List<Player> otherPlayers = players.stream()
                .filter(p -> p.getSkillLevel() != SkillLevel.ADVANCED)
                .collect(Collectors.toList());

        List<TournamentGroup> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            TournamentGroup group = new TournamentGroup();
            group.setName(category.name() + " skupina " + (i + 1));
            group.setPlayers(new ArrayList<>());
            groups.add(group);
        }

        distributeAdvancedPlayers(groups, advancedPlayers);
        Collections.shuffle(otherPlayers);
        fillGroups(groups, otherPlayers, playersPerGroup);

        // Před uložením nastav zpětnou vazbu hráčům
        for (TournamentGroup group : groups) {
            for (Player player : group.getPlayers()) {
                player.setTournamentGroup(group);
            }
        }

// Ulož všechny skupiny (hráči se uloží díky cascade = CascadeType.ALL)
        groupRepository.saveAll(groups);

        return groups.stream()
                .map(groupMapper::tournamentGroupToTournamentGroupDto)
                .collect(Collectors.toList());
    }

    private void distributeAdvancedPlayers(List<TournamentGroup> groups, List<Player> advancedPlayers) {
        int groupIndex = 0;
        for (Player advanced : advancedPlayers) {
            groups.get(groupIndex).getPlayers().add(advanced);
            groupIndex = (groupIndex + 1) % groups.size();
        }
    }

    private void fillGroups(List<TournamentGroup> groups, List<Player> players, int playersPerGroup) {
        for (Player player : players) {
            boolean placed = false;
            Collections.shuffle(groups);

            for (TournamentGroup group : groups) {
                Set<String> lastNames = group.getPlayers().stream()
                        .map(Player::getLastName)
                        .collect(Collectors.toSet());

                if (group.getPlayers().size() < playersPerGroup &&
                        !lastNames.contains(player.getLastName())) {
                    group.getPlayers().add(player);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                groups.stream()
                        .filter(g -> g.getPlayers().size() < playersPerGroup)
                        .findFirst()
                        .ifPresent(g -> g.getPlayers().add(player));
            }
        }
    }

    @Transactional
    public TournamentGroupDto addPlayerToGroup(Long groupId, PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerDto.getId()));

        TournamentGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + groupId));

        if (group.getPlayers().contains(player)) {
            logger.warn("Player {} already in group {}", player.getId(), groupId);
        } else {
            group.getPlayers().add(player);
            groupRepository.save(group);
        }

        return groupMapper.tournamentGroupToTournamentGroupDto(group);
    }

    @Transactional
    public void saveGroupAssignments(List<TournamentGroupDto> groupDtos) {
        for (TournamentGroupDto dto : groupDtos) {
            TournamentGroup group;

            if (dto.getId() != null) {
                group = groupRepository.findById(dto.getId())
                        .orElseGet(() -> TournamentGroup.builder()
                                .name(dto.getName())
                                .players(new ArrayList<>())
                                .build());
            } else {
                group = TournamentGroup.builder()
                        .name(dto.getName())
                        .players(new ArrayList<>())
                        .build();
            }

            // Smaž současné hráče
            group.getPlayers().clear();

            List<Long> playerIds = dto.getPlayers().stream()
                    .map(PlayerDto::getId)
                    .toList();

            List<Player> players = playerRepository.findAllById(playerIds);

            for (Player player : players) {
                player.setTournamentGroup(group);
            }

            group.getPlayers().addAll(players);

            // Ulož skupinu (uloží i hráče díky cascade = CascadeType.ALL)
            groupRepository.save(group);
        }
    }


    @Transactional
    public void deleteTournamentGroup(Long groupId) {
        TournamentGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + groupId));

        for (Player player : group.getPlayers()) {
            player.setTournamentGroup(null);
        }

        groupRepository.delete(group);
    }

}
