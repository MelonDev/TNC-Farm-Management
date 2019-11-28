package th.ac.up.agr.thai_mini_chicken.Tools

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class Date {

    fun getDate() :String{
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.forLanguageTag("th_TH"))
        return df.format(a)
    }

    fun getDay() :String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("dd", Locale.forLanguageTag("th_TH"))
        return df.format(a)
    }

    fun getMonth():String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("MM", Locale.forLanguageTag("th_TH"))
        return df.format(a)
    }

    fun getYear() :String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy", Locale.forLanguageTag("th_TH"))
        return df.format(a)
    }

    fun reDate(str :String) :Date{
        val fmt = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.forLanguageTag("th_TH"))
        return fmt.parse(str) ?: Calendar.getInstance().time
    }

    fun strToDate(str: String): LocalDate {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val localDateTime = LocalDateTime.parse(str, formatter)

        return localDateTime.toLocalDate()
    }

    fun strToDateTime(str: String): LocalDateTime {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
        val localDateTime = LocalDateTime.parse(str, formatter)

        return localDateTime
    }


}