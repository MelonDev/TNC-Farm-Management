package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.app_bar_program_main.*
import kotlinx.android.synthetic.main.fragment_notification.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Adapter.PageNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Data.ArraySlot
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import java.util.*


class NotificationFragment : Fragment() {

    lateinit var adapter: PageNotificationAdapter
    private var run: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        val arr = ArrayList<CardSlot>()
        var count: Int = 0

        //val fab = activity!!.program_main_activity_fab
        //fab.hide()

        val recyclerView = QuickRecyclerView(context!!
                , view.notification_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "Low")
                .recyclerView()


        adapter = PageNotificationAdapter(this.activity!!, arr)
        recyclerView.adapter = adapter

        val firebase = FirebaseDatabase.getInstance().reference

        val database = firebase.child("ผู้ใช้").child("melondev_icloud_com").child("รายการ").child("ใช้งาน")
        val today = Calendar.getInstance()

        val x = database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    arr.clear()
                    p0.children.forEach {
                        val ky = it.key.toString()
                        val y = database.child(it.key.toString()).child("รายการที่ต้องทำ")
                        val n = database.child(it.key.toString()).child("รายละเอียด")

                        n.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.value != null) {
                                    if (ky.indexOf("-99-99-99") == -1) {
                                        val cardData = p0.getValue(CardData::class.java)!!
                                        val calendar = Calendar.getInstance()

                                        calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())
                                        calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
                                        calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

                                        y.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {
                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                p0.children.forEach {
                                                    if (p0.value != null) {
                                                        val slot = it.getValue(Event::class.java)!!

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

                                                        if (days >= 0) {
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
                                                        recyclerView.adapter.notifyDataSetChanged()
                                                    }
                                                }
                                            }
                                        })

                                    }
                                }
                            }
                        })
                    }

                    //Log.e("SIZE",arr.size.toString())

                }
            }
        })

        run = true


        //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)


        return view
    }

    fun reset() {
        if (run) {
            //adapter.resetMenu()
        }

    }


}
