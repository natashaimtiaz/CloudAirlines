package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class SearchFlightsActivity extends AppCompatActivity {
    private ImageButton homeButton;
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private Button searchButton;
    private ListView flightListView;
    private FirebaseAuth auth;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);

        // Initialize UI components
        homeButton = findViewById(R.id.homeButton);
        fromDateEditText = findViewById(R.id.fromDate);
        toDateEditText = findViewById(R.id.toDate);
        searchButton = findViewById(R.id.searchButton);
        flightListView = findViewById(R.id.flightList);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("flights");

        ArrayList<String> flightList = new ArrayList<>();

        // Initialize ArrayAdapter for ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightList);
        flightListView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            // Retrieve fromDate and toDate from EditText widgets
            String fromDate = fromDateEditText.getText().toString();
            String toDate = toDateEditText.getText().toString();

            auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Flight")
                    .whereGreaterThanOrEqualTo("Date", fromDate)
                    .whereLessThanOrEqualTo("Date", toDate)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract specific fields from the document
                                String flightID = document.getString("FlightID");
                                String flightDate = document.getString("Date");
                                String flightTime = document.getString("Time");
                                Long availableSeats = document.getLong("availableSeats");

                                // Format the flight information for display
                                String displayText = "Date: " + flightDate +
                                        "\nTime: " + flightTime +
                                        "\nAvailable Seats: " + availableSeats;

                                // Add the formatted string to the list
                                flightList.add(displayText);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("MainPage", "Error getting documents.", task.getException());
                        }
                    });
            flightListView.setOnItemClickListener((parent, view, position, id) -> {
                // Get the selected flight information from the clicked item
                String selectedFlightInfo = flightList.get(position);

//                // Start the BookFlightActivity and pass relevant data
//                Intent bookingIntent = new Intent(MainActivity.this, BookFlightActivity.class);
//                startActivity(bookingIntent);
            });
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                Intent intent = new Intent(SearchFlightsActivity.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}