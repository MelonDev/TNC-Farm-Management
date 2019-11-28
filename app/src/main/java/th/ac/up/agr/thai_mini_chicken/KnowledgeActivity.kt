package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_knowledge.*
import th.ac.up.agr.thai_mini_chicken.Adapter.KnowledgeAdapter
import th.ac.up.agr.thai_mini_chicken.Data.KnowledgeData
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView

class KnowledgeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(MelonTheme.from(this).getStyle())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge)

        val bundle = intent.extras!!
        val title = bundle.getString("TITLE")
        val ID = bundle.getString("ID")!!

        knowledge_back_btn.setOnClickListener {
            finish()
        }

        knowledge_title_name.text = title

        val data = ArrayList<KnowledgeData>()

        if(ID.contentEquals("0")){
            data.apply {
                add(KnowledgeData("โปรแกรมวัคซีนและยาถ่ายพยาธิ", R.drawable.ic_injection_icon))
                add(KnowledgeData("เทคนิคการเลี้ยงไก่พื้นเมือง", R.drawable.ic_technical_icon))
                add(KnowledgeData("วิธีการทำน้ำหมักเอนไซน์แทนการใช้สารเคมี", R.drawable.ic_molecule_icon))
                add(KnowledgeData("ยาถ่ายโบราณ", R.drawable.ic_mortar_icon))
                add(KnowledgeData("สมุนไพรที่ใช้เลี้ยงไก่", R.drawable.ic_herb_icon))

            }
        } else if(ID.contentEquals("1")){
            data.apply {
                add(KnowledgeData("น้ำหมักสมุนไพรแทนวัคซีน/ปฏิชีวนะ", R.drawable.ic_herb_icon))
                add(KnowledgeData("น้ำหมักสมุนไพรรักษาโรคทั่วไป", R.drawable.ic_herb_icon))
                add(KnowledgeData("น้ำหมักขึ้นฉ่าย", R.drawable.ic_herb_icon))
                add(KnowledgeData("กำจัดไรด้วยหางไหล", R.drawable.ic_herb_icon))
                add(KnowledgeData("ฮอร์โมนชีวภาพ", R.drawable.ic_herb_icon))
                add(KnowledgeData("น้ำหมักสมุนไพรบำรุงสุขภาพ", R.drawable.ic_herb_icon))

            }
        }

        recyclerView = QuickRecyclerView(this
                , knowledge_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()

        val adapter = KnowledgeAdapter(this,ID,data)

        recyclerView.adapter = adapter


    }


}
