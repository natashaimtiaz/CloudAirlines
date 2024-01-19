//package com.example.testing;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SignUp {
//
//    // Firebase Realtime Database reference
//    private DatabaseReference usersRef;
//
//    public SignUp() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        usersRef = database.getReference("Users");
//    }
//
//    public void SignUp(String email, String username, String password) {
//        // Create a User object with the entered information
//        User newUser = new User(username, password, email);
//
//        // Push the user data to the "Users" node in Firebase Realtime Database
//        usersRef.child(username).setValue(newUser, (databaseError, databaseReference) -> {
//            if (databaseError == null) {
//                // Success - handle UI update or navigation as needed
//            } else {
//                // Failure - handle error
//            }
//        });
//    }
//
//    // User class to represent user data
//    static class User {
//        public String username;
//        public String password;
////        public String name;
//        public String email;
////        public String phoneNumber;
////        public String passportNumber;
//
//        // Default constructor required for Firebase
//        public User() {
//        }
//
//        // Constructor with parameters
//        public User(String username, String password, String email) {
//            this.username = username;
//            this.password = password;
////            this.name = name;
//            this.email = email;
////            this.phoneNumber = phoneNumber;
////            this.passportNumber = passportNumber;
//        }
//    }
//}

package com.example.testing;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public SignUp() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(String email, String username, String password, SignUpResultListener listener) {
        // Register user with Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        String userId = task.getResult().getUser().getUid();
                        User newUser = new User(username, email);

                        // Save additional user info in Firestore
                        db.collection("Users").document(userId).set(newUser)
                                .addOnSuccessListener(aVoid -> listener.onSuccess())
                                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                    } else {
                        // Registration failed
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    // User class to represent user data
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

    public interface SignUpResultListener {
        void onSuccess();

        void onFailure(String message);
    }
}
