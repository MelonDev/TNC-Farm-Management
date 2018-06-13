package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_new_notification.view.*
import kotlinx.android.synthetic.main.fragment_new_program.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.NewNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Data.Event

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import java.util.*

class NewNotificationFragment : Fragment() {

    private lateinit var v: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NewNotificationAdapter
    private lateinit var arr: ArrayList<CardSlot>

    private var process: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_notification, container, false)
        v = view
        arr = ArrayList<CardSlot>()


        recyclerView = QuickRecyclerView(context!!
                , view.new_notification_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "Low")
                .recyclerView()

        adapter = NewNotificationAdapter(this, arr)
        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()
        onDataLoad()
    }

    fun onDataLoad() {
        val firebase = FirebaseDatabase.getInstance().reference

        val database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายการ").child("ใช้งาน")
        val today = Calendar.getInstance()

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                v.new_notification_empty_area.visibility = View.VISIBLE
                //Log.e("ARR","CANCEL")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    if (!process) {
                        process = true

                        //Log.e("SIZE",p0.children.count().toString())

                        onRun()
                    }
                } else {
                    arr.clear()
                    recyclerView.adapter.notifyDataSetChanged()
                    v.new_notification_empty_area.visibility = View.VISIBLE
                    //Log.e("ARR_CANCEL",arr.size.toString())
                }
            }
        })

    }

    fun onRun() {

        val firebase = FirebaseDatabase.getInstance().reference

        val database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายการ").child("ใช้งาน")
        val today = Calendar.getInstance()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    arr.clear()

                    val si = p0.children.count()

                    var count = 0

                    for (it in p0.children){
                        //Log.e("KEY", it.key.toString())

                        val y = database.child(it.key.toString()).child("รายการที่ต้องทำ")
                        val n = database.child(it.key.toString()).child("รายละเอียด")

                        n.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p1: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1.value != null) {
                                    val cardData = p1.getValue(CardData::class.java)!!
                                    val calendar = Calendar.getInstance()

                                    calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())
                                    calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
                                    calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

                                    y.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            Log.e("","")
                                        }

                                        override fun onDataChange(p2: DataSnapshot) {
                                            if (p2.value != null) {

                                                //Log.e("C", p2.children.count().toString())

                                                var s = p2.children.count()
                                                var c = 0

                                                for(it in p2.children){

                                                    val slot = it.getValue(Event::class.java)!!

                                                    //Log.e("Z",cardData.cardID)
                                                    //Log.e("Y",arr.size.toString())

                                                    val z = calendar.clone() as Calendar

                                                    z.add(Calendar.WEEK_OF_YEAR, slot.week)
                                                    z.add(Calendar.DAY_OF_YEAR, slot.day)

                                                    val difference = z.timeInMillis - today.timeInMillis
                                                    val days = (difference / (1000 * 60 * 60 * 24)).toInt()

                                                    val cs = CardSlot()
                                                    cs.dataCard = cardData
                                                    cs.event = slot

                                                    var daysss = ""

                                                    when (days.toString().length) {
                                                        1 -> {
                                                            daysss = "0000000$days"
                                                        }
                                                        2 -> {
                                                            daysss = "000000$days"
                                                        }
                                                        3 -> {
                                                            daysss = "00000$days"
                                                        }
                                                        4 -> {
                                                            daysss = "0000$days"
                                                        }
                                                        5 -> {
                                                            daysss = "000$days"
                                                        }
                                                        6 -> {
                                                            daysss = "00$days"
                                                        }
                                                        7 -> {
                                                            daysss = "0$days"
                                                        }
                                                        else -> {
                                                            daysss = "$days"
                                                        }
                                                    }


                                                    cs.day = "${daysss}-slot"

                                                    if (days >= 0 && slot.status.contentEquals("ACTIVE") && cardData.status.contentEquals("ACTIVE")) {
                                                        if (!arr.any { it.day.contentEquals(daysss) }) {
                                                            val m = CardSlot()
                                                            arr.add(m.apply {
                                                                day = daysss
                                                                dataCard = cardData
                                                                event = slot
                                                            })
                                                        }
                                                        arr.add(cs)
                                                        arr.sortBy({ it.day })
                                                    }

                                                    c+=1

                                                    if(c == s){
                                                        recyclerView.adapter.notifyDataSetChanged()
                                                    }

                                                    countSize()

                                                }
                                            } else {

                                                countSize()

                                                recyclerView.adapter.notifyDataSetChanged()
                                            }
                                        }
                                    })

                                }

                            }
                        })


                        count += 1
                        if (si == count) {
                            process = false

                            if (arr.size == 0) {
                                v.new_notification_empty_area.visibility = View.VISIBLE
                            } else {
                                v.new_notification_empty_area.visibility = View.GONE
                            }

                            recyclerView.adapter.notifyDataSetChanged()
                        }


                    }


                }
            }
        })
    }

    fun countSize(){
        if (arr.size == 0) {
            v.new_notification_empty_area.visibility = View.VISIBLE
        } else {
            v.new_notification_empty_area.visibility = View.GONE
        }
    }


}
