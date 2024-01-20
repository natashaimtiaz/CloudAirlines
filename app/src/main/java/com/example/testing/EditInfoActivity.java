package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditInfoActivity extends AppCompatActivity {
    private ImageButton homeButton;
    private EditText fullNameEditText;

    private EditText icNumberEditText;

    private EditText passportNumberEditText;

    private EditText contactNumberEditText;

    private Button cancelButton;
    private Button confirmButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        homeButton = findViewById(R.id.homeButton);
        fullNameEditText = findViewById(R.id.fullName);
        icNumberEditText = findViewById(R.id.icNumber);
        passportNumberEditText = findViewById(R.id.passportNumber);
        contactNumberEditText = findViewById(R.id.contactNumber);
        cancelButton = findViewById(R.id.cancelButton);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String icNumber = icNumberEditText.getText().toString().trim();
            String passportNumber = passportNumberEditText.getText().toString().trim();
            String contactNumber = contactNumberEditText.getText().toString().trim();

            // Get the current user's ID
            String userId = auth.getCurrentUser().getUid();

            // Create a UserDetails object with the entered details
            UserDetails userDetails = new UserDetails(fullName, icNumber, passportNumber, contactNumber);

            // Save the user details to Firestore
            db.collection("UserDetails").document(userId).update("fullName", fullName,
                            "icNumber", icNumber,
                            "passportNumber", passportNumber,
                            "contactNumber", contactNumber)
                    .addOnSuccessListener(aVoid -> {
                        // Successful update
                        // show a Toast and then navigate to another activity
                        Toast.makeText(EditInfoActivity.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditInfoActivity.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failures, show a Toast or log the error
                        Toast.makeText(EditInfoActivity.this, "Failed to update details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                Intent intent = new Intent(EditInfoActivity.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                Intent intent = new Intent(EditInfoActivity.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

    class UserDetails {
        public String fullName;
        public String icNumber;
        public String passportNumber;
        public String contactNumber;

        public UserDetails() {
            // Default constructor required for Firestore
        }

        public UserDetails(String fullName, String icNumber, String passportNumber, String contactNumber) {
            this.fullName = fullName;
            this.icNumber = icNumber;
            this.passportNumber = passportNumber;
            this.contactNumber = contactNumber;
        }
    }

