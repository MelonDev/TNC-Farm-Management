package th.ac.up.agr.thai_mini_chicken.HardData

import th.ac.up.agr.thai_mini_chicken.Data.MoreKnowledgeData
import th.ac.up.agr.thai_mini_chicken.R
import java.util.ArrayList

class ChickenHerb {


    fun getArrayData(): ArrayList<MoreKnowledgeData> {

        val data: ArrayList<MoreKnowledgeData> = ArrayList()

        data.apply {
            add(MoreKnowledgeData("ข่า", "มีผลต่อสมรรถนะการผลิต คุณภาพซาก และการควบคุมโรคบิด", "", R.drawable.image_1))
            add(MoreKnowledgeData("ขมิ้นชัน", "สารเสริมที่ส่งผลทางประสาทสัมผัสของเนื้อไก่ มีผลต่อสมรรถนะการเจริญเติบโต คุณภาพซากและการตอบสนองของภูมิคุ้มกันโรค", "", R.drawable.image_2))
            add(MoreKnowledgeData("พโหม", "มีผลต่อภาวะความเครียด และระดับภูมิคุ้มกันโรค", "", R.drawable.image_3))
            add(MoreKnowledgeData("กระเทียม และออริกาโน", "มีผลต่อสมรรถนะการเจริญเติบโต ซาก อวัยวะและลักษณะเลือด และจุลินทรีย์ในลำไส้", "", R.drawable.image_4))
            add(MoreKnowledgeData("สะเดา", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "", R.drawable.image_5))
            add(MoreKnowledgeData("พริกขี้หนู", "มีผลต่อการเจริญเติบโต การย่อยได้บริเวณลำไส้เล็กส่วนปลาย และการเกิดลิปิดเปอร์ ออกซิเดชั่น ภายใต้สภาวะการเลี้ยงที่หนาแน่น", "", R.drawable.image_6))
            add(MoreKnowledgeData("กล้วยดิบ", "มีผลต่อสมรรถนะการผลิต คุณภาพซาก และการควบคุมโรคในไก่", "", R.drawable.image_7))
            add(MoreKnowledgeData("บอระเพ็ด", "มีผลต่อสมรรถนะการผลิต ลดอัตราการตาย มีภูมิคุ้มกันต่อโรคนิวคาสเซิล และกัมโบโร", "", R.drawable.image_8))
            add(MoreKnowledgeData("ไพล", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "", R.drawable.image_9))
            add(MoreKnowledgeData("ฟ้าทะลายโจร", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "", R.drawable.image_10))
            add(MoreKnowledgeData("ใบบัวบก", "มีผลต่อคุณลักษณะการเจริญเติบโต ปริมาณเอนไซม์จากเซลล์เยื่อบุผนังลำไส้เล็ก และ การย่อยได้ของโภชนะ", "", R.drawable.image_11))
            add(MoreKnowledgeData("ใบฝรั่ง และเปลือกมังคุด", "มีผลต่อระบบภูมิคุ้มกันโรค สัณฐานวิทยาทางลำไส้ และปริมาณเชื้อ E.coil ในไก่เนื้อ \n" +
                    "และมีผลต่อสมรรถนะการผลิต คุณภาพซากและการควบคุมโรคบิด", "", R.drawable.image_12))

        }

        return data
    }


}