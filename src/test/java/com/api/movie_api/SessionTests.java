package com.api.movie_api;

import com.api.movie_api.Entities.Session;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSessions() throws Exception {
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/allSessions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Session> sessions = objectMapper.readValue(responseContent, new TypeReference<List<Session>>() {});

        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }

    @Test
    void getSessionById() throws Exception {
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Session session = objectMapper.readValue(responseContent, Session.class);

        assertNotNull(session);
        assertEquals(1, session.getId());
    }

    @Test
    void testGettingFilteredSessions() throws Exception {
        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/filteredSessions")
                        .param("genre", "Action")
                        .param("language", "English")
                        .param("time", "18:00"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Session> sessions = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertNotNull(sessions);
        assertFalse(sessions.isEmpty());
    }
}
