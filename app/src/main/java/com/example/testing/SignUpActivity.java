//package com.example.testing;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    private EditText emailEditText, usernameEditText, passwordEditText, nameEditText, phoneEditText, passportEditText;
//    private Button signUpButton;
//    SignUp signupHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        // Initialize UI components
//        emailEditText = findViewById(R.id.editTextEmail);
//        usernameEditText = findViewById(R.id.editTextUsername);
//        passwordEditText = findViewById(R.id.editTextPassword);
////        nameEditText = findViewById(R.id.editTextName);
////        phoneEditText = findViewById(R.id.editTextPhone);
////        passportEditText = findViewById(R.id.editTextPassport);
//        signUpButton = findViewById(R.id.buttonSignUp);
//
//        signupHelper = new SignUp();
//
//        signUpButton.setOnClickListener(v -> {
//            // Extract text from EditTexts
//            String email = emailEditText.getText().toString();
//            String username = usernameEditText.getText().toString();
//            String password = passwordEditText.getText().toString();
////            String name = nameEditText.getText().toString();
////            String phone = phoneEditText.getText().toString();
////            String passport = passportEditText.getText().toString();
//
//            // Use user_signup to sign up the user
//            signupHelper.SignUp(email, username, password);
//
//            // Provide feedback or navigate
//            Toast.makeText(this, "Sign Up initiated", Toast.LENGTH_SHORT).show();
//        });
//    }
//}

package com.example.testing;

// ... other imports ...
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, usernameEditText, passwordEditText;
    private Button signUpButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        signUpButton = findViewById(R.id.buttonSignUp);

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Input validation here...

            // Register user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User registration successful, store additional data in Firestore
                            String userId = task.getResult().getUser().getUid();
                            User newUser = new User(username, email);

                            db.collection("Users").document(userId).set(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to login activity or main activity
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        } else {
                            // Registration failed
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    static class User {
        public String username;
        public String email;

        public User() {
            // Default constructor required for Firestore
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }
}
