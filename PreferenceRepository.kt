package com.example.labexam3

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class PreferenceRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("wellness_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString("habits", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>(){}.type
        return gson.fromJson(json, type)
    }

    fun getHabitsToday(): List<Habit> {
        val today = todayString()
        return getHabits().onEach {
            if (it.lastCompletionDate != today) it.completedToday = false
        }
    }

    fun addHabit(habit: Habit) {
        val habits = getHabits()
        habits.add(habit)
        saveHabits(habits)
    }

    fun updateHabit(habit: Habit, newName: String) {
        val habits = getHabits()
        habits.find { it.name == habit.name }?.name = newName
        saveHabits(habits)
    }

    fun deleteHabit(habit: Habit) {
        val habits = getHabits()
        habits.removeAll { it.name == habit.name }
        saveHabits(habits)
    }

    fun saveHabitCompletion(habit: Habit) {
        val habits = getHabits()
        val today = todayString()
        habits.find { it.name == habit.name }?.apply {
            completedToday = habit.completedToday
            lastCompletionDate = if (completedToday) today else lastCompletionDate
        }
        saveHabits(habits)
    }

    private fun saveHabits(habits: List<Habit>) {
        prefs.edit().putString("habits", gson.toJson(habits)).apply()
    }

    fun getMoodEntries(): List<MoodEntry> {
        val json = prefs.getString("moods", null) ?: return listOf()
        val type = object : TypeToken<List<MoodEntry>>(){}.type
        return gson.fromJson(json, type)
    }

    fun addMoodEntry(entry: MoodEntry) {
        val moods = getMoodEntries().toMutableList()
        moods.add(entry)
        prefs.edit().putString("moods", gson.toJson(moods)).apply()
    }

    fun getReminderInterval(): Int =
        prefs.getInt("reminder_interval", 2)

    fun setReminderInterval(hours: Int) {
        prefs.edit().putInt("reminder_interval", hours).apply()
    }

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
}