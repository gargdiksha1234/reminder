package ru.ifr0z.notify.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object AppUtils {
    fun formatDateTimeFromMillis(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(timestamp)
    }

    fun calculateTimeBetweenTwoDatesNotification(
        startDate: Long,

    ): String {


        /**
         * **** Easy Use ****
         * String currDate = new SimpleDateFormat(DateUtils.DATE_FORMATE_4).format(Calendar.getInstance().getTime());
         * String finalTime = DateUtils.calculateTimeBetweenTwoDates(list.get(position).getDatetime(), currDate);
         */
        var finalTimeString = ""
        var ago = "Ago"
        var returSec = "sec"
        var returnMin = "min"
        var returnHour = "hour"
        var returnDay = "d"
        var returnMonth = "Month"
        var returnYear = "Year"


        try {
            val currentTimeInMillis = System.currentTimeMillis()
            val timeDifferenceInMillis = startDate - currentTimeInMillis

            // Ensure that the time difference is positive
            val timeDifference = if (timeDifferenceInMillis >= 0) timeDifferenceInMillis else 0

            // Convert milliseconds to minutes, hours, and days
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
            val hours = TimeUnit.MILLISECONDS.toHours(timeDifference)
            val days = TimeUnit.MILLISECONDS.toDays(timeDifference)


            val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference)

            val months = days / 30
            val year = months / 12

            finalTimeString = if (seconds < 60) {
                if (seconds.toInt() == 0) "Time Over" else if (seconds.toInt() == 1) "$seconds sec" else "$seconds secs"//Now

            } else if (minutes < 60) {
                if (minutes == 1L) {
                    "$minutes $returnMin "
                } else {
                    minutes.toString() + " " + returnMin
                }
            } else if (hours < 24) {
                if (hours == 1L) {
                    "$hours $returnHour "
                } else {
                    hours.toString() + " " + returnHour
                }
            } else if (days < 30) {
                if (days == 1L) {
                    "$days $returnDay "
                } else {
                    days.toString() + " " + returnDay
                }
            } else if (months < 12) {
                if (months == 1L) {
                    "$months $returnMonth "
                } else {
                    months.toString() + " " + returnMonth
                }
            } else {
                if (year == 1L) {
                    "$year $returnYear "
                } else {
                    year.toString() + " " + returnYear
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return finalTimeString
    }
}