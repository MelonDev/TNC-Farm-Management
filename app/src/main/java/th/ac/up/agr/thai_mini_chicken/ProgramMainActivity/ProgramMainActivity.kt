package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.content.Intent
import android.os.Bundle

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_program_main.*
import kotlinx.android.synthetic.main.app_bar_program_main.*
import th.ac.up.agr.thai_mini_chicken.Adapter.ProgramMainViewPagerAdapter
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.CustomPlanActivity
import th.ac.up.agr.thai_mini_chicken.Logic.Dialog.ActionDialog
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class ProgramMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fab: FloatingActionButton
    private var close = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_main)
        setupNavigationDrawer()

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

    private fun setupNavigationDrawer() {
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
                ActionDialog(this).setTitle(R.string.alert_message).setMessage(R.string.exit_from_application_message).positive(R.string.dialog_negative_text_default).negative(R.string.exit_message) {
                    close = true
                    this.onBackPressed()
                }.build().show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
