package com.rokkhi.rokkhiguard.helper;

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val currentTime: String
        get() {
            val currentTimeDate = Calendar.getInstance().time
            val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.US)
            return simpleDateFormat.format(currentTimeDate)
        }

    //get device current date
    val serverReadableCurrentDate: String
        get() {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return df.format(c)
        }

    //get device current date
    val serverReadableCurrentTime: String
        get() {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("HH:mm:ss", Locale.US)
            return df.format(c)
        }

    val humanReadableCurrentDate: String
        get() {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd MMM yyyy", Locale.US)
            return df.format(c)
        }

    val humanReadableCurrentTime: String
        get() {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("hh:mm a", Locale.US)
            return df.format(c)
        }

    fun getRideHistoryFormattedDate(dateTime: String): String {
        val serverDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        var date: Date? = null
        try {
            date = serverDateFormat.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val readableDateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US)
        return readableDateFormat.format(date!!)

    }

    fun convertHumanReadableDateYearAndTime(date: String): String {
        var formatDate = ""
        var dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        try {
            val parseDate = dateFormat.parse(date)
            dateFormat = SimpleDateFormat("dd MMM yyyy hh:mm:ss aa", Locale.US)
            formatDate = dateFormat.format(parseDate!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatDate
    }

    fun convertServerReadableDateYearAndTime(date:String):String{
        var formatDate = ""
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        try {
            val parseDate = dateFormat.parse(date)
            dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            formatDate = dateFormat.format(parseDate!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatDate
    }

    fun convertReadableDateDMY(date: String): String {
        var formatDate = ""
        var dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        try {
            val parseDate = dateFormat.parse(date)
            dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
            formatDate = dateFormat.format(parseDate!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatDate
    }

    fun convertReadableDateYMD(date: String): String {
        var formatDate = ""
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        try {
            val parseDate = dateFormat.parse(date)
            dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
            formatDate = dateFormat.format(parseDate!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatDate
    }

    fun getServerReadableFormat(date: String): String {
        var formatDate = ""
        var dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        try {
            val parseDate = dateFormat.parse(date)
            dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            formatDate = dateFormat.format(parseDate!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatDate
    }

    fun getDateTime(milliSeconds: Long, dateFormat: String): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.US)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


}
