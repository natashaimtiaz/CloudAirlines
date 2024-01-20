package com.example.testing;

class Passenger {
    String name;
    String passportNumber;

    Passenger(String name, String passportNumber) {
        this.name = name;
        this.passportNumber = passportNumber;
    }
}

//class Ticket {
//    int nextTicketId = 1; // Static counter for generating ticket ID
//    Passenger passenger;
//    boolean isBooked;
//    int ticketId;
//
//    Ticket(Passenger passenger) {
//        this.passenger = passenger;
//        this.isBooked = true;
//        this.ticketId = nextTicketId++;
//    }
//    public int getTicketId() {
//        return ticketId;
//    }
//
//}


public class Queue {
    private int size;
    private Ticket[] queue;
    private int front;
    private int rear;

    Queue(int size) {
        this.size = size;
        this.queue = new Ticket[size];
        for (int i = 0; i < size; i++){
            queue[i]= null;
            this.front = this.rear = -1;
        }
    }

    // Enqueue operation
    public void enqueue(Ticket ticket) {
        if (isFull()) {
            System.out.println("Queue is full. Cannot enqueue.");
            return;
        }

        if (isEmpty()) {
            front = rear = 0;
        } else {
            rear = (rear + 1) % size;
        }

        queue[rear] = ticket;
    }

    // Dequeue operation
    public Ticket dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return null;
        }

        Ticket removedTicket = queue[front];
        queue[front] = null;

        if (front == rear) {
            front = rear = -1; // Reset front and rear if the last element is dequeued
        } else {
            front = (front + 1) % size;
        }

        return removedTicket;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return front == -1 && rear == -1;
    }

    // Check if the queue is full
    public boolean isFull() {
        return (rear + 1) % size == front;
    }

    public Ticket[] getQueueArray() {
        return queue;
    }
}


class Ticket {
    private String ticketId;
    private String flightId;
    private String passengerName; // Assuming passenger name is required
    private String status; // For example, "Confirmed", "Waiting List", etc.

    // Constructor
    public Ticket(String ticketId, String flightId, String passengerName, String status) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.status = status;
    }

    // Getter and Setter for ticketId
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    // Getter and Setter for flightId
    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    // Getter and Setter for passengerName
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Optional: ToString method for easy printing
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", flightId='" + flightId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }



    // Other methods can be added as per requirements...
}



