package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.databinding.ActivityChooseCharacterBinding

class ChooseCharacter : AppCompatActivity() {

    private lateinit var binding: ActivityChooseCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the "Female" button
        binding.Female.setOnClickListener {
            // Navigate to FemaleCharacter activity
            val intent = Intent(this, FemaleCharacter::class.java)
            startActivity(intent)
        }

        // Set click listener for the "Male" button
        binding.Male.setOnClickListener {
            // Navigate to MaleCharacter activity
            val intent = Intent(this, MaleCharacter::class.java)
            startActivity(intent)
        }
    }
}
