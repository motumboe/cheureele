package com.example.cheureele

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeTextFormatterTest {

    @Test
    fun `usa mezanot a mezzanotte`() {
        assertEquals("mezanòt", getTimeText(LocalTime.MIDNIGHT))
    }

    @Test
    fun `arrotonda al quarto successivo dalla meta del quarto`() {
        assertEquals("mezanòt", getTimeText(LocalTime.of(0, 7, 29)))
        assertEquals("mezanòt e un quart", getTimeText(LocalTime.of(0, 7, 30)))
    }

    @Test
    fun `gestisce correttamente mezzogiorno e fine giornata`() {
        assertEquals("mezdé", getTimeText(LocalTime.NOON))
        assertEquals("mezanòt", getTimeText(LocalTime.of(23, 59)))
    }

    @Test
    fun `formatta l ora numerica con zero padding`() {
        assertEquals("06:05", getTimeNumber(LocalTime.of(6, 5)))
    }

    @Test
    fun `formatta la versione mini su due righe`() {
        assertEquals("06\n05", getMiniTimeNumber(LocalTime.of(6, 5)))
    }

    @Test
    fun `calcola il prossimo trigger al minuto successivo`() {
        val zoneId = ZoneId.of("Europe/Rome")
        val currentTime = Instant.parse("2026-04-01T10:34:27Z").toEpochMilli()
        val expectedTrigger = Instant.parse("2026-04-01T10:35:00Z").toEpochMilli()

        assertEquals(expectedTrigger, getNextMinuteTriggerAt(currentTime, zoneId))
    }

    @Test
    fun `usa il layout mini quando lo spazio e quello di una cella`() {
        assertEquals(WidgetLayoutMode.MINI, resolveWidgetLayoutMode(56, 56))
    }

    @Test
    fun `usa il layout compatto in spazi intermedi`() {
        assertEquals(WidgetLayoutMode.COMPACT, resolveWidgetLayoutMode(120, 84))
    }

    @Test
    fun `usa il layout completo quando c e spazio sufficiente`() {
        assertEquals(WidgetLayoutMode.FULL, resolveWidgetLayoutMode(180, 96))
    }
}
