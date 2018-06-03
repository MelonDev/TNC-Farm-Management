package th.ac.up.agr.thai_mini_chicken.Fragment


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_program_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Adapter.PageHistoryAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import java.util.*

class HistoryFragment : Fragment() {

    lateinit var adapter: PageHistoryAdapter
    private var run :Boolean = false

    private lateinit var recyclerView: RecyclerView

    private var arrData = ArrayList<CardData>()

    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val ID = "melondev_icloud_com"

    lateinit var progress :ProgressBar
    lateinit var emptyText :TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = QuickRecyclerView(context!!
                , view.history_recycler_view
                , "spacial"
                , 1
                , "vertical"
                , true
                , "alway"
                , "high")
                .recyclerView()

        adapter = PageHistoryAdapter(this, ID, arrData)
        recyclerView.adapter = adapter

        progress = view.fragment_history_progress
        emptyText = view.fragment_history_empty_text

        progress.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(activity!!,R.color.colorText))
        emptyText.setTextColor(ContextCompat.getColor(activity!!,R.color.colorText))

        progress.visibility = View.VISIBLE
        emptyText.visibility = View.GONE

        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progress.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    progress.visibility = View.GONE
                    //emptyText.visibility = View.GONE
                    arrData.clear()
                    recyclerView.adapter.notifyDataSetChanged()
                    p0.children.forEach {
                        val key = it.key.toString()

                        val rf = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key).child("รายละเอียด")
                        rf.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.value != null) {
                                    val slot = p0.getValue(CardData::class.java)!!
                                    //Log.e("ID", slot.createDate.toString())

                                    if (slot.cardID.isEmpty()) {
                                        slot.cardID = key
                                        val ref = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key).child("รายละเอียด").child("cardID")
                                        ref.setValue(key)
                                    }
                                    if(slot.status.contentEquals("INACTIVE")) {
                                        if (arrData.size == 0) {
                                            val calendar = Calendar.getInstance()
                                            val x = Date().reDate(slot.createDate)
                                            calendar.time = x
                                            val a = CardData()
                                            a.apply {
                                                cardID = "null"
                                                createDate = slot.createDate
                                            }
                                            arrData.add(slot)
                                            arrData.add(a)
                                            Log.e("ID", "0")
                                            recyclerView.adapter.notifyDataSetChanged()
                                        } else {
                                            val today = Calendar.getInstance()

                                            val last = Calendar.getInstance()
                                            val calendar = Calendar.getInstance()

                                            val a = Calendar.getInstance()
                                            val b = Calendar.getInstance()

                                            val lSlot = arrData[arrData.lastIndex]

                                            val x = Date().reDate(slot.createDate)
                                            val y = Date().reDate(lSlot.createDate)

                                            a.time = x
                                            b.time = y

                                            calendar.apply {
                                                set(Calendar.YEAR, a.get(Calendar.YEAR))
                                                set(Calendar.MONTH, a.get(Calendar.MONTH))
                                                set(Calendar.DAY_OF_MONTH, a.get(Calendar.DAY_OF_MONTH))
                                            }

                                            last.apply {
                                                set(Calendar.YEAR, b.get(Calendar.YEAR))
                                                set(Calendar.MONTH, b.get(Calendar.MONTH))
                                                set(Calendar.DAY_OF_MONTH, b.get(Calendar.DAY_OF_MONTH))
                                            }

                                            //Log.e("C",calendar.get(Calendar.DAY_OF_MONTH).toString())
                                            //Log.e("L",last.get(Calendar.DAY_OF_MONTH).toString())

                                            //calendar.set(slot.dateYear.toInt(), slot.dateMonth.toInt() - 1, slot.dateDay.toInt())
                                            //last.set(lSlot.dateYear.toInt(), lSlot.dateMonth.toInt() - 1, lSlot.dateDay.toInt())
                                            val difference = calendar.timeInMillis - last.timeInMillis
                                            val days = (difference / (1000 * 60 * 60 * 24)).toInt()

                                            //Log.e(slot.createDate,days.toString())

                                            if (days == 0) {
                                                val a = arrData[arrData.lastIndex]
                                                arrData.removeAt(arrData.lastIndex)
                                                arrData.add(slot)
                                                arrData.add(a)
                                                //Log.e("P","A")
                                            } else {
                                                //Log.e("P","B")
                                                val a = CardData()
                                                a.apply {
                                                    cardID = "null"
                                                    createDate = slot.createDate
                                                }
                                                arrData.add(slot)
                                                arrData.add(a)

                                            }
                                            //Log.e("PRO", "LOAD")
                                            recyclerView.adapter.notifyDataSetChanged()
                                        }
                                        //arrData.add(slot)

                                    }
                                }
                            }
                        })
                    }




                } else {
                    progress.visibility = View.GONE
                    emptyText.visibility = View.VISIBLE
                }
            }
        })


        run = true

        return view
    }

    fun reset(){
        if (run){
            //adapter.resetMenu()
        }

    }

}
