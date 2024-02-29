// RecyclerViewMaleAdapter
package com.example.loginsignup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.LayoutMaleListBinding

class RecyclerViewMaleAdapter(
    private val maleList: List<Male>,
    private var onItemClickListener: ((Male) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerViewMaleAdapter.MyViewHolder>() {

    fun setOnItemClickListener(listener: (Male) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            LayoutMaleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return maleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val male = maleList[position]
        holder.bind(male)
    }

    inner class MyViewHolder(private val binding: LayoutMaleListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(male: Male) {
            binding.apply {
                maleCharacterTitle.text = male.name
                maleCharacterImage.setImageResource(male.image)

                cardView.setOnClickListener {
                    onItemClickListener?.invoke(male)
                }
            }
        }
    }
}