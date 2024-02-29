package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            // Check if the user is authenticated using Firebase
            if (firebaseAuth.currentUser != null) {
                showToast("Already authenticated with Firebase")
                // Navigate to another activity (replace with your desired destination)
                val intent = Intent(this, ChooseCharacter::class.java)
                startActivity(intent)
                finish() // Optional: finish the current activity to prevent going back to the login screen
            } else {
                // Authenticate using Firestore
                val user = Model(username, "", password)
                isValidLogin(user) { isValid ->
                    if (isValid) {
                        // Firestore login successful
                        showToast("Firestore Login successful")

                        // Authenticate with Firebase
                        firebaseAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    // Firebase login successful
                                    showToast("Firebase Login successful")

                                    // Navigate to another activity (replace with your desired destination)
                                    val intent = Intent(this, ChooseCharacter::class.java)
                                    startActivity(intent)
                                    finish() // Optional: finish the current activity to prevent going back to the login screen
                                } else {
                                    // Firebase login failed
                                    showToast("Firebase Login failed: ${authTask.exception?.message}")
                                }
                            }
                    } else {
                        // Display an error message
                        showToast("Invalid username or password")
                    }
                }
            }
        }

        // Set an OnClickListener on the TextView to navigate to the SignupActivity
        binding.textView.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun isValidLogin(user: Model, callback: (Boolean) -> Unit) {
        // Check if the username and password are not empty
        if (user.username.isNotEmpty() && user.password.isNotEmpty()) {
            checkUsernameAndPassword(user) { isValid ->
                // Pass the result of the callback directly
                callback(isValid)
            }
        } else {
            // Empty username or password
            callback(false)
        }
    }

    private fun checkUsernameAndPassword(user: Model, callback: (Boolean) -> Unit) {
        // Check if the username and password are present in the database
        firestore.collection("members")
            .whereEqualTo("username", user.username)
            .whereEqualTo("password", user.password)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Check if any documents match the query
                    val result = task.result
                    val isValid = result != null && !result.isEmpty
                    callback(isValid)
                } else {
                    // Error occurred while checking username and password
                    callback(false)
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
