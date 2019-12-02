package th.ac.up.agr.thai_mini_chicken.Fragment


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_new_notification.view.*

import th.ac.up.agr.thai_mini_chicken.Adapter.NewNotificationAdapter
import th.ac.up.agr.thai_mini_chicken.CallBack.ProgramCardCallBack
import th.ac.up.agr.thai_mini_chicken.Data.CardSlot
import th.ac.up.agr.thai_mini_chicken.Firebase.FirebaseUserProgramCard

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import kotlin.collections.ArrayList

class NewNotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewNotificationAdapter
    private lateinit var arr: ArrayList<CardSlot>


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

        FirebaseUserProgramCard().getActiveProgramCard(object : ProgramCardCallBack {
            override fun onCallback(value: List<CardSlot>?) {
                val parking = value?.toCollection(ArrayList())

                parking?.let { park ->
                    arr = park
                    emptyViewManage()
                    restartAdapter()
                }

            }

        })

    }


    private fun refreshingAdapter() {
        recyclerView.adapter?.run {
            notifyDataSetChanged()
        }
    }

    private fun restartAdapter() {
        adapter = NewNotificationAdapter(this, arr)
        recyclerView.adapter = adapter
    }


    private fun emptyViewManage() {
        if (arr.isEmpty()) {
            view?.new_notification_empty_area?.visibility = View.VISIBLE
        } else {
            view?.new_notification_empty_area?.visibility = View.GONE
        }
    }


}