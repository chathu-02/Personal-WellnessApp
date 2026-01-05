package com.example.labexam3.ui.mood

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import com.example.labexam3.R

class MoodEmojiSelectorDialog(
    context: Context,
    private val onEmojiSelected: (String) -> Unit
) : Dialog(context) {

    private val emojis = listOf("😊", "😐", "😢", "😡", "😁", "😴", "🥳", "🤔", "🥺", "😇")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_emoji_selector)
        val gridLayout = findViewById<GridLayout>(R.id.gridEmoji)
        emojis.forEach { emoji ->
            val tv = TextView(context).apply {
                text = emoji
                textSize = 32f
                setPadding(16, 16, 16, 16)
                setOnClickListener {
                    onEmojiSelected(emoji)
                    dismiss()
                }
            }
            gridLayout.addView(tv)
        }
    }
}