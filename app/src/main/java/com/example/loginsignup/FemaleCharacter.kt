// FemaleCharacter Activity
package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FemaleCharacter : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var recyclerViewFemaleAdapter: RecyclerViewFemaleAdapter? = null
    private var femaleList = mutableListOf<Female>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_female_character)

        femaleList = ArrayList()
        recyclerView = findViewById(R.id.rvFemaleLists)
        recyclerViewFemaleAdapter = RecyclerViewFemaleAdapter(femaleList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerViewFemaleAdapter

        recyclerViewFemaleAdapter?.setOnItemClickListener { selectedFemale ->
            startSelectedCharacterActivity(selectedFemale)
        }

        prepareFemaleListData()
    }

    private fun prepareFemaleListData() {
        var female = Female("Simple Heroine", R.drawable.simplefemale)
        femaleList.add(female)
        female = Female("Confused Heroine", R.drawable.confusedfemale)
        femaleList.add(female)
        female = Female("Strong Heroine 1", R.drawable.strongfemale1)
        femaleList.add(female)
        female = Female("Strong Heroine 2", R.drawable.strongfemale2)
        femaleList.add(female)
        female = Female("Strong Villain 1", R.drawable.fvillian1)
        femaleList.add(female)
        female = Female("Strong Villain 2", R.drawable.fvillian2)
        femaleList.add(female)
        female = Female("Strong Villain 3", R.drawable.fvillian3)
        femaleList.add(female)
        female = Female("Strong Villain 4", R.drawable.fvillian4)
        femaleList.add(female)
        female = Female("Strong Villain 5", R.drawable.fvillian5)
        femaleList.add(female)
        female = Female("Innocent Girl", R.drawable.innocentfemale)
        femaleList.add(female)
        female = Female("Female Side Character 1", R.drawable.femalesidecharacter1)
        femaleList.add(female)
        female = Female("Female Side Character 2", R.drawable.femalesidecharacter2)
        femaleList.add(female)
        female = Female("Female Side Character 3", R.drawable.femalesidecharacter3)
        femaleList.add(female)
        female = Female("Female Side Character 4", R.drawable.femalesidecharacter4)
        femaleList.add(female)
        // Add more female characters as needed

        recyclerViewFemaleAdapter?.notifyDataSetChanged()
    }

    private fun startSelectedCharacterActivity(selectedFemale: Female) {
        val intent = Intent(this, SelectedCharacter::class.java)
        intent.putExtra("selectedCharacterName", selectedFemale.name)
        intent.putExtra("selectedCharacterImage", selectedFemale.image)
        startActivity(intent)
    }
}
