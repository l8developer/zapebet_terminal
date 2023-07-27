package com.bet.mpos.util

import java.text.SimpleDateFormat
import java.util.*

class FunctionsK {

    fun getRangeOfDays(numberOfDays: Int): Pair<String, String> {
        val end = Calendar.getInstance()
        val start = Calendar.getInstance()
        start.add(Calendar.DAY_OF_MONTH, -numberOfDays)


        val resStart = convertTimestampToDateString(start.time.time).substring(0, 10) + " 00:00:00"
        val resEnd = convertTimestampToDateString(end.time.time).substring(0, 10) + " 23:59:59"

        return Pair(resStart, resEnd)
    }

    fun convertTimestampToDateString(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return formatter.format(calendar.time)
    }

    companion object {
        fun convertDate(date: String): String {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pt-BR"))
            val outputFormat = SimpleDateFormat("dd MMM", Locale.forLanguageTag("pt-BR"))

            val inputDate = inputFormat.parse(date)

            return outputFormat.format(inputDate)
        }

        fun convertDate2(date: String): String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pt-BR"))
            val outputFormat = SimpleDateFormat("HH:mm", Locale.forLanguageTag("pt-BR"))

            val inputDate = inputFormat.parse(date)

            return outputFormat.format(inputDate)
        }

        fun convertDate3(date: String): String{
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pt-BR"))
            val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.forLanguageTag("pt-BR"))

            val inputDate = inputFormat.parse(date)

            return outputFormat.format(inputDate)
        }

        fun convertDate4(date: String): String {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pt-BR"))
            val outputFormat = SimpleDateFormat("dd", Locale.forLanguageTag("pt-BR"))

            val inputDate = inputFormat.parse(date)

            return outputFormat.format(inputDate)
        }

        fun convertDate5(date: String): String {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pt-BR"))
            val outputFormat = SimpleDateFormat("MMM", Locale.forLanguageTag("pt-BR"))

            val inputDate = inputFormat.parse(date)

            return outputFormat.format(inputDate)
        }

    }


}