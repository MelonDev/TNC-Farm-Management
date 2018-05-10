package th.ac.up.agr.thai_mini_chicken.Fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_bar_program_main.*
import kotlinx.android.synthetic.main.app_bar_program_main.view.*
import kotlinx.android.synthetic.main.fragment_program.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter

import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig
import th.ac.up.agr.thai_nativechickenexpertsystem.Tools.QuickRecyclerView

class ProgramFragment : Fragment() {

    lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_program, container, false)

        fab = activity!!.program_main_activity_fab

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

        val recyclerView = QuickRecyclerView(context!!
                , view.program_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()

        recyclerView.adapter = ProgramAdapter(arr)

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




}
