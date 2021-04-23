package com.srodi.lottery.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LotteryTicketIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private String uri;

    @Test
    void integrationTestsWithMockMVC() throws Exception {

        // getAllTickets - SUCCESS
        mockMvc.perform(get("/ticket"))
                .andDo(print())
                .andExpect(status().isOk());

        // createTicket - SUCCESS
        mockMvc.perform(post("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numberOfLines\": 1}"))
                .andDo(print()).andDo( i -> this.uri = i.getResponse().getHeader("Location"))
                .andExpect(status().is2xxSuccessful());

        // getTicketById - SUCCESS
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());

        // amendTicketById - SUCCESS
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numberOfLines\": 1}"))
                .andExpect(status().is2xxSuccessful());

        // amendTicketById - FAILURE
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numberOfLines\": 0}"))
                .andExpect(status().is4xxClientError());

        // checkTicketStatus - SUCCESS
        String uriStatus = uri.replaceAll("ticket","status");
        mockMvc.perform(put(uriStatus))
                .andExpect(status().is2xxSuccessful());

    }

}