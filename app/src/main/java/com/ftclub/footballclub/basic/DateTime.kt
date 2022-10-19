package com.ftclub.footballclub.basic

import java.util.Date

object DateTime {

    private val months = mapOf(
        "Jun" to "Янв.", "Feb" to "Фев.", "Mar" to "Мар.",
        "Apr" to "Апр.", "May" to "Май.", "Jun" to "Июн.",
        "Jul" to "Июл.", "Aug" to "Авг.", "Sep" to "Сен.",
        "Oct" to "Окт.", "Nov" to "Ноя.", "Dec." to "Дек.",
    )

    private val daysWeek = mapOf(
        "Mon" to "Пон.", "Tue" to "Вт.", "Wed" to "Ср.",
        "Thu" to "Чет.", "Fri" to "Пят.", "Sat" to "Суб.", "Sun" to "Вос.",
    )

    fun getFormatDateTime(): String {
        var dateTime = getCurrentDateTime()

        dateTime = dateTime.replace("GMT+03:00", "")

        for (dayWeek in daysWeek)
            if (dateTime.contains(dayWeek.key))
                dateTime = dateTime.replace(dayWeek.key, dayWeek.value)


        for (month in months)
            if (dateTime.contains(month.key))
                dateTime = dateTime.replace(month.key, month.value)

        return dateTime
    }


    private fun getCurrentDateTime(): String{
        val date = Date()

        return "$date"
    }

}