package th.ac.up.agr.thai_mini_chicken

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_noti_detail.*
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme

class NotiDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noti_detail)

        window.statusBarColor = resources.getColor(MelonTheme.from(this).getStatusBarOverlay())





    }

}
