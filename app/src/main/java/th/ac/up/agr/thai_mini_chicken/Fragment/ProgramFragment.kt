package th.ac.up.agr.thai_mini_chicken.Fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_program_main.*
import kotlinx.android.synthetic.main.fragment_program.view.*
import th.ac.up.agr.thai_mini_chicken.Adapter.PageProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.Data.CardData
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.Date
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import java.util.*
import kotlin.collections.ArrayList
import android.support.v4.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_program.*


class ProgramFragment : Fragment() {

    //lateinit var fab: FloatingActionButton
    private var run: Boolean = false
    lateinit var adapter: PageProgramAdapter

    //private var arrData = ArrayList<CardData>()

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
        //val themeWrapper = ContextThemeWrapper(activity,R.style.MelonTheme_Amber_Material)
        //val layoutInflater = inflater.cloneInContext(themeWrapper)
        //val view = layoutInflater.inflate(R.layout.fragment_program, container, false)
        val view = MelonTheme.from(activity!!).getStyleForFragment(activity, inflater).inflate(R.layout.fragment_program, container, false)

        //fab.show()

        mSwipeRefreshLayout = view.swipe_program_container
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        recyclerView = QuickRecyclerView(context!!
                , view.program_recycler_view
                , "spacial"
                , 1
                , "vertical"
                , true
                , "alway"
                , "high")
                .recyclerView()

        adapter = PageProgramAdapter(this, ID, arrData)
        recyclerView.adapter = adapter


//loadData()


        run = true

//OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
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
                // Fetching data from server
                //loadRecyclerViewData()
            }
            //Log.e("LOAD","sdaksd")
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    //getKey(p0)

                    //Log.e("PROGRAM","OUTFOR")

                    arrData.clear()
                    //Log.e("PRO","CLEAR")
/*
                    if (dataSnapshot == null && !dataSnapshot.toString().contentEquals(p0.toString())) {
                        dataSnapshot = p0
                        loadAndSetData(p0,swipe)
                    }else {
                        this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                    }
*/
                    loadAndSetData(p0,swipe)

                } else {
                    this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                }
            }
        })
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
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

                                //Log.e("SIZE",size.toString())
                                //recyclerView.adapter.notifyDataSetChanged()
                                //this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
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
                                //Log.e("SIZE",size.toString())

                                //recyclerView.adapter.notifyDataSetChanged()
                                //this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                            }


                            //arrData.add(slot)
                        } else {
                            //this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                        }
                        if (count == size) {
                            //Log.e("SWIPE",swipe.toString())
                            if (swipe) {
                                this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
                            }
                            recyclerView.adapter.notifyDataSetChanged()
                        }

                    }
                }
            })
        }
        //this@ProgramFragment.mSwipeRefreshLayout.isRefreshing = false
        //recyclerView.adapter.notifyDataSetChanged()

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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
