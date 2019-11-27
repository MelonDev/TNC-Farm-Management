package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.app_bar_program_main.*


class ProgramViewPager(val activity: ProgramMainActivity) {

    init {
        setupViewPager()
    }

    fun setupViewPager() {



        val viewPager = activity.program_main_activity_viewpager
        setupViewPager(viewPager)

        val pageListener = PageListener()
        viewPager.setOnPageChangeListener(pageListener)

        activity.program_main_activity_tabbar.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(activity.supportFragmentManager)
        adapter.addFragment(activity.newProgramFragment, "รายการ")
        adapter.addFragment(activity.newNotificationFragment, "แจ้งเตือน")
        adapter.addFragment(activity.newHistoryFragment, "ประวัติ")

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3


    }

    private inner class PageListener : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            when (position) {
                0 -> activity.fab.show()
                1 -> activity.fab.hide()
                2 -> activity.fab.hide()
            }
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
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}