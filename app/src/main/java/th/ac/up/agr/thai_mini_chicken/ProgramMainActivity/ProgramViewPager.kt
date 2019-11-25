package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
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

        //Log.e("STATUS", "ViewPager")

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
        //adapter.addFragment(activity.programFragment, "รายการ")
        adapter.addFragment(activity.newProgramFragment, "รายการ")
        //adapter.addFragment(activity.notificationFragment, "แจ้งเตือน")
        adapter.addFragment(activity.newNotificationFragment, "แจ้งเตือน")
        //adapter.addFragment(activity.historyFragment, "ประวัติ")
        adapter.addFragment(activity.newHistoryFragment, "ประวัติ")

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3

        //OverScrollDecoratorHelper.setUpOverScroll(viewPager)

    }

    private inner class PageListener : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            //Log.e("PAGE", "page selected $position")

            //activity.programFragment.fab = activity.program_main_activity_fab
            /*
            if (position == 0) {
                activity.fab.show()
                //Log.e("PAGE", "0")
                activity.historyFragment.onStop()
                activity.notificationFragment.onStop()
            } else if (position == 1){
                activity.fab.hide()
                //Log.e("PAGE", "1")
                activity.notificationFragment.onLoad()
                activity.programFragment.onStop()
                activity.historyFragment.onStop()
            } else if(position == 2){
                activity.fab.hide()
                //Log.e("PAGE", "2")
                activity.programFragment.onStop()
                activity.notificationFragment.onStop()
            }
*/
            if (position == 0) {
                activity.fab.show()
                //Log.e("PAGE", "0")
                //activity.historyFragment.onStop()
                //activity.notificationFragment.onStop()
            } else if (position == 1){
                activity.fab.hide()
                //Log.e("PAGE", "1")
                //activity.notificationFragment.onLoad()
                //activity.programFragment.onStop()
                //activity.historyFragment.onStop()
            } else if(position == 2){
                activity.fab.hide()
                //Log.e("PAGE", "2")
                //activity.programFragment.onStop()
                //activity.notificationFragment.onStop()
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