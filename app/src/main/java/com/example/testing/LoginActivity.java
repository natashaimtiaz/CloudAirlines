//package com.example.testing;
//
//// ... other imports ...
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailEditText, passwordEditText;
//    private Button loginButton, signUpButton;
//    private FirebaseAuth auth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        auth = FirebaseAuth.getInstance();
//
//        emailEditText = findViewById(R.id.editTextEmail);
////        usernameEditText = findViewById(R.id.editTextUsername);
//        passwordEditText = findViewById(R.id.editTextPassword);
//        loginButton = findViewById(R.id.buttonLogin);
//
//        loginButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString().trim();
////            String username = EditText.getText().toString().trim();
//            String password = passwordEditText.getText().toString().trim();
//
//            // Input validation here...
//
//            // Authenticate user with Firebase Authentication
//            auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, task -> {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
//                            // Redirect to the main activity
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        });
//
//        signUpButton.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//            startActivity(intent);
//        });
//    }
//}
package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signUpButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Initialize your EditTexts and Buttons
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp); // Make sure the ID matches your 'Register' button in the XML

        // Set the onClick listener for the login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Input validation can be done here

            // Authenticate the user with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in is successful
                            Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            // Here you can start a new Intent to open the main activity of your app
                             Intent intent = new Intent(LoginActivity.this, MainPage.class);
                             intent.putExtra("USER_EMAIL", email);
                             startActivity(intent);

                             //pass email value
                             finish(); // Call finish() if you want to close the login activity
                        } else {
                            // Sign in fails
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set the onClick listener for the sign-up button
        signUpButton.setOnClickListener(v -> {
            // Start the SignUpActivity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
