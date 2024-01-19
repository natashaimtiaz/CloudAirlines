package com.example.testing;

//public class TicketDB {
//    private String from;
//    private String to;
//    private String status;
//    private String time;
//    private String flightID;
//
//    // Default constructor required for calls to DataSnapshot.getValue(Ticket.class)
//    public TicketDB() {
//    }
//
//    // Constructor with parameters
//    public TicketDB(String from, String to, String status, String time, String flightID) {
//        this.from = from;
//        this.to = to;
//        this.status = status;
//        this.time = time;
//        this.flightID = flightID;
//    }
//
//    // Getters and Setters
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getFlightID() {
//        return flightID;
//    }
//
//    public void setFlightID(String flightID) {
//        this.flightID = flightID;
//    }
//}

import com.google.firebase.Timestamp;

public class TicketDB {
    private int AvailableSeats;
    private String FlightID;
    private String From;
    private String To;
    private Timestamp Time;
    private String Status;
    private String TicketID;
    private String PassengerID;

    // Default constructor required for calls to DataSnapshot.getValue(TicketDB.class)
    public TicketDB() {
    }

    // Constructor with parameters
    public TicketDB(int availableSeats, String flightID, String from, String to, Timestamp time, String status, String ticketID, String passengerID) {
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

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
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
