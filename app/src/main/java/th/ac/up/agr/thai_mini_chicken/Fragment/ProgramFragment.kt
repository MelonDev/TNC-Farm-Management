package th.ac.up.agr.thai_mini_chicken.Fragment


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
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig

class ProgramFragment : Fragment() {

    lateinit var fab: FloatingActionButton
    private var run: Boolean = false
    lateinit var adapter: PageProgramAdapter

    private var arrData = ArrayList<CardData>()

    private var arrKey = ArrayList<String>()


    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val ID = "melondev_icloud_com"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val themeWrapper = ContextThemeWrapper(activity,R.style.MelonTheme_Amber_Material)
        //val layoutInflater = inflater.cloneInContext(themeWrapper)
        //val view = layoutInflater.inflate(R.layout.fragment_program, container, false)
        val view = MelonTheme.from(activity!!).getStyleForFragment(activity,inflater).inflate(R.layout.fragment_program,container,false)

        fab = activity!!.program_main_activity_fab

        val recyclerView = QuickRecyclerView(context!!
                , view.program_recycler_view
                , "spacial"
                , 1
                , "vertical"
                , true
                , "alway"
                , "high")
                .recyclerView()

        adapter = PageProgramAdapter(this.activity!!,ID,arrKey)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(context, AddProgramActivity::class.java)
            intent.putExtra("ID","0")
            startActivity(intent)
        }

        val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value != null){
                    getKey(p0)
                    recyclerView.adapter.notifyDataSetChanged()
                }
            }
        })

        val arr = ArrayList<String>()
        arr.apply {
            add(CardVHConfig.TITLE)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.INFORMATION)
            add(CardVHConfig.INFORMATION)
        }

        run = true

//OverScrollDecoratorHelper.setUpOverScroll(recyclerView,OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var currentScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
                currentScrollPosition += dy
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        return view
    }

    fun reset() {
        if (run) {
            //adapter.resetMenu()
        }

    }

    fun getKey(dataSnapshot: DataSnapshot?) {
        arrKey.clear()
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrKey){
            it.key
        }
    }

    fun getAll(dataSnapshot: DataSnapshot?) {
        //arrKey.clear()
        //val databaseReference = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrKey){
            it.getValue(String::class.java)

        }
    }

    fun getValue(key :String){
        val databaseReferences = Firebase.reference.child("ผู้ใช้").child(ID).child("รายการ").child("ใช้งาน")
        val y = databaseReferences.child(key).child("รายละเอียด")

        y.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val x = p0.getValue(CardData::class.java)!!
                arrData.add(x)
            }
        })
    }

    fun getData(dataSnapshot: DataSnapshot?) {
        arrData.clear()
        //arrData = dataSnapshot!!.getValue(DiseaseData::class.java)!!
        dataSnapshot!!.children.mapNotNullTo(arrData){
            it.getValue(CardData::class.java)
        }
    }


}
