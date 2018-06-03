package th.ac.up.agr.thai_mini_chicken

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_detail_notification.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Adapter.DetailNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Data.Event
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
import th.ac.up.agr.thai_mini_chicken.Tools.LoadTime
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.firebase.database.*
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import java.util.*
import kotlin.collections.ArrayList


class DetailNotificationActivity : AppCompatActivity() {

    lateinit var adapter: DetailNotificationAdapter

    private var arrEvent = ArrayList<Event>()
    private var arrPassed = ArrayList<Event>()
    lateinit var recyclerView: RecyclerView

    lateinit var ref: DatabaseReference

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notification)

        val bundle = intent.extras
        val card_key = bundle.getString("CARD_KEY")
        val user_ID = bundle.getString("USER_ID")
        type = bundle.getString("TYPE")

        //val arr = LoadTime().getTable(type)

        recyclerView = QuickRecyclerView(this
                , detail_noti_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()


        if (type.contentEquals("0")) {
            activity_detail_title_text.text = "รายการที่ต้องทำ"
            detail_noti_add_text_btn.visibility = View.VISIBLE
            adapter = DetailNotificationAdapter(this, type.toInt(), arrEvent, arrPassed, card_key, user_ID)
            notification_detail_fab_add.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,MelonTheme.from(this).getColor()));
            notification_detail_fab_add.show()
        } else if (type.contentEquals("1")) {
            activity_detail_title_text.text = "ประวัติ"
            detail_noti_add_text_btn.visibility = View.GONE
            adapter = DetailNotificationAdapter(this, type.toInt(), arrEvent, arrPassed, card_key, user_ID)
            notification_detail_fab_add.hide()
        }

        recyclerView.adapter = adapter

        val userRef = Firebase.reference.child("ผู้ใช้").child(user_ID)
        val container = userRef.child("รายการ")

        ref = container.child("ใช้งาน").child(card_key).child("รายการที่ต้องทำ")

        val refs = container.child("ใช้งาน").child(card_key).child("รายละเอียด")

        refs.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val slot = p0.getValue(CardData::class.java)!!

                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.value != null) {
                                getEvents(p0, slot)

                                recyclerView.adapter.notifyDataSetChanged()
                                //recyclerView.layoutManager.smoothScrollToPosition(recyclerView, RecyclerView.State(), 0)

                            }
                        }
                    })

                }


            }
        })


        //OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        detail_noti_back_btn.setOnClickListener { finish() }
        detail_noti_add_text_btn.setOnClickListener {
            val intent = Intent(this, DetailNotificationActivity::class.java)
            intent.putExtra("USER_ID", user_ID)
            intent.putExtra("CARD_KEY", card_key)
            intent.putExtra("TYPE", "1")
            startActivity(intent)
        }

    }

    /*
        var i = 0

        while (i < data.size) {
            if (arr.size == 0) {
                val a = data[0]
                val b = Event()
                b.apply {
                    title = "null"
                    day = a.day
                    week = a.week
                    message = "null"
                }
                arr.add(b)
                arr.add(data[i])
                Log.e("1",i.toString())
            } else if(i == data.size - 1) {
                arr.add(data[i])
                Log.e("2",i.toString())
            } else {
                if((data[i].week != arr[arr.lastIndex].week) && (data[i].day != arr[arr.lastIndex].day)){
                    val a = data[i]
                    val b = Event()
                    b.apply {
                        title = "null"
                        day = a.day
                        week = a.week
                        message = "null"
                    }
                    arr.add(b)
                    arr.add(data[i])
                    Log.e("3",i.toString())
                }else {
                    arr.add(data[i])
                    Log.e("4",i.toString())
                }
            }
            i+=1
        }
    */

    fun getEvent(dataSnapshot: DataSnapshot?) {
        arrEvent.clear()

        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrEvent) {
            //val a = it.getValue(Event::class.java)!!
            val slot = it.getValue(Event::class.java)!!

            if (slot.cardID.isEmpty()) {
                slot.cardID = it.key.toString()

                val a = ref.child(it.key.toString())
                a.setValue(slot)
            }

            if (arrEvent.size == 0) {
                val b = Event()
                b.apply {
                    title = "null"
                    day = slot.day
                    week = slot.week
                    message = "null"
                }
                arrEvent.add(b)
                slot
            } else {
                if ((slot.week != arrEvent[arrEvent.lastIndex].week) || (slot.day != arrEvent[arrEvent.lastIndex].day)) {
                    val b = Event()
                    b.apply {
                        title = "null"
                        day = slot.day
                        week = slot.week
                        message = "null"
                    }
                    arrEvent.add(b)
                    slot
                } else {
                    slot
                }
            }
        }

    }

    fun getEvents(dataSnapshot: DataSnapshot?, cardData: CardData) {
        arrEvent.clear()
        arrPassed.clear()
/*
        val z = Event()
        z.apply {
            title = "INACTIVE_BUTTON"
            day = -1
            week = -1
            message = "BUTTON"
        }
        arrEvent.add(z)
*/
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        calendar.set(cardData.dateYear.toInt(), cardData.dateMonth.toInt() - 1, cardData.dateDay.toInt())

        calendar.add(Calendar.WEEK_OF_YEAR, 0 - cardData.ageWeek.toInt())
        calendar.add(Calendar.DAY_OF_YEAR, 0 - cardData.ageDay.toInt())

        dataSnapshot!!.children.forEach {
            //Log.e("KEY",it.key.toString())

            val slot = it.getValue(Event::class.java)!!

            if (slot.cardID.isEmpty()) {
                slot.cardID = it.key.toString()

                val a = ref.child(it.key.toString())
                a.setValue(slot)
            }

            val x = calendar.clone() as Calendar

            x.add(Calendar.WEEK_OF_YEAR, slot.week)
            x.add(Calendar.DAY_OF_YEAR, slot.day)

            val difference = x.timeInMillis - today.timeInMillis
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()

            //Log.e("DAY", days.toString())

            if (type.contentEquals("0")) {
                if (days >= 0 && slot.status.contentEquals("ACTIVE")) {
                    /*if (arrEvent.size == -1) {
                        val b = Event()
                        b.apply {
                            title = "history"
                            day = -1
                            week = -1
                            message = "history"
                        }
                        arrEvent.add(b)
                        //arrEvent.add(slot)
                    } else
                        */

                    if (arrEvent.size == 0) {
                        val b = Event()
                        b.apply {
                            title = "null"
                            day = slot.day
                            week = slot.week
                            message = "null"
                        }
                        arrEvent.add(b)
                        arrEvent.add(slot)
                    } else {
                        if ((slot.week != arrEvent[arrEvent.lastIndex].week) || (slot.day != arrEvent[arrEvent.lastIndex].day)) {
                            val b = Event()
                            b.apply {
                                title = "null"
                                day = slot.day
                                week = slot.week
                                message = "null"
                            }
                            arrEvent.add(b)
                            arrEvent.add(slot)
                        } else {
                            arrEvent.add(slot)
                        }
                    }
                }
            }
            if (type.contentEquals("1")) {
                //Log.e("DAYS",days.toString())
                if (days < 0 || slot.status.contentEquals("CANCEL") || slot.status.contentEquals("PASSED")) {
                    if (arrPassed.size == 0) {
                        val b = Event()
                        b.apply {
                            title = "null"
                            day = slot.day
                            week = slot.week
                            message = "null"
                        }
                        arrPassed.add(b)
                        arrPassed.add(slot)
                    } else {
                        if ((slot.week != arrPassed[arrPassed.lastIndex].week) || (slot.day != arrPassed[arrPassed.lastIndex].day)) {
                            val b = Event()
                            b.apply {
                                title = "null"
                                day = slot.day
                                week = slot.week
                                message = "null"
                            }
                            arrPassed.add(b)
                            arrPassed.add(slot)
                        } else {
                            arrPassed.add(slot)
                        }
                    }
                }
            }


        }

    }
}
