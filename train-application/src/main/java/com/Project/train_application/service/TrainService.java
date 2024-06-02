package com.Project.train_application.service;

import com.Project.train_application.model.Seat;
import com.Project.train_application.model.Ticket;
import com.Project.train_application.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainService {

    private final Map<String, Ticket> tickets = new HashMap<>();
    private final List<Seat> seats = new ArrayList<>();
    private int nextSeatNumber = 1;
    private final int maxSeats = 100;

    public TrainService() {
        for (int i = 1; i <= maxSeats; i++) {
            String section = (i <= 50) ? "A" : "B";
            seats.add(new Seat(section, i));
        }
    }

    public Ticket purchaseTicket(User user) {
        String email = user.getEmail();
        if (tickets.containsKey(email)) {
            return tickets.get(email);  // User already has a ticket
        }

        if (nextSeatNumber > maxSeats) {
            throw new RuntimeException("No available seats.");
        }

        Seat allocatedSeat = seats.get(nextSeatNumber++ - 1);
        allocatedSeat.setOccupied(true);
        Ticket ticket = new Ticket("London", "France", user, 20.0, allocatedSeat);
        tickets.put(email, ticket);
        return ticket;
    }

    public Ticket getReceipt(String email) {
        return tickets.get(email);
    }

    public List<User> getUsersBySection(String section) {
        List<User> users = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getSeat().getSection().equalsIgnoreCase(section)) {
                users.add(ticket.getUser());
            }
        }
        return users;
    }

    public boolean removeUser(String email) {
        Ticket ticket = tickets.remove(email);
        if (ticket != null) {
            ticket.getSeat().setOccupied(false);
            return true;
        }
        return false;
    }

    public boolean modifySeat(String email, String newSection, int newSeatNumber) {
        Ticket ticket = tickets.get(email);
        if (ticket == null) {
            return false;
        }

        Seat newSeat = findSeat(newSection, newSeatNumber);
        if (newSeat == null || newSeat.isOccupied()) {
            return false;
        }

        ticket.getSeat().setOccupied(false);
        newSeat.setOccupied(true);
        ticket.setSeat(newSeat);
        return true;
    }

    private Seat findSeat(String section, int seatNumber) {
        return seats.stream()
                .filter(seat -> seat.getSection().equalsIgnoreCase(section) && seat.getNumber() == seatNumber)
                .findFirst()
                .orElse(null);
    }
}
