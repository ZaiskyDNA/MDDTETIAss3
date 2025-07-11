package com.example.minfest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.minfest.databinding.ItemMenfessBinding // Import ViewBinding

class MenfessAdapter(
    private val onEditClick: (Menfess) -> Unit,
    private val onDeleteClick: (Menfess) -> Unit
) : ListAdapter<Menfess, MenfessAdapter.MenfessViewHolder>(MenfessDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenfessViewHolder {
        val binding = ItemMenfessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenfessViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenfessViewHolder, position: Int) {
        val menfess = getItem(position)
        holder.bind(menfess, onEditClick, onDeleteClick)
    }

    class MenfessViewHolder(private val binding: ItemMenfessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menfess: Menfess, onEditClick: (Menfess) -> Unit, onDeleteClick: (Menfess) -> Unit) {
            binding.tvTo.text = "To: ${menfess.to}"
            binding.tvFrom.text = "From: ${menfess.from}"
            binding.tvMessage.text = menfess.message

            binding.btnEdit.setOnClickListener { onEditClick(menfess) }
            binding.btnDelete.setOnClickListener { onDeleteClick(menfess) }
        }
    }
}

class MenfessDiffCallback : DiffUtil.ItemCallback<Menfess>() {
    override fun areItemsTheSame(oldItem: Menfess, newItem: Menfess): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Menfess, newItem: Menfess): Boolean {
        return oldItem == newItem
    }
}