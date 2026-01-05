package com.example.labexam3.ui.mood

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labexam3.MoodEntry
import com.example.labexam3.PreferenceRepository
import com.example.labexam3.databinding.FragmentMoodJournalBinding
import java.text.SimpleDateFormat
import java.util.*

class MoodJournalFragment : Fragment() {
    private var _binding: FragmentMoodJournalBinding? = null
    private val binding get() = _binding!!
    private lateinit var moodAdapter: MoodAdapter
    private lateinit var prefs: PreferenceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodJournalBinding.inflate(inflater, container, false)
        prefs = PreferenceRepository(requireContext())
        setupRecyclerView()
        setupListeners()
        return binding.root
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            moods = prefs.getMoodEntries().sortedByDescending { it.timestamp },
            onShare = { mood -> shareMood(mood) }
        )
        binding.recyclerViewMoods.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moodAdapter
        }
    }

    private fun setupListeners() {
        binding.btnAddMood.setOnClickListener { showEmojiSelector() }
    }

    private fun showEmojiSelector() {
        val dialog = MoodEmojiSelectorDialog(requireContext()) { emoji ->
            showAddMoodDialog(emoji)
        }
        dialog.show()
    }

    private fun showAddMoodDialog(emoji: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mood, null)
        val etNote = dialogView.findViewById<android.widget.EditText>(R.id.etMoodNote)
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Log Mood")
            .setMessage("Selected: $emoji")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val note = etNote.text.toString()
                val mood = MoodEntry(
                    emoji = emoji,
                    note = note,
                    timestamp = System.currentTimeMillis()
                )
                prefs.addMoodEntry(mood)
                refreshMoods()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun refreshMoods() {
        moodAdapter.moods = prefs.getMoodEntries().sortedByDescending { it.timestamp }
        moodAdapter.notifyDataSetChanged()
    }

    private fun shareMood(mood: MoodEntry) {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(mood.timestamp))
        val text = "Mood: ${mood.emoji}\nNote: ${mood.note}\nTime: $date"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Share mood entry"))
    }

    override fun onResume() {
        super.onResume()
        refreshMoods()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}