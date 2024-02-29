// RecyclerViewFemaleAdapter
package com.example.loginsignup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.LayoutFemaleListBinding

class RecyclerViewFemaleAdapter(
    private val femaleList: List<Female>
) : RecyclerView.Adapter<RecyclerViewFemaleAdapter.MyViewHolder>() {

    private var onItemClickListener: ((Female) -> Unit)? = null

    fun setOnItemClickListener(listener: (Female) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            LayoutFemaleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return femaleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val female = femaleList[position]
        holder.bind(female)
    }

    inner class MyViewHolder(private val binding: LayoutFemaleListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(female: Female) {
            binding.apply {
                femaleCharacterTitle.text = female.name
                femaleCharacterImage.setImageResource(female.image)

                cardView2.setOnClickListener {
                    onItemClickListener?.invoke(female)
                }
            }
        }
    }
}