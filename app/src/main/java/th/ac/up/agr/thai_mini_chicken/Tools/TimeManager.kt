package th.ac.up.agr.thai_mini_chicken.Tools

import th.ac.up.agr.thai_mini_chicken.Data.Event

class TimeManager {

    //add(setEvent("","",0,0))


    companion object {
        val get = TimeManager()
    }

    val breeder :ArrayList<Event> = ArrayList()
    val meat :ArrayList<Event> = ArrayList()

    init {
        breeder.apply {
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนเอ็มพีและอหิวาต์ไก่\nพร้อมหยอดวัคซีนหลอดลมอักเสบติดต่อ",10,0))
            add(setEvent("ให้แสงสว่าง","ให้แสงสว่างไม่เกินวันละ 12 ชั่วโมง",13,0))
            add(setEvent("ตัดปาก","ตัดปากหยอดวัคซีนหลอดลมอักเสบติดต่อ",15,0))
            add(setEvent("ถ่ายพยาธิ","ถ่ายพยาธิและอาบน้ำฆ่าเหา",17,0))
            add(setEvent("ให้แสงสว่าง","ให้แสงสว่างไม่เกินวันละ 11-12 ชั่วโมง",19,0))
        }
        meat.apply {
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคหลอดลมอักเสบติดต่อ",1,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคนิวคาสเซิล",2,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคฝีดาษ",5,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคอหิวาต์เป็ด-ไก่",5,0))
            add(setEvent("ตัดปาก","ตัดปากไก่ 1/3",7,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น",7,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น",8,0))
            add(setEvent("ให้แสงสว่าง","ให้แสงสว่างไม่เกินวันละ 12 ชั่วโมง",13,0))
            add(setEvent("ฉีดวัคซีน","ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น",16,0))
        }
    }

    private fun setEvent(title :String,message:String,week :Int,day :Int) :Event{
        return Event().setEvent(title,message,week,day)
    }

}