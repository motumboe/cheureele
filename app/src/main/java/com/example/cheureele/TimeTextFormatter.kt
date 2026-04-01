package com.example.cheureele

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val SECONDS_PER_QUARTER = 15 * 60L
private const val QUARTER_ROUNDING_OFFSET_SECONDS = SECONDS_PER_QUARTER / 2

private val numericTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private val dialectQuarterTexts = listOf(
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

fun getTimeText(time: LocalTime = LocalTime.now()): String {
    val secondsFromMidnight = ChronoUnit.SECONDS.between(LocalTime.MIDNIGHT, time)
    val roundedQuarterIndex =
        ((secondsFromMidnight + QUARTER_ROUNDING_OFFSET_SECONDS) / SECONDS_PER_QUARTER).toInt()

    return dialectQuarterTexts[roundedQuarterIndex % dialectQuarterTexts.size]
}

fun getTimeNumber(time: LocalTime = LocalTime.now()): String = time.format(numericTimeFormatter)

fun getNextMinuteTriggerAt(
    currentTimeMillis: Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): Long {
    val nextMinute = Instant.ofEpochMilli(currentTimeMillis)
        .atZone(zoneId)
        .plusMinutes(1)
        .truncatedTo(ChronoUnit.MINUTES)

    return nextMinute.toInstant().toEpochMilli()
}
