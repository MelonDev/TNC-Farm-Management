package th.ac.up.agr.thai_mini_chicken.Data

class Event {

    var title :String = ""
    var message :String = ""
    var week :Int = 0
    var day :Int = 0

    var cardID :String = ""
    var fromID :String = ""
    var status :String = ""

    fun setEvent(title :String,message:String,week :Int,day :Int) :Event{
        this.title = title
        this.message = message
        this.week = week
        this.day = day
        return this
    }



}