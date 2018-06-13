package th.ac.up.agr.thai_mini_chicken.Data

class TableData {
    var title_card :String= ""
    var manual_card :String= ""
    var type_card :String = ""

    fun set(title :String,type :String,manual :String) :TableData{
        this.title_card = title
        this.type_card = type
        this.manual_card = manual
        return this
    }
}