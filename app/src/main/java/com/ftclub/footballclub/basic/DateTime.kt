package com.ftclub.footballclub.basic

import java.util.Date

object DateTime {

    private val monthNum = mapOf(
        "01" to "января", "02" to "февраля", "03" to "марта", "04" to "апреля", "05" to "мая",
        "06" to "июня", "07" to "июля", "08" to "августа", "09" to "сентября", "10" to "октября",
        "11" to "ноября", "12" to "декабря",
    )

    private val monthsEng = mapOf(
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


        for (month in monthsEng)
            if (dateTime.contains(month.key))
                dateTime = dateTime.replace(month.key, month.value)

        return dateTime
    }

    fun formatPlayerAge(newYear: Int, setMonth: Int, setDay: Int): String {
        var newMonth = ""
        var newDay = ""

        when (val month = setMonth + 1) {
            in 0..9 -> newMonth = "0$month"
            in 10..12 -> newMonth = month.toString()
        }

        when (setDay) {
            in 0..9 -> newDay = "0$setDay"
            in 10..31 -> newDay = setDay.toString()
        }

        for (month in monthNum) {
            if (newMonth.contains(month.key)) newMonth = month.value
        }

        return "$newDay $newMonth $newYear г."
    }


    private fun getCurrentDateTime(): String{
        val date = Date()

        return "$date"
    }

}