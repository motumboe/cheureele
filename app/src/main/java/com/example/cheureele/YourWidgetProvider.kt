package com.example.cheureele

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Implementation of App Widget functionality.
 */
class YourWidgetProvider : AppWidgetProvider() {
    companion object {
        const val ACTION_UPDATE_WIDGET = "com.example.cheureele.ACTION_UPDATE_WIDGET"
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("widget","onUpdate chiamata")
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val alarmIntent = Intent(context, YourWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_WIDGET
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval: Long = 300000

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, interval, pendingIntent)

        // Itera su tutti i widget istanziati
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
            appWidgetIds?.let { onUpdate(context!!, appWidgetManager, it) }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        val alarmIntent = Intent(context, YourWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_WIDGET
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}

fun getTimeText(): String {
    val testo = arrayOf(
        "mezanòt", "mezanòt e un quart", "mezanòt e meza", "un quart a un bòt",

        "un bòt", "un bòt e un quart", "un bòt e mès", "un quart a le dò",

        "le dò", "le dò e un quart", "le dò e meza", "un quart a le trè",

        "le trè", "le trè e un quart", "le trè e meza", "un quart a le quater",

        "le quater", "le quater e un quart", "le quater e meza", "un quart a le sic",

        "le sic", "le sic e un quart", "le sic e meza", "un quart a le sés",

        "le sés", "le sés e un quart", "le sés e meza", "un quart a le sèt",

        "le sét", "le sét e un quart", "le sét e meza", "un quart a le òt",

        "le òt", "le òt e un quart", "le òt e meza", "un quart a le nöf",

        "le nöf", "le nöf e un quart", "le nöf e meza", "un quart a le dés",

        "le dés", "le dés e un quart", "le dés e meza", "un quart a le öndès",

        "le öndès", "le öndès e un quart", "le öndès e meza", "un quart a mezdé",

        "mezdé", "mezdé e un quart", "mezdé e mès", "un quart a un bòt",

        "un bòt", "un bòt e un quart", "un bòt e mès", "un quart a le dò",

        "le dò", "le dò e un quart", "le dò e meza", "un quart a le trè",

        "le trè", "le trè e un quart", "le trè e meza", "un quart a le quater",

        "le quater", "le quater e un quart", "le quater e meza", "un quart a le sic",

        "le sic", "le sic e un quart", "le sic e meza", "un quart a le sés",

        "le sés", "le sés e un quart", "le sés e meza", "un quart a le sèt",

        "le sét", "le sét e un quart", "le sét e meza", "un quart a le òt",

        "le òt", "le òt e un quart", "le òt e meza", "un quart a le nöf",

        "le nöf", "le nöf e un quart", "le nöf e meza", "un quart a le dés",

        "le dés", "le dés e un quart", "le dés e meza", "un quart a le öndès",

        "le öndès", "le öndès e un quart", "le öndès e meza", "un quart a mezanòt",

        "mezanòt"
    )
    val now = LocalTime.now()
    val midnight = LocalTime.MIDNIGHT
    val diff = ChronoUnit.SECONDS.between(midnight, now)
    return testo[(((diff+450) / 900).toInt()) % testo.size]

//    val currentTime = LocalTime.now()
    // Qui puoi implementare la tua logica per convertire l'ora in formato testuale
    // Per semplicità, userò HH:mm, ma puoi personalizzarlo come preferisci
//    return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
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