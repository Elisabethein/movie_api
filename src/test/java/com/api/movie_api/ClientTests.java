package com.api.movie_api;

import com.api.movie_api.Entities.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .param("username", "test")
                        .param("password", "Proov_123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Client client = objectMapper.readValue(content, Client.class);
                    assertNotNull(client);
                    assertEquals("test", client.getUsername());
                });
    }

    @Test
    void testUserDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .param("username", "test12345")
                        .param("password", "Proov_123"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

//    @Test
//    void testUserAlreadyExists() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup")
//                        .param("username", "test")
//                        .param("password", "Proov_123"))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
//    }
//
//    @Test
//    void testSignupUser() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/signup")
//                        .param("username", "test2")
//                        .param("password", "Proov_123"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(result -> {
//                    String content = result.getResponse().getContentAsString();
//                    Client client = objectMapper.readValue(content, Client.class);
//                    assertNotNull(client);
//                    assertEquals("test2", client.getUsername());
//                });
//    }
//    @Test
//    void testAddHistory() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/addHistory/1/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
}
