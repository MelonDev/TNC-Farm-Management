package th.ac.up.agr.thai_mini_chicken

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Window

import kotlinx.android.synthetic.main.activity_splash.*
import android.content.Intent
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams
import th.ac.up.agr.thai_mini_chicken.Data.Information
import th.ac.up.agr.thai_mini_chicken.Firebase.Firebase
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
            if(FirebaseAuth.getInstance().currentUser != null){
                startProcess()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }


    fun setErrorDialog(string: String) {
        CircleDialog.Builder(this
        )
                .configDialog(object : ConfigDialog() {
                    override fun onConfig(params: DialogParams) {
                        params.canceledOnTouchOutside = false
                    }
                })
                .setText(string)
                .configText(object : ConfigText() {
                    override fun onConfig(params: TextParams?) {
                        params!!.textSize = 60
                        params.textColor = ContextCompat.getColor(this@SplashActivity, MelonTheme.from(this@SplashActivity).getColor())
                        params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                        params.height = 250
                    }
                })
                .setPositive("รับทราบ", {
                })
                .configPositive(object : ConfigButton() {
                    override fun onConfig(params: ButtonParams) {
                        params.textSize = 50
                        params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.colorText)
                    }
                }).show()


    }


    fun startProcess() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val firebase = Firebase.reference.child("ผู้ใช้").child(user.uid).child("รายละเอียด")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setErrorDialog("คำขอถูกยกเลิก")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val info = p0.getValue(Information::class.java)!!

                    if (info.farmName.isEmpty() || info.username.isEmpty() || info.phoneNumber.isEmpty() || info.farmAddress.isEmpty()) {
                        val intent = Intent(this@SplashActivity, RegisterInfoActivity::class.java)
                        finish()
                        intent.putExtra("ID", "0")
                        //setInfo()
                        firebase.removeEventListener(this)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@SplashActivity, ProgramMainActivity::class.java)
                        //setInfo()
                        startActivity(intent)
                        firebase.removeEventListener(this)
                        finish()
                    }

                } else {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

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
