package th.ac.up.agr.thai_mini_chicken.Tools

import th.ac.up.agr.thai_mini_chicken.Data.Event

class LoadTime {

    fun getTable(type: String): ArrayList<Event> {
        return when (type) {
            "0" -> {
                TimeManager.get.breeder
            }
            "2" -> {
                TimeManager.get.meat
            }
            else -> {
                ArrayList<Event>()
            }
        }
    }

}