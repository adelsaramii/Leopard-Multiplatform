package com.attendace.leopard.util.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

fun String.toDateHourly(): String {
    val dateTime = LocalDateTime.parse(this)
    var dayOfWeek = dateTime.dayOfWeek.name
    val dayOfMonth = dateTime.dayOfMonth
    var month = dateTime.month.name
    val year = dateTime.year

    if (dayOfWeek.length > 3) {
        dayOfWeek = dayOfWeek.substring(0, 3)
    }
    if (month.length > 3) {
        month = month.substring(0, 3)
    }

    return "$dayOfWeek | $dayOfMonth $month $year"
}

fun String.toDateDaily(): String {
    val dateTime = LocalDateTime.parse(this)
    var dayOfWeek = dateTime.dayOfWeek.name
    val dayOfMonth = dateTime.dayOfMonth
    var month = dateTime.month.name
    val year = dateTime.year
    if (dayOfWeek.length > 3) {
        dayOfWeek = dayOfWeek.substring(0, 3)
    }
    if (month.length > 3) {
        month = month.substring(0, 3)
    }
    return "$dayOfWeek $dayOfMonth $month $year"
}

fun String.toHourAndMinute(): String {
    val dateTime = LocalDateTime.parse(this)
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}

fun String.toDateHumanReadable(): Pair<DayStructure, String> {
    try {
        val now = Clock.System.now()
        val dateTime = LocalDateTime.parse(this)

        val start: Instant = Instant.parse("$now")
        val end: Instant = Instant.parse("${dateTime}Z")
        val durationSinceThen: Duration = start - end

        return when (durationSinceThen.inWholeDays.toInt()) {
            0 -> {
                Pair(DayStructure.Today, this.toHourAndMinute())
            }

            1 -> {
                Pair(DayStructure.Yesterday, this.toHourAndMinute())
            }

            else -> {
                Pair(
                    DayStructure.Other,
                    "${dateTime.monthNumber}/${dateTime.dayOfMonth} ${this.toHourAndMinute()}"
                )
            }
        }
    } catch (e: Exception) {
        try {
            val now = Clock.System.now()
            val dateTime = LocalDateTime.parse(this)

            val start: Instant = Instant.parse("$now")
            val end: Instant = Instant.parse("${dateTime}Z".removeRange(15, 16))
            val durationSinceThen: Duration = start - end

            return when (durationSinceThen.inWholeDays.toInt()) {
                0 -> {
                    Pair(DayStructure.Today, this.toHourAndMinute())
                }

                1 -> {
                    Pair(DayStructure.Yesterday, this.toHourAndMinute())
                }

                else -> {
                    Pair(
                        DayStructure.Other,
                        "${dateTime.monthNumber}/${dateTime.dayOfMonth} ${this.toHourAndMinute()}"
                    )
                }
            }
        } catch (e: Exception) {

            var date = this

            if (this.getOrNull(17).toString() == "0" && this.getOrNull(18).toString() == "0") {
                date = this.replaceRange(17, 18, "1").replaceRange(18, 19, "1")
            }

            val now = Clock.System.now()
            val dateTime = LocalDateTime.parse(date)

            val start: Instant = Instant.parse("$now")
            val end: Instant = Instant.parse("${dateTime}Z")
            val durationSinceThen: Duration = start - end

            return when (durationSinceThen.inWholeDays.toInt()) {
                0 -> {
                    Pair(DayStructure.Today, this.toHourAndMinute())
                }

                1 -> {
                    Pair(DayStructure.Yesterday, this.toHourAndMinute())
                }

                else -> {
                    Pair(
                        DayStructure.Other,
                        "${dateTime.monthNumber}/${dateTime.dayOfMonth} ${this.toHourAndMinute()}"
                    )
                }
            }
        }
    }
}

enum class DayStructure {
    Today,
    Yesterday,
    Other
}

fun String.removeTimezone(): String {
    return this.split(" ")[0]
}

fun subtractHourAndMinute(str1: String, str2: String): Pair<String, String> {
    val time1 = str1.toHourAndMinute()
    val time2 = str2.toHourAndMinute()

    val start: Instant = Instant.parse("2020-01-01T${time1}:00Z")
    val end: Instant = Instant.parse("2020-01-01T${time2}:00Z")
    val durationSinceThen: Duration = start - end

    val inWholeHours =
        if (durationSinceThen.inWholeHours < 0) -durationSinceThen.inWholeHours
        else durationSinceThen.inWholeHours
    val inWholeMinutes =
        if (durationSinceThen.inWholeMinutes < 0) -durationSinceThen.inWholeMinutes
        else durationSinceThen.inWholeMinutes

    val minute =
        if ((inWholeMinutes - (inWholeHours * 60)) > 0) -((inWholeHours * 60) - inWholeMinutes) else 0

    return Pair(
        inWholeHours.toString(),
        minute.toString()
    )
}

fun subtractDays(str1: String, str2: String): String {
    val dateTime1 = LocalDateTime.parse(str1)
    val dateTime2 = LocalDateTime.parse(str2)
    val day1 = dateTime1.dayOfYear
    val day2 = dateTime2.dayOfYear
    val year1 = dateTime1.year
    val year2 = dateTime2.year
    val diffDays = day2 - day1
    val diffYears = year2 - year1
    val diff = diffYears * 365 + diffDays
    return diff.toString()
}

fun String.toDateDailyRequest(): String {
    val dateTime = LocalDateTime.parse(this)
    val dayOfMonth = dateTime.dayOfMonth
    var month = dateTime.month.name
    val year = dateTime.year

    if (month.length > 3) {
        month = month.substring(0, 3)
    }

    return "$dayOfMonth $month $year"
}

fun String.secondsToHoursAndMinutes(): String {
    val minutes = this.toInt().div(60)
    return "${minutes.div(60).toString().padStart(2, '0')}:${
        minutes.rem(60).toString().padStart(2, '0')
    }"
}

fun nowLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse("${Clock.System.now()}".replace("Z", ""))
}

fun nowLocalDateTimeTimeZone(): String {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}