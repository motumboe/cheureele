package com.example.cheureele

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import java.time.LocalTime

private const val DEFAULT_FULL_WIDGET_WIDTH_DP = 180
private const val DEFAULT_FULL_WIDGET_HEIGHT_DP = 96

class YourWidgetProvider : AppWidgetProvider() {
    companion object {
        private const val LOG_TAG = "widget"
        private const val ACTION_UPDATE_WIDGET = "com.example.cheureele.ACTION_UPDATE_WIDGET"

        fun requestImmediateUpdate(context: Context) {
            context.sendBroadcast(createUpdateIntent(context))
        }

        private fun createUpdateIntent(context: Context): Intent =
            Intent(context, YourWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_WIDGET
            }

        private fun createUpdatePendingIntent(context: Context): PendingIntent =
            PendingIntent.getBroadcast(
                context,
                0,
                createUpdateIntent(context),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        private fun getInstalledWidgetIds(context: Context): IntArray {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetComponent = ComponentName(context, YourWidgetProvider::class.java)
            return appWidgetManager.getAppWidgetIds(widgetComponent)
        }

        private fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = getInstalledWidgetIds(context)

            if (widgetIds.isEmpty()) {
                return
            }

            widgetIds.forEach { appWidgetId ->
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }

        private fun scheduleNextUpdate(context: Context) {
            val widgetIds = getInstalledWidgetIds(context)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createUpdatePendingIntent(context)

            alarmManager.cancel(pendingIntent)

            if (widgetIds.isEmpty()) {
                Log.d(LOG_TAG, "Nessun widget attivo: scheduler fermato")
                return
            }

            val triggerAt = getNextMinuteTriggerAt(System.currentTimeMillis())
            Log.d(LOG_TAG, "Prossimo aggiornamento widget alle $triggerAt")
            alarmManager.set(AlarmManager.RTC, triggerAt, pendingIntent)
        }

        private fun cancelScheduledUpdates(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(createUpdatePendingIntent(context))
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

        scheduleNextUpdate(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateAppWidget(context, appWidgetManager, appWidgetId)
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_UPDATE_WIDGET,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.d(LOG_TAG, "Aggiornamento widget per action=${intent.action}")
                updateAllWidgets(context)
                scheduleNextUpdate(context)
            }

            else -> super.onReceive(context, intent)
        }
    }

    override fun onEnabled(context: Context) {
        scheduleNextUpdate(context)
    }

    override fun onDisabled(context: Context) {
        cancelScheduledUpdates(context)
        super.onDisabled(context)
    }
}

private fun createRemoteViews(
    context: Context,
    layoutMode: WidgetLayoutMode,
    currentTime: LocalTime
): RemoteViews = when (layoutMode) {
    WidgetLayoutMode.MINI -> RemoteViews(
        context.packageName,
        R.layout.your_widget_provider_mini
    ).apply {
        setTextViewText(R.id.numeric_time_text, getMiniTimeNumber(currentTime))
    }

    WidgetLayoutMode.COMPACT -> RemoteViews(
        context.packageName,
        R.layout.your_widget_provider_compact
    ).apply {
        setTextViewText(R.id.numeric_time_text, getTimeNumber(currentTime))
    }

    WidgetLayoutMode.FULL -> RemoteViews(
        context.packageName,
        R.layout.your_widget_provider
    ).apply {
        setTextViewText(R.id.numeric_time_text, getTimeNumber(currentTime))
        setTextViewText(R.id.appwidget_text, getTimeText(currentTime))
    }
}

private fun resolveWidgetLayoutMode(
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
): WidgetLayoutMode {
    val widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val widgetWidthDp = widgetOptions.getInt(
        AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH,
        DEFAULT_FULL_WIDGET_WIDTH_DP
    )
    val widgetHeightDp = widgetOptions.getInt(
        AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT,
        DEFAULT_FULL_WIDGET_HEIGHT_DP
    )

    return resolveWidgetLayoutMode(widgetWidthDp, widgetHeightDp)
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val widgetWidthDp = widgetOptions.getInt(
        AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH,
        DEFAULT_FULL_WIDGET_WIDTH_DP
    )
    val widgetHeightDp = widgetOptions.getInt(
        AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT,
        DEFAULT_FULL_WIDGET_HEIGHT_DP
    )
    val layoutMode = resolveWidgetLayoutMode(appWidgetManager, appWidgetId)
    val currentTime = LocalTime.now()
    val views = createRemoteViews(context, layoutMode, currentTime)

    Log.d(
        "widget",
        "Aggiorna widget $appWidgetId in modalita $layoutMode (${widgetWidthDp}x${widgetHeightDp}dp)"
    )

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
