package th.ac.up.agr.thai_mini_chicken.ProgramMainActivity

import android.content.Intent
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_program_main.*
import kotlinx.android.synthetic.main.nav_header_program_main.view.*
import th.ac.up.agr.thai_mini_chicken.ContainerActivity
import th.ac.up.agr.thai_mini_chicken.SettingActivity

class ProgramNavDrawer(private val activity: ProgramMainActivity) {

    var navView = activity.nav_view.getHeaderView(0)

    init {
        onClick()
    }

    private fun onClick() {
        navView.nav_profile_area.setOnClickListener {
            //initIntent(ContainerActivity()).put("TITLE", "โปรไฟล์").start()
            //val intent = Intent(activity,TestLogin::class.java)
            //activity.startActivity(intent)
        }
        navView.nav_program_area.setOnClickListener {
            hideDrawer()
        }
        navView.nav_parasite_area.setOnClickListener {
            initIntent(ContainerActivity()).put("TITLE", "โปรแกรมถ่ายพยาธิ").start()
        }
        navView.nav_injection_area.setOnClickListener {
            initIntent(ContainerActivity()).put("TITLE", "โปรแกรมวัคซีน").start()
        }
        navView.nav_settings_area.setOnClickListener {
            initIntent(SettingActivity()).put("TITLE", "ตั้งค่า").start()
            activity.finish()
        }
        navView.nav_about_area.setOnClickListener {
            initIntent(ContainerActivity()).put("TITLE", "ติดต่อเรา").start()
        }
    }

    private fun Intent.put(id: String, str: String): Intent {
        this.putExtra(id, str)
        return this
    }

    private fun Intent.start() {
        activity.startActivity(this)
        hideDrawer()
    }

    private fun initIntent(path: AppCompatActivity): Intent {
        return Intent(activity, path::class.java)
    }

    private fun hideDrawer() {
        activity.drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun showDrawer() {
        activity.drawer_layout.openDrawer(GravityCompat.START)
    }

}