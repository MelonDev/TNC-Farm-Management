package th.ac.up.agr.thai_mini_chicken.Adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import kotlinx.android.synthetic.main.app_bar_program_main.*
import th.ac.up.agr.thai_mini_chicken.Fragment.NewHistoryFragment
import th.ac.up.agr.thai_mini_chicken.Fragment.NewNotificationFragment
import th.ac.up.agr.thai_mini_chicken.Fragment.NewProgramFragment

class ProgramMainViewPagerAdapter(private val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewProgramFragment()
            1 -> NewNotificationFragment()
            else -> NewHistoryFragment()
        }
    }
}