package com.example.testing;

public class TicketDB {
    private int AvailableSeats;
    private String FlightID;
    private String From;
    private String To;
    private String Time;
    private String Status;
    private String TicketID;
    private String PassengerID;

    // Default constructor required for calls to DataSnapshot.getValue(TicketDB.class)
    public TicketDB() {
    }

    // Constructor with parameters
    public TicketDB(int availableSeats, String flightID, String from, String to, String time, String status, String ticketID, String passengerID) {
        this.AvailableSeats = availableSeats;
        this.FlightID = flightID;
        this.From = from;
        this.To = to;
        this.Time = time;
        this.Status = status;
        this.TicketID = ticketID;
        this.PassengerID = passengerID;
    }

    // Getters and Setters
    public int getAvailableSeats() {
        return AvailableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        AvailableSeats = availableSeats;
    }

    public String getFlightID() {
        return FlightID;
    }

    public void setFlightID(String flightID) {
        FlightID = flightID;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTicketID() {
        return TicketID;
    }

    public void setTicketID(String ticketID) {
        TicketID = ticketID;
    }

    public String getPassengerID() {
        return PassengerID;
    }

    public void setPassengerID(String passengerID) {
        PassengerID = passengerID;
    }
}
