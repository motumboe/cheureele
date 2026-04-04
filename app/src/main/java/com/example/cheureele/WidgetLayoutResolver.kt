package com.example.cheureele

import java.time.LocalTime

private const val MINI_WIDGET_MAX_WIDTH_DP = 74
private const val MINI_WIDGET_MAX_HEIGHT_DP = 74
private const val COMPACT_WIDGET_MAX_WIDTH_DP = 146
private const val COMPACT_WIDGET_MAX_HEIGHT_DP = 92

enum class WidgetLayoutMode {
    MINI,
    COMPACT,
    WIDE,
    FULL
}

fun resolveWidgetLayoutMode(minWidthDp: Int, minHeightDp: Int): WidgetLayoutMode = when {
    minWidthDp <= MINI_WIDGET_MAX_WIDTH_DP && minHeightDp <= MINI_WIDGET_MAX_HEIGHT_DP ->
        WidgetLayoutMode.MINI

    minWidthDp > COMPACT_WIDGET_MAX_WIDTH_DP && minHeightDp <= COMPACT_WIDGET_MAX_HEIGHT_DP ->
        WidgetLayoutMode.WIDE

    minWidthDp <= COMPACT_WIDGET_MAX_WIDTH_DP || minHeightDp <= COMPACT_WIDGET_MAX_HEIGHT_DP ->
        WidgetLayoutMode.COMPACT

    else -> WidgetLayoutMode.FULL
}

fun getCompactTimeText(time: LocalTime = LocalTime.now()): String =
    getTimeText(time)
        .replace(" e ", "\ne ")
        .replace(" a ", "\na ")

fun getMiniTimeText(time: LocalTime = LocalTime.now()): String = getCompactTimeText(time)
