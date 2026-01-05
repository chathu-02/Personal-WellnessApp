package com.example.labexam3.ui.mood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.MoodEntry
import com.example.labexam3.databinding.ItemMoodBinding
import java.text.SimpleDateFormat
import java.util.*

class MoodAdapter(
    var moods: List<MoodEntry>,
    private val onShare: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(val binding: ItemMoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mood: MoodEntry) {
            binding.tvEmoji.text = mood.emoji
            binding.tvNote.text = mood.note
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(mood.timestamp))
            binding.tvTimestamp.text = date
            binding.btnShare.setOnClickListener { onShare(mood) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(moods[position])
    }

    override fun getItemCount() = moods.size
}