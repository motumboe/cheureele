package com.example.cheureele

import java.time.LocalTime
import java.util.Locale

private const val MINI_WIDGET_MAX_WIDTH_DP = 74
private const val MINI_WIDGET_MAX_HEIGHT_DP = 74
private const val COMPACT_WIDGET_MAX_WIDTH_DP = 146
private const val COMPACT_WIDGET_MAX_HEIGHT_DP = 92

enum class WidgetLayoutMode {
    MINI,
    COMPACT,
    FULL
}

fun resolveWidgetLayoutMode(minWidthDp: Int, minHeightDp: Int): WidgetLayoutMode = when {
    minWidthDp <= MINI_WIDGET_MAX_WIDTH_DP || minHeightDp <= MINI_WIDGET_MAX_HEIGHT_DP ->
        WidgetLayoutMode.MINI

    minWidthDp <= COMPACT_WIDGET_MAX_WIDTH_DP || minHeightDp <= COMPACT_WIDGET_MAX_HEIGHT_DP ->
        WidgetLayoutMode.COMPACT

    else -> WidgetLayoutMode.FULL
}

fun getMiniTimeNumber(time: LocalTime = LocalTime.now()): String =
    String.format(Locale.ROOT, "%02d\n%02d", time.hour, time.minute)
