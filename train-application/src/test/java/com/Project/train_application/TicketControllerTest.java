package com.Project.train_application;

import com.Project.train_application.controller.TicketController;
import com.Project.train_application.model.Seat;
import com.Project.train_application.model.Ticket;
import com.Project.train_application.model.User;
import com.Project.train_application.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainService trainService;

    private User user;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        ticket = new Ticket("London", "France", user, 20.0, new Seat("A", 1));
    }

    @Test
    public void testPurchaseTicket() throws Exception {
        when(trainService.purchaseTicket(any(User.class))).thenReturn(ticket);

        mockMvc.perform(post("/api/purchaseTicket")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("London"))
                .andExpect(jsonPath("$.to").value("France"))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));

        verify(trainService, times(1)).purchaseTicket(any(User.class));
    }

    @Test
    public void testGetReceipt() throws Exception {
        when(trainService.getReceipt(anyString())).thenReturn(ticket);

        mockMvc.perform(get("/api/receipt/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("London"))
                .andExpect(jsonPath("$.to").value("France"))
                .andExpect(jsonPath("$.price").value(20.0))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));

        verify(trainService, times(1)).getReceipt("john.doe@example.com");
    }

    @Test
    public void testGetUsersBySection() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(trainService.getUsersBySection(anyString())).thenReturn(users);

        mockMvc.perform(get("/api/seats/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(trainService, times(1)).getUsersBySection("A");
    }

    @Test
    public void testRemoveUser() throws Exception {
        when(trainService.removeUser(anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/removeUser/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(trainService, times(1)).removeUser("john.doe@example.com");
    }

    @Test
    public void testModifySeat() throws Exception {
        when(trainService.modifySeat(anyString(), anyString(), anyInt())).thenReturn(true);

        mockMvc.perform(put("/api/modifySeat")
                .param("email", "john.doe@example.com")
                .param("newSection", "B")
                .param("newSeatNumber", "51"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(trainService, times(1)).modifySeat("john.doe@example.com", "B", 51);
    }
}
