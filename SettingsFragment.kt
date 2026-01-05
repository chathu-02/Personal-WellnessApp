package com.example.labexam3.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.labexam3.PreferenceRepository
import com.example.labexam3.ReminderScheduler
import com.example.labexam3.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: PreferenceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        prefs = PreferenceRepository(requireContext())
        setupIntervalSpinner()
        return binding.root
    }

    private fun setupIntervalSpinner() {
        val intervals = listOf(1, 2, 3)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, intervals)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInterval.adapter = adapter
        val current = prefs.getReminderInterval()
        val idx = intervals.indexOf(current)
        if (idx != -1) binding.spinnerInterval.setSelection(idx)

        binding.btnSave.setOnClickListener {
            val selectedInterval = binding.spinnerInterval.selectedItem as Int
            prefs.setReminderInterval(selectedInterval)
            ReminderScheduler.schedule(requireContext(), selectedInterval)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}