package com.Project.train_application.controller;

import com.Project.train_application.model.Ticket;
import com.Project.train_application.model.User;
import com.Project.train_application.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TrainService trainService;

    @PostMapping("/purchaseTicket")
    public Ticket purchaseTicket(@RequestBody User user) {
        return trainService.purchaseTicket(user);
    }

    @GetMapping("/receipt/{email}")
    public Ticket getReceipt(@PathVariable String email) {
        return trainService.getReceipt(email);
    }

    @GetMapping("/seats/{section}")
    public List<User> getUsersBySection(@PathVariable String section) {
        return trainService.getUsersBySection(section);
    }

    @DeleteMapping("/removeUser/{email}")
    public boolean removeUser(@PathVariable String email) {
        return trainService.removeUser(email);
    }

    @PutMapping("/modifySeat")
    public boolean modifySeat(@RequestParam String email, @RequestParam String newSection, @RequestParam int newSeatNumber) {
        return trainService.modifySeat(email, newSection, newSeatNumber);
    }
}
