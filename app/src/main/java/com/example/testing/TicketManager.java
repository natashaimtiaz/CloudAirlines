package com.example.testing;

public class TicketManager {
    private Queue waitingListQueue; // Queue for waiting list tickets

    public TicketManager(int queueSize) {
        waitingListQueue = new Queue(queueSize); // Initialize the queue with the specified size
    }

    public void enqueueTickets(TicketDB[] tickets) {
        for (TicketDB ticket : tickets) {
            if (ticket != null) {
                // Assuming you have a method to convert TicketDB object to Ticket object
                Ticket convertedTicket = convertTicketDBToTicket(ticket);
                waitingListQueue.enqueue(convertedTicket);
                System.out.println("Ticket added to waiting list: " + ticket.getTicketID());
            }
        }
    }

    private Ticket convertTicketDBToTicket(TicketDB ticketDB) {
        // Convert TicketDB object to Ticket object
        // This is a basic conversion. You need to adjust the fields as per your requirements.
        return new Ticket(ticketDB.getTicketID(), ticketDB.getFlightID(), ticketDB.getPassengerID(), ticketDB.getStatus());
    }


    // Dequeue a ticket and return the ID of the dequeued ticket from the waiting list
    public String dequeueTicketAndGetId() {
        if (!waitingListQueue.isEmpty()) {
            Ticket dequeuedTicket = waitingListQueue.dequeue(); // Dequeue the next ticket
            if (dequeuedTicket != null) {
                System.out.println("Ticket moved from waiting list to confirmed: " + dequeuedTicket.getTicketId());
                return dequeuedTicket.getTicketId(); // Return the ID of the dequeued ticket
            }
        }
        System.out.println("No ticket to dequeue from waiting list.");
        return null; // No ticket was dequeued
    }

    // Other methods...
}
