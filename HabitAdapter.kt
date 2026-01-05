package com.example.labexam3.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.Habit
import com.example.labexam3.databinding.ItemHabitBinding

class HabitAdapter(
    var habits: List<Habit>,
    private val onCheckChanged: (Habit, Boolean) -> Unit,
    private val onEdit: (Habit) -> Unit,
    private val onDelete: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.cbCompleted.text = habit.name
            binding.cbCompleted.isChecked = habit.completedToday
            binding.cbCompleted.setOnCheckedChangeListener { _, checked ->
                onCheckChanged(habit, checked)
            }
            binding.btnEdit.setOnClickListener { onEdit(habit) }
            binding.btnDelete.setOnClickListener { onDelete(habit) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}