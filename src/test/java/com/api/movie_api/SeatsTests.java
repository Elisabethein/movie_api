package com.api.movie_api;

import com.api.movie_api.Entities.Seat;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeatsTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetSeatPlan() throws Exception {
		String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/seatPlan/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		char[][] seatPlan = objectMapper.readValue(responseContent, char[][].class);

		assertEquals(9, seatPlan.length);
		assertEquals(9, seatPlan[0].length);

		for (char[] row : seatPlan) {
			for (char seat : row) {
				assertTrue(seat == 'X' || seat == 'O');
			}
		}
	}

	@Test
	void testSuggestedSeats() throws Exception {
		String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/suggestedSeat/1/2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		List<Seat> suggestedSeats = objectMapper.readValue(responseContent, new TypeReference<>() {});

		assertNotNull(suggestedSeats);
		assertFalse(suggestedSeats.isEmpty());
		assertThat(suggestedSeats.size()).isEqualTo(2);
	}

	@Test
	void getSeatBySessionIdAndRowAndSeat() throws Exception {
		String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/seat/1/6/4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		Seat seat = objectMapper.readValue(responseContent, Seat.class);

		assertNotNull(seat);
		assertEquals(6, seat.getRow());
		assertEquals(4, seat.getSeat());
	}

//	@Test
//	void testBookSeat() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/bookSeat/1/8/6"))
//				.andExpect(MockMvcResultMatchers.status().isOk());
//		Seat seat = objectMapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get("/api/seat/1/8/6"))
//				.andReturn().getResponse().getContentAsString(), Seat.class);
//        assertTrue(seat.isReserved());
//	}

}
