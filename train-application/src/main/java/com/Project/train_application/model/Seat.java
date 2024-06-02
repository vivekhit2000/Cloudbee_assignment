package com.Project.train_application.model;

public class Seat {
    private String section;
    private int number;
    private boolean isOccupied;

    public Seat(String section, int number) {
        this.section = section;
        this.number = number;
        this.isOccupied = false;
    }

    // Getters and setters
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
}
