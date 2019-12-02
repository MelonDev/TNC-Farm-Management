package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.content.Intent
import android.os.Bundle

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

import com.mylhyl.circledialog.CircleDialog

import kotlinx.android.synthetic.main.activity_program_main.*
import kotlinx.android.synthetic.main.app_bar_program_main.*
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramMainViewPagerAdapter
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.CustomPlanActivity
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class ProgramMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fab: FloatingActionButton
    private var close = false

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.MelonTheme_Amber_Material)
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_main)
        setNavigationDrawer()

        fab = program_main_activity_fab
        fab.setOnClickListener {
            val intent = Intent(this, AddProgramActivity::class.java)
            intent.putExtra("ID", "0")
            intent.putExtra("USER_ID", FirebaseAuth.getInstance().currentUser!!.uid)
            startActivity(intent)
        }

        program_main_activity_custom.setOnClickListener {
            val intent = Intent(this, CustomPlanActivity::class.java)
            intent.putExtra("TYPE", "0")
            startActivity(intent)
        }

        setUpViewPagerAndTab()
        ProgramNavDrawer(this)


    }

    private fun setUpViewPagerAndTab() {
        val adapter = ProgramMainViewPagerAdapter(this)
        program_main_activity_viewpager.adapter = adapter

        TabLayoutMediator(program_main_activity_tabbar, program_main_activity_viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "รายการ"
                1 -> "แจ้งเตือน"
                else -> "ประวัติ"
            }
        }.attach()
    }

    private fun setNavigationDrawer() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()



        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            close -> {
                super.onBackPressed()
            }
            else -> {
                setQuestionDialog()
            }
        }
    }

    fun setQuestionDialog() {
        CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText("คุณต้องการจะออกจากแอปหรือไม่?")
                .configText { params ->
                    params!!.textSize = 50
                    params.textColor = ContextCompat.getColor(this@ProgramMainActivity, R.color.colorText)
                    params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
                }
                .setTitle("คำเตือน")
                .configTitle { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@ProgramMainActivity, MelonTheme.from(this@ProgramMainActivity).getColor())
                }
                .setPositive("ออก") {
                    close = true
                    this.onBackPressed()
                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@ProgramMainActivity, MelonTheme.from(this@ProgramMainActivity).getColor())
                }
                .setNegative("ยกเลิก") {
                }
                .configNegative { params ->
                    params.textSize = 50

                    params.textColor = ContextCompat.getColor(this@ProgramMainActivity, R.color.colorText)
                }
                .show(this.supportFragmentManager)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
