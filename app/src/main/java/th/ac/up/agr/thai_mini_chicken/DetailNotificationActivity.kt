package th.ac.up.agr.thai_mini_chicken

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_detail_notification.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramAdapter
import th.ac.up.agr.thai_mini_chicken.Tools.QuickRecyclerView
import th.ac.up.agr.thai_mini_chicken.ViewHolder.CardVHConfig

class DetailNotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notification)

        lateinit var adapter: ProgramAdapter

        val arr = ArrayList<String>()
        arr.apply {
            add(CardVHConfig.TITLE)
            add(CardVHConfig.INJECTION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.PARASITE)
            add(CardVHConfig.INJECTION)
            add(CardVHConfig.INJECTION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.PARASITE)
            add(CardVHConfig.INJECTION)
            add(CardVHConfig.TITLE)
            add(CardVHConfig.PARASITE)
            add(CardVHConfig.PARASITE)
            add(CardVHConfig.INJECTION)
        }

        val recyclerView = QuickRecyclerView(this
                , detail_noti_recycler_view
                , "linear"
                , 1
                , "vertical"
                , false
                , "alway"
                , "high")
                .recyclerView()


        adapter = ProgramAdapter(this,3,arr)
        recyclerView.adapter = adapter

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

        detail_noti_back_btn.setOnClickListener { finish() }

    }

}
