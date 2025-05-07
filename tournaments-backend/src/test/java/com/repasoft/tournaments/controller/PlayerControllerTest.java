package com.repasoft.tournaments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repasoft.tournaments.dto.PlayerDto;
import com.repasoft.tournaments.service.PlayerService;
import com.repasoft.tournaments.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.repasoft.tournaments.model.enums.PlayerCategory.SENIOR;
import static com.repasoft.tournaments.model.enums.SkillLevel.ADVANCED;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

public class PlayerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }


   /* public void testGetAllPlayers() throws Exception {
        // Mockováno, že vracíme seznam hráčů s ID 1
        when(playerService.getAllPlayers()).thenReturn(List.of(new Player(1L, "John", "Doe", SENIOR, ADVANCED, "1234", true, null, null)));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))  // Ověříme, že id není null
                .andExpect(jsonPath("$.size()").value(1));  // Ověříme, že seznam má jeden prvek
    }

    @Test
    public void testCreatePlayer() throws Exception {
        // Vytvoření PlayerDto s požadovanými hodnotami
        PlayerDto playerDto = new PlayerDto(1L, "John", "Doe", SENIOR, ADVANCED, "1234", true, null, null, null, null, null, null, null ,null);

        // Mockování, že service metoda vrátí vytvořeného hráče s ID
        when(playerService.createPlayer(any(Player.class))).thenReturn(new Player(1L, "John", "Doe", SENIOR, ADVANCED, "1234", true, null, null));

        // Perform testu pro vytvoření hráče
        mockMvc.perform(post("/api/players")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(playerDto)))
                .andExpect(status().isCreated())  // Ověříme, že odpověď má status 201 (Created)
                .andExpect(jsonPath("$.id").value(1L));  // Ověříme, že ID hráče je 1
    }

    @Test
    public void testUpdatePlayer() throws Exception {
        // Vytvoření PlayerDto pro update s ID 1
        PlayerDto playerDto = new PlayerDto(1L, "John", "Doe", SENIOR, ADVANCED, "1234", true, null, null, null, null, null, null, null, null);

        // Mockování, že metoda pro update vrátí aktualizovaného hráče
        when(playerService.updatePlayer(eq(1L), any(Player.class))).thenReturn(new Player(1L, "John", "Doe", SENIOR, ADVANCED, "1234", true, null, null));

        // Perform testu pro update hráče
        mockMvc.perform(put("/api/players/{id}", 1L)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(playerDto)))
                .andExpect(status().isOk())  // Ověříme, že odpověď má status 200 (OK)
                .andExpect(jsonPath("$.id").value(1L));  // Ověříme, že ID hráče je 1
    }

    @Test
    public void testDeletePlayer() throws Exception {
        // Mockování metody deletePlayer, že nebude nic vracet (jen provede akci)
        doNothing().when(playerService).deletePlayer(1L);

        // Perform testu pro smazání hráče
        mockMvc.perform(delete("/api/players/{id}", 1L))
                .andExpect(status().isNoContent());  // Ověříme, že odpověď je 204 (No Content)
    }*/
}
