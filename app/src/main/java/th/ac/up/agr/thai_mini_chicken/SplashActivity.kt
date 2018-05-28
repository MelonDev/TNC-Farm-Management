package th.ac.up.agr.thai_mini_chicken

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Window

import kotlinx.android.synthetic.main.activity_splash.*
import android.content.Intent
import th.ac.up.agr.thai_mini_chicken.ProgramMainActivity.ProgramMainActivity
import th.ac.up.agr.thai_mini_chicken.Tools.MelonTheme


class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var delay_time: Long = 0
    private var time: Long = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(MelonTheme.from(this).getStyle())
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        handler = Handler()

        runnable = Runnable {
            val intent = Intent(this, ProgramMainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        delay_time = time
        handler.postDelayed(runnable, delay_time)
        time = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

}
