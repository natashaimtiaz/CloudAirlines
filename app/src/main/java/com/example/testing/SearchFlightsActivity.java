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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchFlightsActivity extends AppCompatActivity {
    private ImageButton homeButton;
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private Button searchButton;
    private ListView flightListView;

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

            // Query Firebase for flights in the specified date range
            databaseReference.orderByChild("flightDate").startAt(fromDate).endAt(toDate)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                flightList.add(snapshot.getValue().toString());
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                            Log.e("FirebaseError", "Database error: " + databaseError.getMessage());

                            // Display an error message to the user if needed
                            Toast.makeText(SearchFlightsActivity.this, "Error retrieving flight data. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
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