package th.ac.up.agr.thai_mini_chicken.HardData

import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeData
import java.util.ArrayList

class ChickenProgram {

    fun getArrayData(): ArrayList<MoreKnowledgeData> {

        val data: ArrayList<MoreKnowledgeData> = ArrayList()

        data.apply {
            add(MoreKnowledgeData("1 สัปดาห์", "นิวคลาสเซิลและหลอดลมอักเสบ", "หยอดจมูก 1 หยด", -1))
            add(MoreKnowledgeData("2 สัปดาห์", "ฝีดาษ", "เจาะผนังปีก", -1))
            add(MoreKnowledgeData("3 สัปดาห์", "กัมโบโร", "หยอดปาก", -1))
            add(MoreKnowledgeData("4 สัปดาห์", "นิวคลาสเซิลและหยอดลมอักเสบ", "หยอดจมูก 1 หยด", -1))
            add(MoreKnowledgeData("6 สัปดาห์", "ถ่ายพยาธิภายในและใช้ยามีเบนดาโซล", "ป้อนยาเม็ด 30 มิลลิกรัม", -1))
            add(MoreKnowledgeData("12 สัปดาห์", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ  1 ซีซี", -1))
            add(MoreKnowledgeData("20 สัปดาห์", "กำจัดพยาธิภายนอก", "ผสมเซพวิน 85 กับน้ำ จับไก่จุ่มลงและฉีดพ่นภายในโรงเรือน", -1))
            add(MoreKnowledgeData("20 สัปดาห์", "นิวคลาสเซิลและหยอดลมอักเสบ", "ตัวละ 1 หยด และทุกๆ 3 เดือน", -1))
            add(MoreKnowledgeData("24 สัปดาห์", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี", -1))
            add(MoreKnowledgeData("ทุกๆ 3 เดือน", "นิวคลาสเซิล", "หยอดตาหรือจมูก  1  หยด", -1))
            add(MoreKnowledgeData("6 เดือน", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี", -1))
            add(MoreKnowledgeData("ทุกๆ 6 เดือน", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี", -1))

        }

        return data
    }


}