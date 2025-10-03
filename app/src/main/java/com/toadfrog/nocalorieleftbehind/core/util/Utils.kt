package com.toadfrog.nocalorieleftbehind.core.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object Utils {

    /**
     *@return The number of seconds from kotlin Clock epoch
     **/
    @OptIn(ExperimentalTime::class)
    fun todayMidnightTimestamp(): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        return today.atStartOfDayIn(timeZone).epochSeconds
    }
}