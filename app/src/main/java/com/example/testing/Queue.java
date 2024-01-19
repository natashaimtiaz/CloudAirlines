package com.example.testing;

class Passenger {
    String name;
    String passportNumber;

    Passenger(String name, String passportNumber) {
        this.name = name;
        this.passportNumber = passportNumber;
    }
}

class Ticket {
    int nextTicketId = 1; // Static counter for generating ticket ID
    Passenger passenger;
    boolean isBooked;
    int ticketId;

    Ticket(Passenger passenger) {
        this.passenger = passenger;
        this.isBooked = true;
        this.ticketId = nextTicketId++;
    }
    public int getTicketId() {
        return ticketId;
    }

}

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