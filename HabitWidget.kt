package com.example.labexam3

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class HabitWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val prefs = PreferenceRepository(context)
        val habits = prefs.getHabitsToday()
        val completed = habits.count { it.completedToday }
        val total = habits.size
        val percent = if (total > 0) "$completed/$total" else "0/0"

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_habit)
            views.setTextViewText(R.id.tvWidgetProgress, "Habits: $percent Complete")
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}