package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import kotlinx.android.synthetic.main.app_bar_program_main.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
//import kotlinx.android.synthetic.main.content_program_main.*
import th.ac.up.agr.thai_mini_chicken.Fragment.HistoryFragment
import th.ac.up.agr.thai_mini_chicken.Fragment.NotificationFragment
import th.ac.up.agr.thai_mini_chicken.Fragment.ProgramFragment
import th.ac.up.agr.thai_mini_chicken.R

@Suppress("DEPRECATION")
class ProgramViewPager(val activity: ProgramMainActivity) {

    init {
        setupViewPager()
    }

    fun setupViewPager() {

        Log.e("STATUS", "ViewPager")

        //activity.programFragment.fab = activity.program_main_activity_fab

        val viewPager = activity.program_main_activity_viewpager
        setupViewPager(viewPager)

        val pageListener = PageListener()
        viewPager.setOnPageChangeListener(pageListener)

        activity.program_main_activity_tabbar.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        //val adapter = ViewPagerAdapter(myContext.supportFragmentManager)
        val adapter = ViewPagerAdapter(activity.supportFragmentManager)
        adapter.addFragment(activity.programFragment, "รายการ")
        adapter.addFragment(activity.notificationFragment, "แจ้งเตือน")
        adapter.addFragment(activity.historyFragment, "ประวัติ")
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 0

        //OverScrollDecoratorHelper.setUpOverScroll(viewPager)

    }

    private inner class PageListener : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            //Log.e(TAG, "page selected $position")
            activity.programFragment.fab = activity.program_main_activity_fab
            if (position == 0) {
                activity.programFragment.fab.show()
                activity.historyFragment.reset()
                activity.notificationFragment.reset()
            } else if (position == 1){
                activity.programFragment.fab.hide()
                activity.programFragment.reset()
                activity.historyFragment.reset()
            } else {
                activity.programFragment.fab.hide()
                activity.programFragment.reset()
                activity.notificationFragment.reset()
            }
/*
            when (position) {
                0 -> {
                    activity.notificationFragment.reset()
                    activity.historyFragment.reset()
                }
                1 -> {
                    activity.programFragment.reset()
                    activity.historyFragment.reset()
                }
                2 -> {
                    activity.notificationFragment.reset()
                    activity.programFragment.reset()
                }
            }
            */
            //resetAll()
            //currentPage = position
        }
    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
            //Log.e("Test", title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}