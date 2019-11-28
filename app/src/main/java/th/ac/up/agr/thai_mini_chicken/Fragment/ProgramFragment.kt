package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_program.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.PageProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import java.util.*
import kotlin.collections.ArrayList
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth


class ProgramFragment : Fragment() {

    private var run: Boolean = false
    lateinit var adapter: PageProgramAdapter


    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout


    private var arrKey = ArrayList<String>()

    private var arrCh = ArrayList<DataSnapshot>()

    private lateinit var recyclerView: RecyclerView
    private var arrData = ArrayList<CardData>()

    var dataSnapshot: DataSnapshot? = null

    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val ID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = MelonTheme.from(activity!!).getStyleForFragment(activity, inflater).inflate(R.layout.fragment_program, container, false)


        mSwipeRefreshLayout = view.swipe_program_container
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        recyclerView = QuickRecyclerView(context!!
                , view.program_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()

        adapter = PageProgramAdapter(this, ID, arrData)
        recyclerView.adapter = adapter


        run = true


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    //fab.hide()
                } else {
                    //fab.show()
                }
                currentScrollPosition += dy
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.post {
                mSwipeRefreshLayout.isRefreshing = true

                loadData()

            }
        }



        return view
    }

    fun loadData() {
        this.mSwipeRefreshLayout.isRefreshing = true
        onLoad(true)
    }

    fun onLoad(swipe: Boolean) {

        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {

                    arrData.clear()

                    newLoadAndSetData(p0,swipe)

                } else {
                    this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun newLoadAndSetData(p0: DataSnapshot,swipe: Boolean) {

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

                        if (slot.status.contentEquals("ACTIVE")) {

                            if (arrData.size == 0) {
                                val calendar = Calendar.getInstance()
                                val x = Date().reDate(slot.createDate)
                                calendar.time = x
                                val a = CardData()
                                a.apply {
                                    cardID = "null"
                                    createDate = slot.createDate
                                }
                                arrData.add(a)
                                arrData.add(slot)


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

                                    arrData.add(slot)

                                } else {
                                    val a = CardData()
                                    a.apply {
                                        cardID = "null"
                                        createDate = slot.createDate
                                    }
                                    arrData.add(a)
                                    arrData.add(slot)


                                }

                            }


                        }
                        if (count == size) {
                            if (swipe) {
                                this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }

                    }
                }
            })
        }


    }


    private fun loadAndSetData(p0: DataSnapshot,swipe: Boolean) {

        var size = 0

        for (it in p0.children) {
            size += 1
        }
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

                            }


                        }
                        if (count == size) {
                            if (swipe) {
                                this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }

                    }
                }
            })
        }


    }

    fun reset() {
        if (run) {
            //adapter.resetMenu()
        }

    }

    override fun onStart() {
        super.onStart()
        onLoad(false)
        //Log.e("ACTIVITY","ONSTART")
    }

    /*
        override fun onResume() {
            super.onResume()

            Log.e("ACTIVITY","ONRESUME")

        }

        override fun onPause() {
            super.onPause()
            Log.e("ACTIVITY","ONPAUSE")

        }

        override fun onStop() {
            super.onStop()

            Log.e("ACTIVITY","ONSTOP")
        }
    */
    fun getKey(dataSnapshot: DataSnapshot?) {
        arrKey.clear()
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrKey) {
            it.key
        }
    }

    fun getAll(dataSnapshot: DataSnapshot?) {
        //arrKey.clear()
        //val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!

        dataSnapshot!!.children.mapNotNullTo(arrKey) {
            it.getValue(String::class.java)

        }
    }

    fun getValue(key: String) {
        val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        val y = databaseReferences.child(key).child("รายละเอียด")

        y.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("","")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val x = p0.getValue(CardData::class.java)!!
                //arrData.add(x)
            }
        })
    }

    fun getData(p0: DataSnapshot) {

    }


}
