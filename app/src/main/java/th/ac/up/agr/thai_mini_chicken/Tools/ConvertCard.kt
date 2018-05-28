package th.ac.up.agr.thai_mini_chicken.Tools

class ConvertCard {

    private val arrSystem = ArrayList<String>()
    private val arrObjective = ArrayList<String>()
    private val arrMonth = ArrayList<String>()


    fun getSystem(position :String) :String{
        arrSystem.apply {
            add("กรงตับ")
            add("ขังคอก")
            add("ปล่อยอิสระ")
            add("กึ่งขังกึ่งปล่อย")
            add("อินทรีย์")
        }
        return arrSystem[position.toInt()]
    }

    fun getSystem() :ArrayList<String>{
        arrSystem.apply {
            add("กรงตับ")
            add("ขังคอก")
            add("ปล่อยอิสระ")
            add("กึ่งขังกึ่งปล่อย")
            add("อินทรีย์")
        }
        return arrSystem
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

    fun getBool(bool :Boolean) :String{
        return if(bool){
            "เปิด"
        }else {
            "ปิด"
        }
    }

    fun getObjective(position :String) :String{
        arrObjective.apply {
            add("ไก่พ่อ-แม่พันธุ์")
            add("ขายลูกไก่")
            add("ไก่เนื้อ")
            add("ประกวด")
            add("อื่นๆ")
        }
        return arrObjective[position.toInt()]
    }

    fun getObjective() :ArrayList<String>{
        arrObjective.apply {
            add("ไก่พ่อ-แม่พันธุ์")
            add("ขายลูกไก่")
            add("ไก่เนื้อ")
            add("ประกวด")
            add("อื่นๆ")
        }
        return arrObjective
    }
}