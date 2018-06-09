package th.ac.up.agr.thai_mini_chicken.Data

class CustomData {
    var name: String = ""
    var key: String = ""
    var date: String = ""

    fun init(name: String, key: String, date: String): CustomData {
        this.name = name
        this.key = key
        this.date = date
        return this
    }

}