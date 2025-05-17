package com.repasoft.tournaments.service;

import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.dto.TournamentGroupDto;
import com.repasoft.tournaments.mapper.TournamentGroupMapper;
import com.repasoft.tournaments.model.MatchSet;
import com.repasoft.tournaments.model.Player;
import com.repasoft.tournaments.model.TournamentGroup;
import com.repasoft.tournaments.model.TournamentMatch;
import com.repasoft.tournaments.model.enums.PlayerCategory;
import com.repasoft.tournaments.model.enums.SkillLevel;
import com.repasoft.tournaments.repository.GroupRepository;
import com.repasoft.tournaments.repository.MatchRepository;
import com.repasoft.tournaments.repository.MatchSetRepository;
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
    private final MatchRepository matchRepository;
    private final MatchSetRepository matchSetRepository;

    public TournamentGroupService(GroupRepository groupRepository,
                                  PlayerRepository playerRepository,
                                  TournamentGroupMapper groupMapper,
                                  MatchRepository matchRepository,
                                  MatchSetRepository matchSetRepository) {
        this.groupRepository = groupRepository;
        this.playerRepository = playerRepository;
        this.groupMapper = groupMapper;
        this.matchRepository = matchRepository;
        this.matchSetRepository = matchSetRepository;
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
            group.setPlayers(new HashSet<>());
            groups.add(group);
        }

        distributeAdvancedPlayers(groups, advancedPlayers);
        Collections.shuffle(otherPlayers);
        fillGroups(groups, otherPlayers, playersPerGroup);
        groupRepository.saveAll(groups);
        fillMatches(groups);

        // Před uložením nastav zpětnou vazbu hráčům
        for (TournamentGroup group : groups) {
            for (Player player : group.getPlayers()) {
                player.setTournamentGroup(group);
            }
        }

// Ulož všechny skupiny (hráči se uloží díky cascade = CascadeType.ALL)
        //groupRepository.saveAll(groups);

        return groups.stream()
                .map(groupMapper::tournamentGroupToTournamentGroupDto)
                .collect(Collectors.toList());
    }

    private void fillMatches(List<TournamentGroup> groups) {
        groups.forEach(group -> {
            Set<TournamentMatch> groupMatches = new HashSet<>();
            for (int i = 0; i < group.getPlayers().size(); i++) {
                for (int j = 0; j < group.getPlayers().size(); j++) {
                    if (i == j) continue;
                    List<MatchSet> matchSets = new ArrayList<>();
                    TournamentMatch match = TournamentMatch.builder()
                            .tournamentGroup(group)
                            .finished(false)
                            .player1(group.getPlayers().stream().toList().get(i))
                            .player2(group.getPlayers().stream().toList().get(j))
                            .build();
                    matchSets.add(MatchSet.builder()
                            .match(match)
                            .build());
                    matchSets.add(MatchSet.builder()
                            .match(match)
                            .build());
                    match.setMatchSets(matchSets);
                    groupMatches.add(match);
                    matchRepository.save(match);
                    matchSetRepository.saveAll(matchSets);
                }
            }
            group.setMatches(groupMatches);
        });
    }

    private void distributeAdvancedPlayers(List<TournamentGroup> groups, List<Player> advancedPlayers) {
        int groupIndex = 0;
        for (Player advanced : advancedPlayers) {
            advanced.setGroupIndex(groups.get(groupIndex).getPlayers().size());
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
                    player.setGroupIndex(group.getPlayers().size());
                    group.getPlayers().add(player);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                groups.stream()
                        .filter(g -> g.getPlayers().size() < playersPerGroup)
                        .findFirst()
                        .ifPresent(g -> {
                            player.setGroupIndex(g.getPlayers().size());
                            g.getPlayers().add(player);
                        });
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
                                .players(new HashSet<>())
                                .build());
            } else {
                group = TournamentGroup.builder()
                        .name(dto.getName())
                        .players(new HashSet<>())
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


    public void deleteTournamentGroup(Long groupId) {
        TournamentGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        // Nastavíme všem hráčům group = null
        for (Player player : group.getPlayers()) {
            player.setTournamentGroup(null);
        }
        playerRepository.saveAll(group.getPlayers());

        // A pak smažeme skupinu
        groupRepository.delete(group);
    }


}
