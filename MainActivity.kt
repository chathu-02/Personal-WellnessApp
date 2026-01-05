package com.example.labexam3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.labexam3.databinding.ActivityMainBinding
import com.example.labexam3.ui.habits.HabitTrackerFragment
import com.example.labexam3.ui.mood.MoodJournalFragment
import com.example.labexam3.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HabitTrackerFragment())
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_habits -> loadFragment(HabitTrackerFragment())
                R.id.menu_mood -> loadFragment(MoodJournalFragment())
                R.id.menu_settings -> loadFragment(SettingsFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}