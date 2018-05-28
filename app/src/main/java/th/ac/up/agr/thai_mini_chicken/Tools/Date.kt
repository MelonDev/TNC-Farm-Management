package th.ac.up.agr.thai_mini_chicken.Tools

import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class Date {

    fun getDate() :String{
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        val formattedDate = df.format(a)

        return formattedDate
    }

    fun getDay() :String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("dd")
        val formattedDate = df.format(a)

        return formattedDate
    }

    fun getMonth():String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("MM")
        val formattedDate = df.format(a)

        return formattedDate
    }

    fun getYear() :String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy")
        val formattedDate = df.format(a)

        return formattedDate
    }

    fun reDate(str :String) :Date{
        val fmt = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
        return fmt.parse(str)
    }

    fun reDateNull(str :String) :Date{
        val fmt = SimpleDateFormat("yyyy-MM-dd")
        return fmt.parse(str)
    }

    fun getDateNull():String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(a)

        return formattedDate
    }

    fun getDateFakeID():String {
        val a = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(a)

        return formattedDate + "-99-99-99"
    }

}