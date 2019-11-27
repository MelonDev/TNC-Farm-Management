package th.ac.up.agr.thai_mini_chicken.Fragment


import android.content.res.ColorStateList
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.PageHistoryAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import java.util.*

class HistoryFragment : Fragment() {

    lateinit var adapter: PageHistoryAdapter
    private var run :Boolean = false

    private lateinit var recyclerView: RecyclerView

    private var arrData = ArrayList<CardData>()

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val ID = FirebaseAuth.getInstance().currentUser!!.uid

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

        mSwipeRefreshLayout = view.swipe_history_container
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.post {
                mSwipeRefreshLayout.isRefreshing = true

                onLoad()

            }
        }

        run = true

        return view
    }

    fun onLoad(){
        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progress.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    progress.visibility = View.GONE
                    arrData.clear()
                    recyclerView.adapter!!.notifyDataSetChanged()


                    p0.children.forEach {
                        val key = it.key.toString()


                        val rf = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key).child("รายละเอียด")
                        rf.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.value != null) {
                                    val slot = p0.getValue(CardData::class.java)!!

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
                                            recyclerView.adapter!!.notifyDataSetChanged()
                                            this@HistoryFragment.mSwipeRefreshLayout.isRefreshing = false
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


                                            val difference = calendar.timeInMillis - last.timeInMillis
                                            val days = (difference / (1000 * 60 * 60 * 24)).toInt()


                                            if (days == 0) {
                                                val a = arrData[arrData.lastIndex]
                                                arrData.removeAt(arrData.lastIndex)
                                                arrData.add(slot)
                                                arrData.add(a)
                                            } else {
                                                val a = CardData()
                                                a.apply {
                                                    cardID = "null"
                                                    createDate = slot.createDate
                                                }
                                                arrData.add(slot)
                                                arrData.add(a)

                                            }
                                            recyclerView.adapter!!.notifyDataSetChanged()
                                            this@HistoryFragment.mSwipeRefreshLayout.isRefreshing = false
                                        }

                                    }
                                }
                            }
                        })
                    }




                } else {
                    progress.visibility = View.GONE
                    emptyText.visibility = View.VISIBLE
                    this@HistoryFragment.mSwipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        onLoad()
    }


}
