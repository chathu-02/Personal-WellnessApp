package com.example.labexam3

data class Habit(
    val name: String,
    var completedToday: Boolean = false,
    var lastCompletionDate: String = ""
)