package com.example.testing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainPage extends AppCompatActivity implements TicketAdapter.TicketItemListener {
    private RecyclerView recyclerView;
    private TicketAdapter ticketAdapter;
    private TicketDB[] ticketArray;
    private String passengerID = "USER01"; // Replace with the actual PassengerID
    private Spinner spinner;
    private TicketManager ticketManager;
    private ImageButton exitButton;
    private Button buttonNext;
    private String userEmail, FROMValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        spinner = findViewById(R.id.spinnerFrom);

        // Create an ArrayAdapter with a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

        // Add your fixed destination directly
        adapter.add("KUL, MY");

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Notify the adapter that the data has changed so the spinner can update
        adapter.notifyDataSetChanged(); // Get the intent that started this activity


        recyclerView = findViewById(R.id.recyclerView); // ID of your RecyclerView in activity_main.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the array with the expected number of tickets
        // You need to know the size beforehand or handle dynamic resizing
        ticketArray = new TicketDB[8]; // For example, if you expect a maximum of 10 tickets

        ticketAdapter = new TicketAdapter(ticketArray, this);
        recyclerView.setAdapter(ticketAdapter);

        Intent intent2 = getIntent();
        userEmail = intent2.getStringExtra("USER_EMAIL");

        fetchTickets(userEmail);


        ticketManager = new TicketManager(10);

        //item selected on Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Here, 'parent' is the spinner
                // 'position' is the index of the selected item
                FROMValue = parent.getItemAtPosition(position).toString();

                // Now 'selectedValue' contains the current value of the spinner
                // You can store it in a string or use it as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            // Start the SignUpActivity
            Intent intent = new Intent(MainPage.this, LoginActivity.class);
            startActivity(intent);
            //onDestroy();
        });

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(v -> {
            // Start the SignUpActivity
            Intent intent = new Intent(MainPage.this, SearchFlightsActivity.class);

            //send FROM to SearchFlightActivity
            intent.putExtra("FROM", FROMValue);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
            //onDestroy();
        });
        
    }

    // Modified fetchTickets method to fetch based on PassengerID
    private void fetchTickets(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Ticket")
                .whereEqualTo("email", email)
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
                                fetchFlightDetails(ticket.getFlightID(), index);
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

    private void fetchFlightDetails(String flightID, int index) {
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
                                ticketArray[index].setFrom(document.getString("From"));
                                ticketArray[index].setTo(document.getString("To"));
                                ticketArray[index].setTime(formattedDate);
                            }
                        }
                        ticketAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("MainPage", "Error getting flight details.", task.getException());
                    }
                });
    }

    @Override
    public void onCancelTicket(TicketDB ticket, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Ticket")
                .setMessage("Are you sure you want to cancel this ticket?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Start a query on the "Flight" collection
                    Query flightQuery = db.collection("Flight").whereEqualTo("FlightID", ticket.getFlightID());

                    flightQuery.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Assuming FlightID is unique and only one document is expected
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            if (document.exists()) {
                                int availableSeats = document.getLong("AvailableSeats").intValue();
                                Log.d("Available Seat: ", String.valueOf(availableSeats));
                                // Get a DocumentReference to the specific flight
                                DocumentReference flightRef = document.getReference();

                                if (availableSeats == 0) {
                                    // Handle the waiting list logic
                                    handleWaitingList(flightRef, ticket, position);
                                    ticketAdapter.notifyDataSetChanged();
                                } else {
                                    // Directly delete the user that wants to cancel
                                    deleteTicket(ticket, position);

                                    // Update available seats
                                    flightRef.update("AvailableSeats", FieldValue.increment(1));
                                }
                            } else {
                                Log.d("MainPage", "No such flight");
                            }
                        } else {
                            Log.d("MainPage", "Failed to get flight details", task.getException());
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void handleWaitingList(DocumentReference flightRef, TicketDB ticket, int position) {
        Log.d("Handle Waiting", "Inside waiting");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetching all tickets on the waiting list for the specific flight
        db.collection("Ticket")
                .whereEqualTo("FlightID", ticket.getFlightID())
                .whereEqualTo("Status", "Waiting List")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            TicketDB[] tempData = new TicketDB[querySnapshot.size()];
                            int index = 0;
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                tempData[index++] = document.toObject(TicketDB.class);
                            }
                            ticketManager.enqueueTickets(tempData);
                            String dequeuedTicketId = ticketManager.dequeueTicketAndGetId();

                            Log.d("Dequeue ID", dequeuedTicketId);

                            updateTicketStatus(dequeuedTicketId, "Confirmed");
                        } else {
                            Log.d("MainPage", "No tickets on the waiting list for this flight");
                        }

                        // After handling the waiting list, delete the original ticket
                        deleteTicket(ticket, position);
                    } else {
                        Log.w("MainPage", "Error getting waiting list", task.getException());
                    }
                });
    }

//    private void queueWaitingList(QuerySnapshot querySnapshot) {
//        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
//            String waitingListTicketId = document.getString("TicketID");
//
//            // Update the status of each ticket on the waiting list
//            updateTicketStatus(waitingListTicketId, "Confirmed");
//
//            // Log the update for each ticket
//            Log.d("MainPage", "Ticket updated to 'Confirmed': " + waitingListTicketId);
//        }
//
//        // Optionally, notify the adapter if the UI needs to reflect these changes immediately
//        // ticketAdapter.notifyDataSetChanged();
//    }


    private void updateTicketStatus(String ticketId, String newStatus) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("Inside Update", "Inside Update");
        // Query the collection to find documents with the matching TicketID field
        db.collection("Ticket")
                .whereEqualTo("TicketID", ticketId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Log.d("Inside Update", "Inside For Update");

                        if (documentSnapshot.exists()) {
                            Log.d("Inside Update", "Inside if Update");
                            db.collection("Ticket").document(documentSnapshot.getId())
                                    .update("Status", newStatus) // Assuming you are updating the "Status" field
                                    .addOnSuccessListener(aVoid -> Log.d("MainPage", "Ticket status updated successfully: " + documentSnapshot.getId()))
                                    .addOnFailureListener(e -> Log.w("MainPage", "Error updating ticket status: " + documentSnapshot.getId(), e));
                            // Update local data source
                            updateLocalTicketArray(ticketId, newStatus);
                        }
                    }
                    ticketAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.w("MainPage", "Error finding tickets to update", e));
    }

    private void updateLocalTicketArray(String ticketId, String newStatus) {
        for (int i = 0; i < ticketArray.length; i++) {
            if (ticketArray[i] != null && ticketArray[i].getTicketID().equals(ticketId)) {
                // Assuming TicketDB has a method to set status
                ticketArray[i].setStatus(newStatus);
                ticketAdapter.notifyItemChanged(i);
                break;
            }
        }
    }


    private void deleteTicket(TicketDB ticket, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query to find the document with the matching TicketID field
        db.collection("Ticket")
                .whereEqualTo("TicketID", ticket.getTicketID())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Loop through the query results and delete each document
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            db.collection("Ticket").document(documentSnapshot.getId()).delete()
                                    .addOnSuccessListener(aVoid -> Log.d("MainPage", "Ticket deleted successfully: " + documentSnapshot.getId()))
                                    .addOnFailureListener(e -> Log.w("MainPage", "Error deleting ticket: " + documentSnapshot.getId(), e));
                        }
                    }

                    // Update the ticketArray and notify the adapter
                    ticketArray[position] = null;
                    ticketAdapter.notifyItemRemoved(position);
                    //ticketAdapter.notifyItemRangeChanged(position, ticketArray.length - position);
                })
                .addOnFailureListener(e -> Log.w("MainPage", "Error finding ticket to delete", e));
    }
}


//    class ViewTicketStatus {
//        private int confirmedCount = 0; // Counter for confirmed tickets
//        private Ticket[] confirmedTickets = new Ticket[10]; // Example size
//        private Queue waitingList = new Queue(10); // Example size
//
//        public void vts(Queue waitingList, Ticket[] confirmedTickets) {
//            //Display confirmed tickets
//            System.out.println("Confirmed Tickets: ");
//            for (Ticket ticket : confirmedTickets) {
//                if (ticket != null) {
//                    System.out.println("Passenger: " + ticket.passenger.name + ", Passport: " + ticket.passenger.passportNumber);
//                }
//            }
//            // Display waiting list
//            System.out.println("\nWaiting List:");
//            Ticket[] waitingArray = waitingList.getQueueArray();
//            for (Ticket ticket : waitingArray) {
//                if (ticket != null && ticket.passenger != null) {
//                    System.out.println("Passenger: " + ticket.passenger.name + ", Passport: " + ticket.passenger.passportNumber);
//                }
//            }
//        }
//
//        public void cancelTicket(int ticketId) {
//            boolean found = false;
//            for (int i = 0; i < confirmedCount; i++) {
//                if (confirmedTickets[i] != null && confirmedTickets[i].getTicketId() == ticketId) {
//                    System.arraycopy(confirmedTickets, i + 1, confirmedTickets, i, confirmedCount - i - 1);
//                    confirmedTickets[confirmedCount - 1] = null;
//                    confirmedCount--;
//                    found = true;
//                    System.out.println("Ticket cancelled successfully. Ticket ID: " + ticketId);
//
//                    Ticket nextTicket = waitingList.dequeue();
//                    if (nextTicket != null) {
//                        confirmedTickets[confirmedCount] = nextTicket;
//                        confirmedCount++;
//                    }
//                    break;
//                }
//            }
//
//            if (!found) {
//                System.out.println("Ticket ID not found: " + ticketId);
//            }
//        }
//    }
//}
//
//
