package com.example.cheureele

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Implementation of App Widget functionality.
 */
class YourWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("widget","onUpdate chiamata")
        // Itera su tutti i widget istanziati
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

fun getTimeText(): String {
    val currentTime = LocalTime.now()
    // Qui puoi implementare la tua logica per convertire l'ora in formato testuale
    // Per semplicità, userò HH:mm, ma puoi personalizzarlo come preferisci
    return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = getTimeText() // La tua funzione per ottenere l'ora in formato testuale
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.your_widget_provider)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}