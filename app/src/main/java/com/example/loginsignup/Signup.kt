package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.SignupButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.Confirmpassword.text.toString()
            val username = binding.username.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && username.isNotEmpty()) {
                if (pass == confirmPass) {
                    // Check if the username is already taken
                    val user = Model(username, email, pass)
                    checkUsernameAvailability(user) { isAvailable ->
                        if (isAvailable) {
                            // Username is available, proceed with account creation
                            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Registration successful
                                        saveUserDataToFirestore(user)
                                        Toast.makeText(
                                            this,
                                            "Registration successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navigate to another activity after successful signup
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish() // Optional: finish the current activity to prevent going back to the signup screen
                                    } else {
                                        // Registration failed
                                        Toast.makeText(
                                            this,
                                            "Registration failed: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // Username is already taken
                            Toast.makeText(
                                this,
                                "Username is already taken. Please choose a different one.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUsernameAvailability(user: Model, callback: (Boolean) -> Unit) {
        // Check if the username is already present in the database
        firestore.collection("members")
            .whereEqualTo("username", user.username)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Check if any documents match the query
                    val isAvailable = task.result?.isEmpty ?: true
                    callback(isAvailable)
                } else {
                    // Error occurred while checking username availability
                    callback(false)
                }
            }
    }

    private fun saveUserDataToFirestore(user: Model) {
        // Save user data to Firestore
        val userData = hashMapOf(
            "username" to user.username,
            "email" to user.email,
            "password" to user.password
        )

        firestore.collection("members")
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(userData)
            .addOnSuccessListener {
                // User data saved successfully
                Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()

                // Optionally, you can navigate to another activity or perform other actions
                // For example, navigate to the main activity after successful signup:
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: finish the current activity to prevent going back to the signup screen
            }
            .addOnFailureListener { e ->
                // Failed to save user data
                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
