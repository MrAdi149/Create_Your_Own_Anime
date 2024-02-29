// MaleCharacter Activity
package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MaleCharacter : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var recyclerViewMaleAdapter: RecyclerViewMaleAdapter? = null
    private var maleList = mutableListOf<Male>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_male_character)

        maleList = ArrayList()
        recyclerView = findViewById(R.id.rvMaleLists)
        recyclerViewMaleAdapter = RecyclerViewMaleAdapter(maleList)

        recyclerViewMaleAdapter?.setOnItemClickListener { selectedMale ->
            startSelectedCharacterActivity(selectedMale)
        }

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerViewMaleAdapter

        prepareMaleListData()
    }

    private fun prepareMaleListData() {
        var male = Male("Angry Hero", R.drawable.angry_hero)
        maleList.add(male)
        male = Male("Simple Hero", R.drawable.simple_hero)
        maleList.add(male)
        male = Male("Master Of Hero", R.drawable.master_of_hero)
        maleList.add(male)
        male = Male("Silent Character", R.drawable.silent_character)
        maleList.add(male)
        male = Male("Side Character 1", R.drawable.side_character_1)
        maleList.add(male)
        male = Male("Side Character 2", R.drawable.side_character_2)
        maleList.add(male)
        male = Male("Side Character 3", R.drawable.side_character_3)
        maleList.add(male)
        male = Male("Villian 1", R.drawable.villian_1)
        maleList.add(male)
        male = Male("Villian 2", R.drawable.villian_2)
        maleList.add(male)
        male = Male("Villian 3", R.drawable.villian_3)
        maleList.add(male)
        male = Male("Villian 4", R.drawable.villian_4)
        maleList.add(male)
        male = Male("Villian 5", R.drawable.villian_5)
        maleList.add(male)
        male = Male("Confident Hero", R.drawable.confident_hero)
        maleList.add(male)

        recyclerViewMaleAdapter!!.notifyDataSetChanged()
    }

    private fun startSelectedCharacterActivity(selectedMale: Male) {
        val intent = Intent(this, SelectedCharacter::class.java)
        intent.putExtra("selectedCharacterName", selectedMale.name)
        intent.putExtra("selectedCharacterImage", selectedMale.image)
        startActivity(intent)
    }
}