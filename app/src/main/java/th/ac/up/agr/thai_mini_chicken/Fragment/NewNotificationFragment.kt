package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_new_notification.view.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import th.ac.up.agr.thai_mini_chicken.Adapter.NewNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Data.Event

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import kotlin.collections.ArrayList

class NewNotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewNotificationAdapter
    private lateinit var arr: ArrayList<CardSlot>

    private var process: Boolean = false

    private var isLoading: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_notification, container, false)

        arr = ArrayList()

        AndroidThreeTen.init(this.activity)

        return view
    }

    private fun initialRecyclerView() {

        view?.let {
            recyclerView = QuickRecyclerView(context!!
                    , it.new_notification_recycler_view
                    , "linear"
                    , 1
                    , "vertical"
                    , false
                    , "alway"
                    , "Low")
                    .recyclerView()

            adapter = NewNotificationAdapter(this, arr)
            recyclerView.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        initialRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        val firebase = FirebaseDatabase.getInstance().reference
        val database = firebase.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายการ").child("ใช้งาน")

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                view?.new_notification_empty_area?.visibility = View.VISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                prepareBeforePrepareArrayData(p0)
            }
        })

    }

    private fun prepareBeforePrepareArrayData(p0: DataSnapshot) {
        p0.value?.let {
            if (!isLoading) {
                isLoading = true
                prepareArrayData(p0)
            }
        } ?: run {
            showEmptyView()
        }
    }

    private fun showEmptyView() {

        arr.clear()
        refreshingAdapter()
        view?.new_notification_empty_area?.visibility = View.VISIBLE
    }

    private fun refreshingAdapter() {
        recyclerView.adapter?.run {
            notifyDataSetChanged()
        }
    }

    private fun prepareArrayData(dataSnapshot: DataSnapshot) {
        arr.clear()

        var countDataSnapshot = 0

        dataSnapshot.children.forEach { childDataSnapshot ->

            val detail = getDetailData(childDataSnapshot)
            val calendar = initialCalendarFromDateOnDetail(detail)
            val reminderList = getListOfReminder(childDataSnapshot)

            var countReminder = 0

            reminderList.forEach { event ->
                prepareCard(calendar, event, detail)
                countReminder += 1

                if (countReminder == reminderList.size) {
                    refreshingAdapter()
                }
                emptyViewManage()
            }

            if (reminderList.isEmpty()) {
                emptyViewManage()
                refreshingAdapter()
            }

            countDataSnapshot += 1

            if (dataSnapshot.childrenCount.toInt() == countDataSnapshot) {
                dataSnapshotLoopFinish()
            }
        }
    }


    private fun dataSnapshotLoopFinish() {
        loadingSuccess()
        emptyViewManage()
        refreshingAdapter()
    }

    private fun prepareCard(calendar: LocalDate, event: Event, detail: CardData) {
        val calendarEvent = initialCalendarFromDateOnEvent(calendar, event)
        val duration = findDateDuration(calendarEvent)

        val titleCard = initialTitleCard(detail, event, duration)

        if (isFutureDateOrCardIsActive(duration, event, detail)) {
            if (isCardNotDuplicated(duration)) {
                initialCard(detail, duration, event)
            }
            arr.add(titleCard)
            sortDataArray()
        }
    }

    private fun isFutureDateOrCardIsActive(duration: Int, event: Event, detail: CardData): Boolean {
        return duration >= 0 && event.status.contentEquals("ACTIVE") && detail.status.contentEquals("ACTIVE")
    }

    private fun sortDataArray() {
        arr.sortBy { it.duration }
    }

    private fun initialCard(detail: CardData, duration: Int, event: Event) {
        val m = CardSlot()
        arr.add(m.apply {
            this.day = duration.toString()
            this.dataCard = detail
            this.event = event
            this.duration = duration
        })
    }

    private fun isCardNotDuplicated(duration: Int): Boolean {
        return !arr.any { it.duration == duration }
    }

    private fun initialTitleCard(detail: CardData, event: Event, duration: Int): CardSlot {
        return CardSlot().apply {
            this.dataCard = detail
            this.event = event
            this.day = "${duration}-slot"
            this.duration = duration
        }
    }

    private fun findDateDuration(calendar: LocalDate): Int {

        val date1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))
        val date2 = LocalDateTime.of(calendar, LocalTime.of(0, 0, 0))

        return Duration.between(date1, date2).toDays().toInt()
    }

    private fun initialCalendarFromDateOnEvent(calendar: LocalDate, event: Event): LocalDate {
        return calendar.plusWeeks(event.week.toLong()).plusDays(event.day.toLong())
    }

    private fun initialCalendarFromDateOnDetail(detail: CardData): LocalDate {
        return LocalDate.of(detail.dateYear.toInt(), detail.dateMonth.toInt(), detail.dateDay.toInt()).plusWeeks((0 - detail.ageWeek.toInt()).toLong()).plusDays((0 - detail.ageDay.toInt()).toLong())
    }

    private fun getListOfReminder(dataSnapshot: DataSnapshot): List<Event> {
        val eventDataSnapshot = dataSnapshot.child("รายการที่ต้องทำ")
        return dataSnapshot.child("รายการที่ต้องทำ").value?.let { _ ->
            getListEventFromDataSnapshot(eventDataSnapshot)
        } ?: arrayListOf()
    }

    private fun getListEventFromDataSnapshot(eventDataSnapshot: DataSnapshot): List<Event> {
        return eventDataSnapshot.children.map {
            it.getValue(Event::class.java) ?: Event()
        }
    }

    private fun getDetailData(dataSnapshot: DataSnapshot): CardData {
        val detailDataSnapshot = dataSnapshot.child("รายละเอียด")
        return detailDataSnapshot.let { childSnapshot ->
            childSnapshot.value?.let { _ ->
                getDetailFromDataSnapshot(childSnapshot)
            } ?: run { CardData() }
        }
    }

    private fun getDetailFromDataSnapshot(childSnapshot: DataSnapshot): CardData? {
        return childSnapshot.getValue(CardData::class.java)?.let { cardData ->
            cardData
        } ?: run { null }
    }

    private fun loadingSuccess() {
        process = false

    }

    private fun emptyViewManage() {
        if (arr.isEmpty()) {
            view?.new_notification_empty_area?.visibility = View.VISIBLE
        } else {
            view?.new_notification_empty_area?.visibility = View.GONE
        }
    }


}