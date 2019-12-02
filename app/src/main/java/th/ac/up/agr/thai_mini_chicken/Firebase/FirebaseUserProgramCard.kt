package th.ac.up.agr.thai_mini_chicken.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import th.ac.up.agr.thai_mini_chicken.CallBack.ProgramCardCallBack
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Data.Event


class FirebaseUserProgramCard : Firebase() {

    private var arr: ArrayList<CardSlot> = ArrayList()


    fun getActiveProgramCard(callback: ProgramCardCallBack) {

        FirebaseAuth.getInstance().currentUser?.let {
            val database = reference.child("ผู้ใช้").child(it.uid).child("รายการ").child("ใช้งาน")

            database.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    callback.onCallback(null)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    Log.e("JI", p0.toString())
                    prepareArrayData(callback, p0)
                }

            })
        } ?: run {
            callback.onCallback(null)
        }

        /*
        val database = reference.child("ผู้ใช้").child(FirebaseAuth.getInstance().currentUser!!.uid).child("รายการ")
        database.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onCallback(null)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.e("Hi","Hello1")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.e("Hi","Hello2")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                prepareArrayData(callback,p0)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.e("Hi","Hello4")
            }

        })
         */

    }

    private fun prepareArrayData(callback: ProgramCardCallBack, dataSnapshot: DataSnapshot) {
        arr.clear()

        var countDataSnapshot = 0

        dataSnapshot.children.forEach { childDataSnapshot ->

            val detail = getDetailData(childDataSnapshot)
            detail?.let {
                val calendar = initialCalendarFromDateOnDetail(it)
                val reminderList = getListOfReminder(childDataSnapshot)

                var countReminder = 0

                reminderList.forEach { event ->
                    prepareCard(calendar, event, it)
                    countReminder += 1

                    if (countReminder == reminderList.size) {
                        callback.onCallback(arr)
                    }
                }

                if (reminderList.isEmpty()) {
                    callback.onCallback(arr)
                }

                countDataSnapshot += 1

                if (dataSnapshot.childrenCount.toInt() == countDataSnapshot) {
                    callback.onCallback(arr)
                }
            }

        }
    }


    private fun prepareCard(calendar: LocalDate, event: Event, detail: CardData) {
        val calendarEvent = initialCalendarFromDateOnEvent(calendar, event)
        val duration = findDateDuration(calendarEvent)

        val titleCard = initialTitleCard(detail, event, duration)

        if (isFutureDateOrCardIsActive(duration, event, detail)) {
            if (isCardNotDuplicated(arr, duration)) {
                arr.add(initialCard(detail, duration, event))
            }
            arr.add(titleCard)
            arr = sortData(arr)
        }
    }

    private fun isFutureDateOrCardIsActive(duration: Int, event: Event, detail: CardData): Boolean {
        return duration >= 0 && event.status.contentEquals("ACTIVE") && detail.status.contentEquals("ACTIVE")
    }

    private fun sortData(data: ArrayList<CardSlot>): ArrayList<CardSlot> {
        data.sortBy { it.duration }
        return data
    }

    private fun initialCard(detail: CardData, duration: Int, event: Event): CardSlot {
        return CardSlot().apply {
            this.day = duration.toString()
            this.dataCard = detail
            this.event = event
            this.duration = duration
        }
    }

    private fun isCardNotDuplicated(arr: ArrayList<CardSlot>, duration: Int): Boolean {
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

    private fun getDetailData(dataSnapshot: DataSnapshot): CardData? {
        val detailDataSnapshot = dataSnapshot.child("รายละเอียด")
        return detailDataSnapshot.let { childSnapshot ->
            childSnapshot.value?.let { _ ->
                getDetailFromDataSnapshot(childSnapshot)
            } ?: run { null }
        }
    }

    private fun getDetailFromDataSnapshot(childSnapshot: DataSnapshot): CardData? {
        return childSnapshot.getValue(CardData::class.java)?.let { cardData ->
            cardData
        } ?: run { null }
    }

}