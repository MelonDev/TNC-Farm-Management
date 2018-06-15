package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.TextParams
import com.mylhyl.circledialog.params.TitleParams
import kotlinx.android.synthetic.main.activity_program_main.*
import kotlinx.android.synthetic.main.app_bar_program_main.*
import th.ac.up.agr.thai_mini_chicken.AddProgramActivity.AddProgramActivity
import th.ac.up.agr.thai_mini_chicken.CustomPlanActivity
import th.ac.up.agr.thai_mini_chicken.Fragment.*
import th.ac.up.agr.thai_mini_chicken.R
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class ProgramMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val programFragment: ProgramFragment = ProgramFragment()
    val notificationFragment: NotificationFragment = NotificationFragment()
    val historyFragment: HistoryFragment = HistoryFragment()

    val newProgramFragment: NewProgramFragment = NewProgramFragment()
    val newNotificationFragment: NewNotificationFragment = NewNotificationFragment()
    val newHistoryFragment: NewHistoryFragment = NewHistoryFragment()

    lateinit var fab: FloatingActionButton

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


        /*
        fab = program_main_activity_fab
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
*/

        ProgramViewPager(this)
        ProgramNavDrawer(this)


    }

    override fun onResume() {
        super.onResume()

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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (close) {
            super.onBackPressed()
        } else {
            setQuestionDialog()
        }
    }

    fun setQuestionDialog() {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText("คุณต้องการจะออกจากแอปหรือไม่?")
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 50
                        params.textColor = ContextCompat.getColor(this@ProgramMainActivity, R.color.colorText)
                        params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)

                    }
                })
                .setTitle("คำเตือน")
                .configTitle(object : ConfigTitle() {
                    override fun onConfig(params: TitleParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@ProgramMainActivity, MelonTheme.from(this@ProgramMainActivity).getColor())
                    }
                })
                .setPositive("ออก", {
                    //unit
                    close = true
                    this.onBackPressed()
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@ProgramMainActivity, MelonTheme.from(this@ProgramMainActivity).getColor())
                    }
                })
                .setNegative("ยกเลิก", {
                })
                .configNegative(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50

                        params.textColor = ContextCompat.getColor(this@ProgramMainActivity, R.color.colorText)

                    }
                })
                .show()


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
