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
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_new_history.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.NewHistoryAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import java.util.*

class NewHistoryFragment : Fragment() {

    private lateinit var v :View

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: NewHistoryAdapter

    val ID = FirebaseAuth.getInstance().currentUser!!.uid

    private var process: Boolean = false

    private var arrData = ArrayList<CardData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_history, container, false)
        v = view

        recyclerView = QuickRecyclerView(context!!
                , view.new_history_recycler_view
                , "spacial"
                , 1
                , "vertical"
                , true
                , "alway"
                , "high")
                .recyclerView()

        adapter = NewHistoryAdapter(this, ID, arrData)
        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()

        onPost()
    }

    fun onPost(){
        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                arrData.clear()
                recyclerView.adapter.notifyDataSetChanged()
                v.new_history_empty_area.visibility = View.VISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    if (!process) {
                        process = true

                        onDataLoad()
                    }
                } else {
                    arrData.clear()
                    recyclerView.adapter.notifyDataSetChanged()
                    v.new_history_empty_area.visibility = View.VISIBLE
                }
            }
        })

    }

    fun onDataLoad(){
        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    arrData.clear()
                    //recyclerView.adapter.notifyDataSetChanged()

                    //v.new_history_empty_area.visibility = View.GONE

                    var count = 0
                    val size = p0.children.count()

                    for(it in p0.children){
                        val key = it.key.toString()

                        val rf = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key).child("รายละเอียด")
                        rf.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1.value != null) {
                                    val slot = p1.getValue(CardData::class.java)!!

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
                                            recyclerView.adapter.notifyDataSetChanged()
                                            check()
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
                                            check()
                                        }
                                        //arrData.add(slot)

                                    }
                                }
                            }
                        })

                        count+=1

                        if(count == size){
                            process = false

                            recyclerView.adapter.notifyDataSetChanged()
                            check()
                        }

                    }




                } else {
                    //progress.visibility = View.GONE
                    //emptyText.visibility = View.VISIBLE
                }
            }
        })
    }

    fun check(){
        if(arrData.isEmpty()){
            v.new_history_empty_area.visibility = View.VISIBLE
        }else{
            v.new_history_empty_area.visibility = View.GONE

        }
    }


}
