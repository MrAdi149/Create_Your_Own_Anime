package com.example.loginsignup

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.LayoutSelectedCharacterBinding

class RecyclerViewSelectedAdapter(private val selectedList: MutableList<Selected>) :
    RecyclerView.Adapter<RecyclerViewSelectedAdapter.SelectedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder {
        val binding = LayoutSelectedCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectedViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        val selected = selectedList[position]
        holder.bind(selected)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }

    class SelectedViewHolder(
        private val binding: LayoutSelectedCharacterBinding,
        private val adapter: RecyclerViewSelectedAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(selected: Selected) {
            binding.selectedCharacterImage.setImageResource(selected.image)
            binding.editText.text = Editable.Factory.getInstance().newEditable(selected.editTextContent)

            // Add TextWatcher to update the Selected object when the text changes
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                    // Not needed
                }

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                    // Update the Selected object with the entered text content
                    val updatedSelected = selected.copy(editTextContent = charSequence.toString())
                    adapter.updateItemAtPosition(updatedSelected, adapterPosition)
                }

                override fun afterTextChanged(editable: Editable?) {
                    // Not needed
                }
            })
        }
    }

    fun updateItemAtPosition(selected: Selected, position: Int) {
        selectedList[position] = selected
    }
}
