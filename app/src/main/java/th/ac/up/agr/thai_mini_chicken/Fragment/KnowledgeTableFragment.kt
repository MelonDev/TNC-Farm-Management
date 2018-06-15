package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_knowledge_table.*
import kotlinx.android.synthetic.main.fragment_knowledge_table.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.TableAdapter
import th.ac.up.agr.thai_mini_chicken.Data.TableData

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView


class KnowledgeTableFragment : Fragment() {

    companion object {

        fun newInstance(ID: String): KnowledgeTableFragment {
            val args = Bundle()
            //args.putString("TITLE", title)
            args.putString("ID", ID)
            val fragment = KnowledgeTableFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_knowledge_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ID = arguments!!.getString("ID")

        val data = ArrayList<TableData>()

        if (ID.contentEquals("0")) {
            data.apply {
                add(TableData().set("1 สัปดาห์", "นิวคลาสเซิลและหลอดลมอักเสบ", "หยอดจมูก 1 หยด",-1))
                add(TableData().set("2 สัปดาห์", "ฝีดาษ", "เจาะผนังปีก",-1))
                add(TableData().set("3 สัปดาห์", "กัมโบโร", "หยอดปาก",-1))
                add(TableData().set("4 สัปดาห์", "นิวคลาสเซิลและหยอดลมอักเสบ", "หยอดจมูก 1 หยด",-1))
                add(TableData().set("6 สัปดาห์", "ถ่ายพยาธิภายในและใช้ยามีเบนดาโซล", "ป้อนยาเม็ด 30 มิลลิกรัม",-1))
                add(TableData().set("12 สัปดาห์", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ  1 ซีซี",-1))
                add(TableData().set("20 สัปดาห์", "กำจัดพยาธิภายนอก", "ผสมเซพวิน 85 กับน้ำ จับไก่จุ่มลงและฉีดพ่นภายในโรงเรือน",-1))
                add(TableData().set("20 สัปดาห์", "นิวคลาสเซิลและหยอดลมอักเสบ", "ตัวละ 1 หยด และทุกๆ 3 เดือน",-1))
                add(TableData().set("24 สัปดาห์", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี",-1))
                add(TableData().set("ทุกๆ 3 เดือน", "นิวคลาสเซิล", "หยอดตาหรือจมูก  1  หยด",-1))
                add(TableData().set("6 เดือน", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี",-1))
                add(TableData().set("ทุกๆ 6 เดือน", "อหิวาต์เป็ด-ไก่", "ฉีดเข้ากล้ามเนื้อตัวละ 1 ซีซี",-1))

            }
        } else {
            data.apply {
                add(TableData().set("ข่า", "มีผลต่อสมรรถนะการผลิต คุณภาพซาก และการควบคุมโรคบิด", "",R.drawable.h1))
                add(TableData().set("ขมิ้นชัน", "สารเสริมที่ส่งผลทางประสาทสัมผัสของเนื้อไก่ มีผลต่อสมรรถนะการเจริญเติบโต คุณภาพซากและการตอบสนองของภูมิคุ้มกันโรค", "",R.drawable.h2))
                add(TableData().set("พโหม", "มีผลต่อภาวะความเครียด และระดับภูมิคุ้มกันโรค", "",R.drawable.h3))
                add(TableData().set("กระเทียม และออริกาโน", "มีผลต่อสมรรถนะการเจริญเติบโต ซาก อวัยวะและลักษณะเลือด และจุลินทรีย์ในลำไส้", "",R.drawable.h4))
                add(TableData().set("สะเดา", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "",R.drawable.h5))
                add(TableData().set("พริกขี้หนู", "มีผลต่อการเจริญเติบโต การย่อยได้บริเวณลำไส้เล็กส่วนปลาย และการเกิดลิปิดเปอร์ ออกซิเดชั่น ภายใต้สภาวะการเลี้ยงที่หนาแน่น", "",R.drawable.h6))
                add(TableData().set("กล้วยดิบ", "มีผลต่อสมรรถนะการผลิต คุณภาพซาก และการควบคุมโรคในไก่", "",R.drawable.h7))
                add(TableData().set("บอระเพ็ด", "มีผลต่อสมรรถนะการผลิต ลดอัตราการตาย มีภูมิคุ้มกันต่อโรคนิวคาสเซิล และกัมโบโร", "",R.drawable.h8))
                add(TableData().set("ไพล", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "",R.drawable.h9))
                add(TableData().set("ฟ้าทะลายโจร", "มีผลต่อน้ำหนักตัวสูงขึ้น ประสิทธิภาพการเปลี่ยนเป็นน้ำหนัก", "",R.drawable.h10))
                add(TableData().set("ใบบัวบก", "มีผลต่อคุณลักษณะการเจริญเติบโต ปริมาณเอนไซม์จากเซลล์เยื่อบุผนังลำไส้เล็ก และ การย่อยได้ของโภชนะ", "",R.drawable.h11))
                add(TableData().set("ใบฝรั่ง และเปลือกมังคุด", "มีผลต่อระบบภูมิคุ้มกันโรค สัณฐานวิทยาทางลำไส้ และปริมาณเชื้อ E.coil ในไก่เนื้อ \n" +
                        "และมีผลต่อสมรรถนะการผลิต คุณภาพซากและการควบคุมโรคบิด", "",R.drawable.h12))

            }
        }


        val recyclerView = QuickRecyclerView(context!!
                , view.knowledge_table_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()

        val adapter = TableAdapter(this, ID.toInt(), data)

        recyclerView.adapter = adapter
    }


}
