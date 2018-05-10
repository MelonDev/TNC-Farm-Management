package th.ac.up.agr.thai_nativechickenexpertsystem.Tools

class ConvertCard {

    private val arrSystem = ArrayList<String>()
    private val arrObjective = ArrayList<String>()
    private val arrMonth = ArrayList<String>()


    fun getSystem(position :String) :String{
        arrSystem.apply {
            add("กรงตับ")
            add("ขังคอก")
            add("ปล่อยอิสระ")
            add("อินทรีย์")
        }
        return arrSystem[position.toInt()]
    }

    fun getMonth(month :String) :String{
        arrMonth.apply {
            add("มกราคม")
            add("กุมภาพันธ์")
            add("มีนาคม")
            add("เมษายน")
            add("พฤษภาคม")
            add("มิถุนายน")
            add("กรกฎาคม")
            add("สิงหาคม")
            add("กันยายน")
            add("ตุลาคม")
            add("พฤศจิกายน")
            add("ธันวาคม")
        }
        return arrMonth[month.toInt()-1]
    }

    fun getArrMonth() :ArrayList<String> {
        arrMonth.apply {
            add("มกราคม")
            add("กุมภาพันธ์")
            add("มีนาคม")
            add("เมษายน")
            add("พฤษภาคม")
            add("มิถุนายน")
            add("กรกฎาคม")
            add("สิงหาคม")
            add("กันยายน")
            add("ตุลาคม")
            add("พฤศจิกายน")
            add("ธันวาคม")
        }
        return arrMonth
    }

    fun getYear(year :String) :String{
        return (year.toInt() + 543).toString()
    }

    fun getObjective(position :String) :String{
        arrObjective.apply {
            add("พ่อ-แม่พันธุ์")
            add("ขุนอาหาร")
        }
        return arrObjective[position.toInt()]
    }
}