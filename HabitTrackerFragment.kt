package com.example.labexam3.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labexam3.Habit
import com.example.labexam3.PreferenceRepository
import com.example.labexam3.databinding.FragmentHabitTrackerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HabitTrackerFragment : Fragment() {
    private var _binding: FragmentHabitTrackerBinding? = null
    private val binding get() = _binding!!
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var prefs: PreferenceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitTrackerBinding.inflate(inflater, container, false)
        prefs = PreferenceRepository(requireContext())
        setupRecyclerView()
        setupListeners()
        return binding.root
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = prefs.getHabitsToday(),
            onCheckChanged = { habit, checked ->
                habit.completedToday = checked
                prefs.saveHabitCompletion(habit)
                updateProgress()
            },
            onEdit = { habit -> showHabitDialog(habit) },
            onDelete = { habit -> prefs.deleteHabit(habit); refreshHabits() }
        )
        binding.recyclerViewHabits.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = habitAdapter
        }
        updateProgress()
    }

    private fun setupListeners() {
        binding.btnAddHabit.setOnClickListener { showHabitDialog() }
    }

    private fun showHabitDialog(habitToEdit: Habit? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_habit, null)
        val editText = dialogView.findViewById<android.widget.EditText>(R.id.etHabitName)
        editText.setText(habitToEdit?.name ?: "")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (habitToEdit == null) "Add Habit" else "Edit Habit")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    if (habitToEdit == null) {
                        prefs.addHabit(Habit(name = name))
                    } else {
                        prefs.updateHabit(habitToEdit, name)
                    }
                    refreshHabits()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun refreshHabits() {
        habitAdapter.habits = prefs.getHabitsToday()
        habitAdapter.notifyDataSetChanged()
        updateProgress()
    }

    private fun updateProgress() {
        val habits = habitAdapter.habits
        val completed = habits.count { it.completedToday }
        val total = habits.size
        binding.progressBar.max = total
        binding.progressBar.progress = completed
        binding.tvProgress.text = "Habits: $completed/$total Complete"
    }

    override fun onResume() {
        super.onResume()
        refreshHabits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}