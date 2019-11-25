package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.app_bar_program_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.fragment_program.view.*
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

    private lateinit var recyclerView: RecyclerView

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var arr: ArrayList<CardSlot>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        arr = ArrayList<CardSlot>()

        mSwipeRefreshLayout = view.swipe_notification_container
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)


        var count: Int = 0

        //val fab = activity!!.program_main_activity_fab
        //fab.hide()

        recyclerView = QuickRecyclerView(context!!
                , view.notification_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "Low")
                .recyclerView()


        adapter = PageNotificationAdapter(this, arr)
        recyclerView.adapter = adapter


        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.post {
                mSwipeRefreshLayout.isRefreshing = true

                onLoad()
                // Fetching data from server
                //loadRecyclerViewData()
            }
            //Log.e("LOAD","sdaksd")
        }

        run = true


        //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)


        return view
    }

    fun onLoad() {

        val firebase = FirebaseDatabase.getInstance().reference

        val database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายการ").child("ใช้งาน")
        val today = Calendar.getInstance()

        val x = database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    arr.clear()
                    p0.children.forEach {
                        //val ky = it.key.toString()
                        val y = database.child(it.key.toString()).child("รายการที่ต้องทำ")
                        val n = database.child(it.key.toString()).child("รายละเอียด")

                        n.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.value != null) {
                                    val cardData = p0.getValue(CardData::class.java)!!
                                    val calendar = Calendar.getInstance()

                                    calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())
                                    calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
                                    calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

                                    y.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            Log.e("","")
                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            if (p0.value != null) {

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
                                                        recyclerView.adapter!!.notifyDataSetChanged()
                                                        this@NotificationFragment.mSwipeRefreshLayout.isRefreshing = false
                                                    }
                                                }
                                            } else {
                                                arr.clear()
                                                recyclerView.adapter!!.notifyDataSetChanged()
                                                this@NotificationFragment.mSwipeRefreshLayout.isRefreshing = false

                                            }
                                        }
                                    })

                                }

                            }
                        })
                    }


                } else {
                    this@NotificationFragment.mSwipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    fun reset() {
        if (run) {
            //adapter.resetMenu()
        }

    }

    override fun onStart() {
        super.onStart()
        onLoad()
        Log.e("ACTIVITY", "ONSTART")
    }

    override fun onResume() {
        super.onResume()

        Log.e("ACTIVITY", "ONRESUME")

    }

    override fun onPause() {
        super.onPause()
        Log.e("ACTIVITY", "ONPAUSE")

    }

    override fun onStop() {
        super.onStop()

        Log.e("ACTIVITY", "ONSTOP")
    }


}
