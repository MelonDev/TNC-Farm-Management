package th.ac.up.agr.thai_mini_chicken.Data

class KnowledgeData{
    var name :String = ""
    var image :String = ""

    fun set(name: String,image :Int) :KnowledgeData{
        this.name = name
        this.image = image.toString()

        return this
    }
}