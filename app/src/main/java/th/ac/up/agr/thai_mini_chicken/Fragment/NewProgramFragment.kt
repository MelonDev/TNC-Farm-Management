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
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_new_program.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.NewProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import java.util.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.Period


class NewProgramFragment : Fragment() {

    lateinit var adapter: NewProgramAdapter
    private val ID = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var recyclerView: RecyclerView

    var arrData = ArrayList<CardData>()

    private var process: Boolean = false

    private lateinit var v: View

    private var started = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_program, container, false)

        v = view



        recyclerView = QuickRecyclerView(context!!
                , view.new_program_recycler_view
                , "spacial"
                , 1
                , "vertical"
                , true
                , "alway"
                , "high")
                .recyclerView()

        adapter = NewProgramAdapter(this, ID, arrData)
        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()

        onLoad()
    }

    fun onLoad() {
        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                v.new_program_empty_area.visibility = View.VISIBLE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    if (!process) {
                        process = true

                        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                v.new_program_empty_area.visibility = View.VISIBLE
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1 != null) {
                                    arrData.clear()
                                    v.new_program_empty_area.visibility = View.GONE

                                    loadAndSetData(p1)
                                    //newLoadAndSetData(p1)
                                }
                            }
                        })
                    }
                } else {
                    arrData.clear()

                    recyclerView.adapter!!.notifyDataSetChanged()

                    v.new_program_empty_area.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun loadAndSetData(p0: DataSnapshot) {
        var size = p0.children.count()
        var count = 0

        p0.children.forEach {
            val key = it.key.toString()

            count += 1


            val rf = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน").child(key).child("รายละเอียด")
            rf.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("","")
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
                        if (slot.status.contentEquals("ACTIVE")) {

                            if (arrData.size == 0) {

                                val a = CardData()
                                a.apply {
                                    cardID = "null"
                                    createDate = slot.createDate
                                }
                                arrData.add(slot)
                                arrData.add(a)

                            } else {


                                val lSlot = arrData[arrData.lastIndex]

                                val localDateSlot = Date().strToDate(slot.createDate)
                                val localDateLastSlot = Date().strToDate(lSlot.createDate)

                                val period = Period.between(localDateSlot, localDateLastSlot)

                                if (period.days == 0) {
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




                            }

                        }


                        if (count == size) {
                            process = false

                            if (arrData.size == 0) {
                                v.new_program_empty_area.visibility = View.VISIBLE
                            } else {
                                v.new_program_empty_area.visibility = View.GONE
                            }



                            recyclerView.adapter!!.notifyDataSetChanged()
                            recyclerView.scrollToPosition(arrData.lastIndex)

                        }

                    }
                }
            })
        }
    }



}
