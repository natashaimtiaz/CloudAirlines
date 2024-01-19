package com.example.testing;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private TicketDB[] ticketArray;
    private ImageView imageViewCalendar;
    private TextView textViewDate; // TextView to show the selected date
    // Add a member to hold the PassengerID you want to query
    private String passengerID = "USER01"; // Replace with the actual PassengerID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Check if the user is authenticated
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // User is not logged in, redirect to the login activity
            Intent intent = new Intent(MainPage.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish MainPage so the user can't go back to it without logging in
            return; // Prevent further execution of onCreate
        }
        recyclerView = findViewById(R.id.recyclerView); // ID of your RecyclerView in activity_main.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the array with the expected number of tickets
        // You need to know the size beforehand or handle dynamic resizing
        ticketArray = new TicketDB[10]; // For example, if you expect a maximum of 10 tickets
        ticketAdapter = new TicketAdapter(ticketArray);
        recyclerView.setAdapter(ticketAdapter);

//        fetchTickets();
        // Call the modified fetchTickets method
        fetchTickets(passengerID);
        fetchFlightDetails("FLUFF");

        imageViewCalendar = findViewById(R.id.imageViewCalendar);
        textViewDate = findViewById(R.id.textViewDate); // Assuming you have a TextView to show the date

        imageViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH);
                int day = now.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Format the date and set it to the TextView
                                String formattedDate = String.format(Locale.getDefault(), "%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                                textViewDate.setText(formattedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    // Modified fetchTickets method to fetch based on PassengerID
    private void fetchTickets(String passengerID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ticket")
                .whereEqualTo("PassengerID", passengerID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (index < ticketArray.length) {
                                TicketDB ticket = document.toObject(TicketDB.class);
                                Log.d("MainPage", "Fetched TicketID: " + ticket.getTicketID() +
                                        " FlightID: " + ticket.getFlightID() +
                                        " Status: " + ticket.getStatus());
                                ticketArray[index] = ticket;
                                index++;
                            } else {
                                Log.w("MainPage", "Array is not large enough to hold all tickets");
                                break;
                            }
                        }
                        ticketAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("MainPage", "Error getting documents.", task.getException());
                    }
                });
    }

    private void fetchFlightDetails(String flightID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Flight")
                .whereEqualTo("FlightID", flightID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Handle the Timestamp here
                            Timestamp time = document.getTimestamp("Time");
                            if (time != null) {
                                // Format to a readable date string if needed
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedDate = dateFormat.format(time.toDate());
                                Log.d("MainPage", "Fetched Flight Details: From - " + document.getString("From") +
                                        ", To - " + document.getString("To") +
                                        ", Time - " + formattedDate); // Use the formatted date
                            }
                        }
                    } else {
                        Log.w("MainPage", "Error getting flight details.", task.getException());
                    }
                });
    }


}

    class ViewTicketStatus {
    private int confirmedCount = 0; // Counter for confirmed tickets
    private Ticket[] confirmedTickets = new Ticket[10]; // Example size
    private Queue waitingList = new Queue(10); // Example size
    public void vts(Queue waitingList, Ticket[] confirmedTickets) {
        //Display confirmed tickets
        System.out.println("Confirmed Tickets: ");
        for(Ticket ticket : confirmedTickets){
            if (ticket != null) {
                System.out.println("Passenger: " + ticket.passenger.name + ", Passport: " + ticket.passenger.passportNumber);
            }
        }
        // Display waiting list
        System.out.println("\nWaiting List:");
        Ticket[] waitingArray = waitingList.getQueueArray();
        for (Ticket ticket : waitingArray) {
            if (ticket != null && ticket.passenger != null) {
                System.out.println("Passenger: " + ticket.passenger.name + ", Passport: " + ticket.passenger.passportNumber);
            }
        }
    }
    public void cancelTicket(int ticketId) {
        boolean found = false;
        for (int i = 0; i < confirmedCount; i++) {
            if (confirmedTickets[i] != null && confirmedTickets[i].getTicketId() == ticketId) {
                System.arraycopy(confirmedTickets, i + 1, confirmedTickets, i, confirmedCount - i - 1);
                confirmedTickets[confirmedCount - 1] = null;
                confirmedCount--;
                found = true;
                System.out.println("Ticket cancelled successfully. Ticket ID: " + ticketId);

                Ticket nextTicket = waitingList.dequeue();
                if (nextTicket != null) {
                    confirmedTickets[confirmedCount] = nextTicket;
                    confirmedCount++;
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Ticket ID not found: " + ticketId);
        }
    }
}


