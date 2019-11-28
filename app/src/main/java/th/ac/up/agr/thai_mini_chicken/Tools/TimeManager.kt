package th.ac.up.agr.thai_mini_chicken.Tools

import th.ac.up.agr.thai_mini_chicken.Data.Event

class TimeManager {

    //add(setEvent("","",0,0))


    companion object {
        val get = TimeManager()
    }

    val breeder: ArrayList<Event> = ArrayList()
    val meat: ArrayList<Event> = ArrayList()

    val oldBreeder: ArrayList<Event> = ArrayList()
    val oldMeat: ArrayList<Event> = ArrayList()

    init {
        oldBreeder.apply {
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนเอ็มพีและอหิวาต์ไก่\nพร้อมหยอดวัคซีนหลอดลมอักเสบติดต่อ", 10, 0))
            add(setEvent("ให้แสงสว่าง", "ให้แสงสว่างไม่เกินวันละ 12 ชั่วโมง", 13, 0))
            add(setEvent("ตัดปาก", "ตัดปากหยอดวัคซีนหลอดลมอักเสบติดต่อ", 15, 0))
            add(setEvent("ถ่ายพยาธิ", "ถ่ายพยาธิและอาบน้ำฆ่าเหา", 17, 0))
            add(setEvent("ให้แสงสว่าง", "ให้แสงสว่างไม่เกินวันละ 11-12 ชั่วโมง", 19, 0))
        }
        breeder.apply {
            add(setEvent("ตัดปาก", "ตัดปากรอบแรก", 1, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดนิวคลาสเซิลและหลอดลมอักเสบ โดยหยอดจมูก 1 หยด", 1, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนฝีดาษ เจาะผนังปีก", 2, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนกัมโบโร โดยหยอดปาก", 3, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิลและหยอดลมอักเสบ โดยหยอดจมูก 1 หยด", 4, 0))
            add(setEvent("ถ่ายพยาธิ", "ถ่ายพยาธิภายในและใช้ยามีเบนดาโซล ป้อนยาเม็ด 30 มิลลิกรัม", 6, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดอหิวาต์เป็ด-ไก่ เข้ากล้ามเนื้อตัวละ  1 ซีซี", 12, 0))
            add(setEvent("ให้แสงสว่าง", "ให้แสงสว่างไม่เกินวันละ 12 ชั่วโมง", 13, 0))
            add(setEvent("ตัดปาก", "ตัดปากรอบที่สอง", 15, 0))
            add(setEvent("ฉีดวัคซีน", "หยอดวัคซีนหลอดลมอักเสบติดต่อ", 15, 0))
            add(setEvent("ถ่ายพยาธิ", "ถ่ายพยาธิและอาบน้ำฆ่าเหา", 17, 0))
            add(setEvent("ให้แสงสว่าง", "ให้แสงสว่างไม่เกินวันละ 11-12 ชั่วโมง", 19, 0))
            add(setEvent("กำจัดพยาธิภายนอก", "ผสมเซพวิน 85 กับน้ำ จับไก่จุ่มลงและฉีดพ่นภายในโรงเรือน", 20, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิลและหยอดลมอักเสบ ตัวละ 1 หยด และทุกๆ 3 เดือน", 20, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนอหิวาต์เป็ด โดยฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี", 24, 0))

            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนอหิวาต์เป็ด-ไก่ เข้ากล้ามเนื้อตัวละ 1 ซีซี", 25, 5))

            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 28, 2))
            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 32, 4))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 36, 6))
            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 41, 1))
            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 45, 3))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 49, 5))

            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนอหิวาต์เป็ด-ไก่ เข้ากล้ามเนื้อตัวละ 1 ซีซี", 51, 3))

            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 54, 0))
            //add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 58, 2))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนนิวคลาสเซิล โดยหยอดตาหรือจมูก  1  หยด", 62, 4))

        }
        oldMeat.apply {
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคหลอดลมอักเสบติดต่อ", 1, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคนิวคาสเซิล", 2, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคฝีดาษ", 5, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคอหิวาต์เป็ด-ไก่", 5, 0))
            add(setEvent("ตัดปาก", "ตัดปากไก่ 1/3", 7, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น", 7, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น", 8, 0))
            add(setEvent("ให้แสงสว่าง", "ให้แสงสว่างไม่เกินวันละ 12 ชั่วโมง", 13, 0))
            add(setEvent("ฉีดวัคซีน", "ฉีดวัคซีนป้องกันโรคนิวคาสเซิลเชื้อเป็น", 16, 0))

        }
        meat.apply {
            add(setEvent("ฉีดวัคซีน", "ฉีดมาเร็กซ์ ใต้ผิวหนัง 1 โด๊ส", 0, 1))
            add(setEvent("ฉีดวัคซีน", "วัคซีนหลอดลมอักเสบ หยอดตา 1 หยด", 0, 1))

            add(setEvent("ฉีดวัคซีน", "วัคซีนนิวคาสเซิล หยอดตา 1 หยด", 1, 3))
            add(setEvent("ฉีดวัคซีน", "วัคซีนกัมโบโร ละลายน้ำ", 2, 0))
            add(setEvent("ฉีดวัคซีน", "นิวคลาสเซิล+หยอดลมอักเสบ โดย หยอดตา 1 หยด", 4, 0))
            add(setEvent("ฉีดวัคซีน", "วัคซีนฝีดาษ โดยการแทงปีก", 4, 0))
            add(setEvent("ฉีดวัคซีน", "วัคซีนหวัด ฉีดเข้ากล้ามเนื้อ 1 โด๊ส", 5, 0))
            add(setEvent("ฉีดวัคซีน", "กล่องเสียงอักเสบ หยอดตา 1 หยด", 5, 0))
            add(setEvent("ฉีดวัคซีน", "นิวคลาสเซิล+หยอดลมอักเสบ โดย หยอดตา 1 หยด", 8, 0))
            add(setEvent("ฉีดวัคซีน", "วัคซีนนิวคาสเซิล ฉีดเข้ากล้ามเนื้อ 1 โด๊ส", 8, 0))
        }
    }

    private fun setEvent(title: String, message: String, week: Int, day: Int): Event {
        var a = week * 7
        a += day
        return Event(title, message, week, day, a.toString())
    }

}